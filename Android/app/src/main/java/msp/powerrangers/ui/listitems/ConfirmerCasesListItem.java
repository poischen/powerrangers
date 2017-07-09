package msp.powerrangers.ui.listitems;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import msp.powerrangers.R;

/**
 * == Data
 * Tutorial: https://www.sitepoint.com/mastering-complex-lists-with-the-android-recyclerview/
 */
public class ConfirmerCasesListItem {

    public String title;
    public String city;
    public String country;
    public String comment;
    public int imageId;

    String titleDB;
    String cityDB;
    String countryDB;
    String commentDB;
    int imageIdDB;

    List<ConfirmerCasesListItem> data = new ArrayList<>();
    private DatabaseReference dbRefCases;

    public ConfirmerCasesListItem(String title, String city, String country, String comment, int imageId) {
        this.title = title;
        this.city = city;
        this.country = country;
        this.comment = comment;
        this.imageId = imageId;
    }

    public ConfirmerCasesListItem() {

    }

    /**
     * Generates Confirmer Cases Objects for RecyclerView's adapter.
     */
    public List<ConfirmerCasesListItem> fill_with_data() {

        // get the reference to the db cases
        dbRefCases = FirebaseDatabase.getInstance().getReference("cases");

        dbRefCases.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // get data for each case from the db
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    titleDB = (String) singleSnapshot.child("name").getValue();
                    cityDB = (String) singleSnapshot.child("city").getValue();
                    countryDB = (String) singleSnapshot.child("country").getValue();
                    commentDB = (String) singleSnapshot.child("comment").getValue();
                    // TODO: get first image for case from db
                    imageIdDB = R.drawable.placeholder_case;
                    data.add(new ConfirmerCasesListItem(titleDB, cityDB, countryDB, commentDB, imageIdDB));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

      /*  data.add(new ConfirmerCasesListItem("Munich, Germany", "English Garden, 25.06.2017 ...", R.drawable.placeholder_case));
        data.add(new ConfirmerCasesListItem("Munich, Germany", "Oettingenstr, 23.06.2017 ...", R.drawable.placeholder_case));
        data.add(new ConfirmerCasesListItem("Casablanca, Marokko", "Summary: ....", R.drawable.placeholder_case));
        data.add(new ConfirmerCasesListItem("Colombo, Sri Lanka", "Summary of the case in Colombo...", R.drawable.placeholder_case));

        */
        return data;
    }

}
