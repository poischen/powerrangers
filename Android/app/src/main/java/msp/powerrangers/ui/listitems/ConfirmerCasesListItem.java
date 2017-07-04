package msp.powerrangers.ui.listitems;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import msp.powerrangers.R;

/**
 * == Data
 * Tutorial: https://www.sitepoint.com/mastering-complex-lists-with-the-android-recyclerview/
 *
 */
public class ConfirmerCasesListItem {

    public String title;
    public String desc;
    public int imageID;

    List<ConfirmerCasesListItem> data;
    private DatabaseReference  dbRefCases;

    private ConfirmerCasesListItem(String title, String desc, int imageID) {
        this.title = title;
        this.desc = desc;
        this.imageID = imageID;
    }

    /**
     * Generates RT-Objects for RecyclerView's adapter.
     */
    public  List<ConfirmerCasesListItem> fill_with_data() {

        dbRefCases  = FirebaseDatabase.getInstance().getReference("cases");
        final String dbId = dbRefCases.push().getKey();

        dbRefCases.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    data = new ArrayList<>();
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                        title = (String) singleSnapshot.child(dbId).child("name").getValue();
                        // TODO: build desc from all case informations
                        desc = (String) singleSnapshot.child(dbId).child("comment").getValue();
                        imageID = R.drawable.placeholder_case;


                    }
                    data.add(new ConfirmerCasesListItem(title, desc, imageID));

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
