package msp.powerrangers.ui.listitems;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import msp.powerrangers.R;
import msp.powerrangers.ui.FragmentWait;

import static android.content.ContentValues.TAG;

/**
 * == Data
 * Tutorial: https://www.sitepoint.com/mastering-complex-lists-with-the-android-recyclerview/
 */
public class VotingTasksListItem implements Serializable {

    public String title;
    public String location;
    public int imageID1;
    public int imageID2;
    public String imageBeforeURL;
    public String imageAfterURL;
    public String nLikes;
    public String nDislikes;
    public String taskId;

    String titleDB;
    String cityDB;
    String countryDB;
    String locationDB;
    String imageBeforeDB;
    String imageAfterDB;
    String nLikesDB;
    String nDislikesDB;
    String taskIdDB;

    Boolean taskVoted;

    List<VotingTasksListItem> data = new ArrayList<>();
    private DatabaseReference dbRefTasks;

    public VotingTasksListItem(String taskId, String title, String location, String imageBeforeURL, String imageAfterURL, String nLikes, String nDislikes) {
        this.taskId = taskId;
        this.title = title;
        this.location = location;
        this.imageBeforeURL = imageBeforeURL;
        this.imageAfterURL = imageAfterURL;
        this.nLikes = nLikes;
        this.nDislikes = nDislikes;
    }

    public VotingTasksListItem() {

    }

    /**
     * Generates Voting Task Objects for RecyclerView's adapter.
     */
    public void fill_with_data(final FragmentWait fragmentWait) {

        // get the reference to the db tasks
        dbRefTasks = FirebaseDatabase.getInstance().getReference("tasks");
        Log.i("KATJA", "VotingTasksListItem before OnDataChange");

        dbRefTasks.orderByChild("taskCompleted").equalTo(true)
                .addValueEventListener(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // get data for each case from the db

                                Log.i("KATJA", "onDataChange drin");
                                Log.i("KATJA", "onDataChange dataSnapshot" + dataSnapshot);

                                data = new ArrayList<>();
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    // Get the task data from snapshot
                                    Log.i("KATJA", "onDataChange for loop");

                                    taskVoted = (Boolean) singleSnapshot.child("taskVoted").getValue();

                                    if (!taskVoted) {

                                        taskIdDB = (String) singleSnapshot.child("taskDbId").getValue();

                                        titleDB = (String) singleSnapshot.child("city").getValue();
                                        Log.i("KATJA", "title: " + titleDB);
                                        cityDB = (String) singleSnapshot.child("city").getValue();
                                        Log.i("KATJA", "city: " + cityDB);
                                        countryDB = (String) singleSnapshot.child("country").getValue();
                                        Log.i("KATJA", "country: " + countryDB);

                                        imageBeforeDB = (String) singleSnapshot.child("taskPicture").getValue();
                                        Log.i("KATJA", "before: " + imageBeforeDB);
                                        imageAfterDB = (String) singleSnapshot.child("taskPictureAfter").getValue();
                                        Log.i("KATJA", "after: " + imageAfterDB);

                                        nLikesDB = String.valueOf(singleSnapshot.child("numberUpvotes").getValue());
                                        Log.i("KATJA", "up: " + nLikesDB);
                                        nDislikesDB = String.valueOf(singleSnapshot.child("numberDownvotes").getValue());
                                        Log.i("KATJA", "down: " + nDislikesDB);

                                        //TODO: add after image as ranger and get here
                                        locationDB = cityDB + ", " + countryDB;

                                        data.add(new VotingTasksListItem(taskIdDB, titleDB, locationDB, imageBeforeDB, imageAfterDB, nLikesDB, nDislikesDB));
                                        Log.i("KATJA", "This is the data for voting: " + data);
                                    }
                                }

                                fragmentWait.changeToContentView(true);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

        /*
        data.add(new VotingTasksListItem("Waste on the beach", "Spain, Palma de Mallorca",  R.drawable.polluted_beach2, R.drawable.clean_beach2));
        */
        //Log.i("Data", "This is the data: " + data);

    }


    public List<VotingTasksListItem> getData() {
        return data;
    }

}
