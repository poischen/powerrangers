package msp.powerrangers.ui.listitems;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import msp.powerrangers.R;
import msp.powerrangers.ui.FragmentWait;

/**
 * == Data
 * Tutorial: https://www.sitepoint.com/mastering-complex-lists-with-the-android-recyclerview/
 *
 */
public class UsersOpenTasksListItem {

    public String title;
    public String desc;
    public int imageID;


    String titleDB;
    String cityDB;
    String countryDB;
    String commentDB;
    int imageIdDB;

    List<UsersOpenTasksListItem> data = new ArrayList<>();
    private DatabaseReference dbRefTasks;

    public UsersOpenTasksListItem(String title, String desc, int imageID) {
        this.title = title;
        this.desc = desc;
        this.imageID = imageID;
    }

    public UsersOpenTasksListItem(){

    }

    public void fill_with_data(final FragmentWait fragmentWait) {

        // get the reference to the db tasks
        dbRefTasks = FirebaseDatabase.getInstance().getReference("tasks");

        dbRefTasks.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // get data for each task from the db
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    cityDB = (String) singleSnapshot.child("city").getValue();
                    countryDB = (String) singleSnapshot.child("country").getValue();

                    titleDB = cityDB + " , " + countryDB;

                    commentDB = (String) singleSnapshot.child("comment").getValue();
                    // TODO: get first image for task from db
                    imageIdDB = R.drawable.placeholder_case;
                    data.add(new UsersOpenTasksListItem(titleDB, commentDB, imageIdDB));

                }

                fragmentWait.changeToContentView(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public List<UsersOpenTasksListItem> getData(){
        return data;
    }

}
