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
 * Data for OpenTasks in RecyclerView
 */
public class UsersOpenTasksListItem implements Serializable {

    public String title;
    public String location;
    public String comment;
    public int imageID;     // for holder (if exception)

    public String taskImageUrlDB;
    public String caseImageUrlDB;

    String titleDB;
    String cityDB;
    String countryDB;
    String commentDB;
    String locationDB;
    String taskImageDB;

    Boolean taskCompletedDB;
    Boolean taskVotedDb;
    String taskIdDB;
    String caseIdDB;

    List<UsersOpenTasksListItem> data = new ArrayList<>();

    public UsersOpenTasksListItem(String title, String location, String comment,
                                  String taskIdDB, String caseIdDB, String taskImageUrlDB, Boolean taskCompletedDB) {
        this.title = title;
        this.location = location;
        this.comment = comment;
        this.taskIdDB = taskIdDB;
        this.caseIdDB = caseIdDB;
        this.taskCompletedDB = taskCompletedDB;
        this.taskImageUrlDB = taskImageUrlDB;
    }

    public UsersOpenTasksListItem() {

    }

    public void fill_with_data(final FragmentWait fragmentWait, String userDbId) {
        DatabaseReference dbRefTasks;

        // get the reference to the db tasks
        dbRefTasks = FirebaseDatabase.getInstance().getReference("tasks");

        dbRefTasks.orderByChild("rangerDbId").equalTo(userDbId)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                data = new ArrayList<UsersOpenTasksListItem>();

                                // get data for each task from the db
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                                    taskCompletedDB = (Boolean) singleSnapshot.child("taskCompleted").getValue();
                                    taskVotedDb = (Boolean) singleSnapshot.child("taskVoted").getValue();

                                    if (!taskCompletedDB || !taskVotedDb) {

                                        titleDB = (String) singleSnapshot.child("name").getValue();
                                        cityDB = (String) singleSnapshot.child("city").getValue();
                                        countryDB = (String) singleSnapshot.child("country").getValue();
                                        locationDB = cityDB + ", " + countryDB;
                                        commentDB = (String) singleSnapshot.child("comment").getValue();
                                        taskImageDB = (String) singleSnapshot.child("taskPicture").getValue();
                                        taskIdDB = (String) singleSnapshot.child("taskDbId").getValue();
                                        caseIdDB = (String) singleSnapshot.child("caseId").getValue();

                                        data.add(new UsersOpenTasksListItem(titleDB, locationDB, commentDB, taskIdDB, caseIdDB, taskImageDB, taskCompletedDB));
                                    }

                                    fragmentWait.changeToContentUOT();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
    }

    public List<UsersOpenTasksListItem> getData() {
        return data;
    }

    public String getTitle() {
        return titleDB;
    }

    public String getDescription() {
        return commentDB;
    }

    public boolean getTaskCompleted() {
        return taskCompletedDB;
    }

    public String getTaskid() {
        return taskIdDB;
    }

    public String getCaseId() {
        return caseIdDB;
    }

    public String getTaskUrl() {
        return taskImageUrlDB;
    }

}
