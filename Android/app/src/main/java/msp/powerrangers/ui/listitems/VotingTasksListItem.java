package msp.powerrangers.ui.listitems;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import msp.powerrangers.R;

import static android.content.ContentValues.TAG;

/**
 * == Data
 * Tutorial: https://www.sitepoint.com/mastering-complex-lists-with-the-android-recyclerview/
 *
 */
public class VotingTasksListItem {

    public String title;
    public String location;
    public int imageID1;
    public int imageID2;

    String locationDB;
    String titleDB;

    List<VotingTasksListItem> data = new ArrayList<>();
    private DatabaseReference dbRefTasks;

    public VotingTasksListItem(String title, String location, int imageID1, int imageID2) {
        this.title = title;
        this.location = location;
        this.imageID1 = imageID1;
        this.imageID2 = imageID2;
    }

    public VotingTasksListItem() {

    }

    /**
     * Generates Voting Task Objects for RecyclerView's adapter.
     */
    public List<VotingTasksListItem> fill_with_data() {

        // get the reference to the db tasks
        dbRefTasks = FirebaseDatabase.getInstance().getReference("tasks");
        dbRefTasks.orderByChild("taskCompleted").equalTo(true).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get data for each case from the db
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    // Get the task data from snapshot
                    locationDB = (String) singleSnapshot.child("city").getValue();
                    titleDB = (String) singleSnapshot.child("city").getValue();
                    data.add(new VotingTasksListItem(titleDB, locationDB, R.drawable.polluted_beach1, R.drawable.clean_beach));
                    //Log.i("Data Top", "This is the data: " + data);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        /*
        data.add(new VotingTasksListItem("Waste on the beach", "Spain, Palma de Mallorca",  R.drawable.polluted_beach2, R.drawable.clean_beach2));
        */
        //Log.i("Data", "This is the data: " + data);


        return data;

    }


    /**
     * Generates RT-Objects for RecyclerView's adapter.

    public List<VotingTasksListItem> fill_with_data() {
        // Firebase db instance
        DatabaseReference dbRefTasks;

        dbRefTasks = FirebaseDatabase.getInstance().getReference("tasks");
        String taskDbId = dbRefTasks.push().getKey();

        //Query taskQuery = dbRefTasks.child(taskDbId).orderByChild("taskCompleted").equalTo(true);
        dbRefTasks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {

                    // Get the task data from snapshot
                     String location = (String) singleSnapshot.child("city").getValue();
                     String title = (String) singleSnapshot.child("city").getValue();
                     data.add(new VotingTasksListItem(title, location, R.drawable.polluted_beach1, R.drawable.clean_beach));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        data.add(new VotingTasksListItem("Polluted Beach", "Tamaris, Marokko", R.drawable.polluted_beach1, R.drawable.clean_beach));
        data.add(new VotingTasksListItem("Waste on the beach", "Spain, Palma de Mallorca",  R.drawable.polluted_beach2, R.drawable.clean_beach2));
        return data;
    }
    */
}
