package msp.powerrangers.ui.listitems;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import msp.powerrangers.ui.FragmentWait;


/**
 * Data for VotingTasks in RecyclerView
 */
public class VotingTasksListItem implements Serializable {

    public String title;
    public String location;
    public String comment;
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
    String commentDB;
    String imageBeforeDB;
    String imageAfterDB;
    String taskIdDB;
    int rewardDB;

    Boolean taskVoted;

    public VotingTasksListItem(String taskId, String title, String location, String comment, String imageBeforeURL, String imageAfterURL, int reward) {
        this.taskId = taskId;
        this.title = title;
        this.location = location;
        this.comment = comment;
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
                                data = new ArrayList<VotingTasksListItem>();

                                // get data for each case from the db
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                                    taskVoted = (Boolean) singleSnapshot.child("taskVoted").getValue();

                                    if (!taskVoted) {

                                        taskIdDB = (String) singleSnapshot.child("taskDbId").getValue();
                                        titleDB = (String) singleSnapshot.child("name").getValue();
                                        cityDB = (String) singleSnapshot.child("city").getValue();
                                        countryDB = (String) singleSnapshot.child("country").getValue();
                                        commentDB = (String) singleSnapshot.child("comment").getValue();
                                        rewardDB = (int) (long) singleSnapshot.child("reward").getValue();
                                        imageBeforeDB = (String) singleSnapshot.child("taskPicture").getValue();
                                        imageAfterDB = (String) singleSnapshot.child("taskPictureAfter").getValue();
                                        locationDB = cityDB + ", " + countryDB;

                                        data.add(new VotingTasksListItem(taskIdDB, titleDB, locationDB, commentDB, imageBeforeDB, imageAfterDB, rewardDB));
                                    }
                                }
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
