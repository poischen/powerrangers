package msp.powerrangers.ui.listitems;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import msp.powerrangers.ui.FragmentConfirmerCases;
import msp.powerrangers.ui.FragmentWait;

/**
 * == Data
 * Tutorial: https://www.sitepoint.com/mastering-complex-lists-with-the-android-recyclerview/
 */
public class ConfirmerCasesListItem implements Serializable {

    public String title;
    public String city;
    public String country;
    public String comment;
    public int imageId;
    public String caseImageUrlDB;

    String titleDB;
    String cityDB;
    String countryDB;
    String commentDB;


    List<ConfirmerCasesListItem> data = new ArrayList<>();


    public ConfirmerCasesListItem(String title, String city, String country, String comment, String imageURL) {
        this.title = title;
        this.city = city;
        this.country = country;
        this.comment = comment;
        this.caseImageUrlDB = imageURL;
    }

    public ConfirmerCasesListItem() {

    }


    public List<ConfirmerCasesListItem> getData(){
        return data;
    }

    /**
     * Generates Confirmer Cases Objects for RecyclerView's adapter.
     */
    public void fill_with_data(final FragmentWait fragmentWait) {
        DatabaseReference dbRefCases;

        // get the reference to the db cases
        dbRefCases = FirebaseDatabase.getInstance().getReference("cases");

        dbRefCases.orderByChild("confirmed").equalTo(false).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get data for each case from the db
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    titleDB = (String) singleSnapshot.child("name").getValue();
                    cityDB = (String) singleSnapshot.child("city").getValue();
                    countryDB = (String) singleSnapshot.child("country").getValue();
                    commentDB = (String) singleSnapshot.child("comment").getValue();
                    caseImageUrlDB = (String) singleSnapshot.child("pictureURL").child("0").getValue();
                    data.add(new ConfirmerCasesListItem(titleDB, cityDB, countryDB, commentDB, caseImageUrlDB));
                }

                fragmentWait.changeToContentView(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

}
