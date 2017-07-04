package msp.powerrangers.ui.listitems;

import android.util.Log;

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
    //public int imageID;

    String name;
    String comment;

    List<ConfirmerCasesListItem> data = new ArrayList<>();
    private DatabaseReference  dbRefCases;

    public ConfirmerCasesListItem(String title, String desc){
        this.title = title;
        this.desc = desc;
        //this.imageID = imageID;
    }

    public ConfirmerCasesListItem(){

    }

    /**
     * Generates RT-Objects for RecyclerView's adapter.
     */
    public List<ConfirmerCasesListItem> fill_with_data() {

        dbRefCases  = FirebaseDatabase.getInstance().getReference("cases");

        dbRefCases.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //data = new ArrayList<>();;

                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                       name = (String) singleSnapshot.child("name").getValue();
                     //   Log.i("Name: ", name);

                        // TODO: build desc from all case informations
                        comment  = (String) singleSnapshot.child("comment").getValue();
                    //    Log.i("Comment: ", comment);


                        data.add(new ConfirmerCasesListItem(name, comment));

                    //    Log.i("DIE LISTE: " , data.toString());


/*
                        note.setUid(database.child("notes").push().getKey());
                        note.setTitle(titleTextView.getText().toString());
                        note.setDescription(descriptionTextView.getText().toString());
                        database.child("notes").child(note.getUid()).setValue(note);*/

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
