package msp.powerrangers.ui.listitems;

import android.graphics.Bitmap;
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
    public int reward;
    public String imageBeforeURL;
    public String imageAfterURL;
    public String taskId;


    List<VotingTasksListItem> data = new ArrayList<>();
    private DatabaseReference dbRefTasks;

    String titleDB;
    String cityDB;
    String countryDB;
    String locationDB;
    String imageBeforeDB;
    String imageAfterDB;
    String taskIdDB;
    int rewardDB;

    Boolean taskVoted;

    public VotingTasksListItem(String taskId, String title, String location, String imageBeforeURL, String imageAfterURL, int reward) {
        this.taskId = taskId;
        this.title = title;
        this.location = location;
        this.imageBeforeURL = imageBeforeURL;
        this.imageAfterURL = imageAfterURL;
        this.reward = reward;
    }

    public VotingTasksListItem() {

    }

    /**
     * Generates Voting Task Objects for RecyclerView's adapter.
     */
    public void fill_with_data(final FragmentWait fragmentWait) {
        DatabaseReference dbRefTasks;
        // get the reference to the db tasks
        dbRefTasks = FirebaseDatabase.getInstance().getReference("tasks");
        dbRefTasks.orderByChild("taskCompleted").equalTo(true)
                .addValueEventListener(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                // get data for each case from the db
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                                    taskVoted = (Boolean) singleSnapshot.child("taskVoted").getValue();

                                    if (!taskVoted) {

                                        taskIdDB = (String) singleSnapshot.child("taskDbId").getValue();
                                        titleDB = (String) singleSnapshot.child("name").getValue();
                                        cityDB = (String) singleSnapshot.child("city").getValue();
                                        countryDB = (String) singleSnapshot.child("country").getValue();
                                        rewardDB = (int)(long) singleSnapshot.child("reward").getValue();

                                        imageBeforeDB = (String) singleSnapshot.child("taskPicture").getValue();
                                        imageAfterDB = (String) singleSnapshot.child("taskPictureAfter").getValue();

                                        locationDB = cityDB + ", " + countryDB;

                                        data.add(new VotingTasksListItem(taskIdDB, titleDB, locationDB, imageBeforeDB, imageAfterDB, rewardDB));
                                        Log.i("KATJA", "This is the data for voting: " + data);
                                    }
                                }

                                // TODO: checken, ob wirklich true sein soll (es gibt einen besonderen tag bei voting... kP)
                                fragmentWait.changeToContentView(true);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

    }


    public String getImageBeforeURL() {
        return imageBeforeURL;
    }

    public String getImageAfterURL() {
        return imageAfterURL;
    }

    public List<VotingTasksListItem> getData() {
        return data;
    }

}
