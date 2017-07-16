package msp.powerrangers.ui.listitems;

import android.graphics.Bitmap;

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


    public Bitmap taskImage;
    public String taskImageUrlDB;


    String titleDB;
    String cityDB;
    String countryDB;
    String commentDB;
    int imageIdDB;
    boolean taskCompletedDB;
    String taskIdDB;
    String caseIdDB;

    List<UsersOpenTasksListItem> data = new ArrayList<>();
    private DatabaseReference dbRefTasks;

    public UsersOpenTasksListItem(String title, String desc, int imageID) {
        this.title = title;
        this.desc = desc;
        this.imageID = imageID;
    }

    public UsersOpenTasksListItem(){

    }

    public void setTaskBitmap(Bitmap image){
        this.taskImage = image;
    }

    public Bitmap getTaskBitmap(){
        return taskImage;
    }

    public String getTaskUrl(){
        return taskImageUrlDB;
    }

    public void fill_with_data(final FragmentWait fragmentWait, String userID) {

        // get the reference to the db tasks
        dbRefTasks = FirebaseDatabase.getInstance().getReference("tasks");

        dbRefTasks.orderByChild("rangerID").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // get data for each task from the db
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    cityDB = (String) singleSnapshot.child("city").getValue();
                    countryDB = (String) singleSnapshot.child("country").getValue();
                    titleDB = cityDB + " , " + countryDB;
                    commentDB = (String) singleSnapshot.child("comment").getValue();
                    taskCompletedDB = (boolean) singleSnapshot.child("taskCompeted").getValue();

                    // TODO: get first image for task from db
                    imageIdDB = R.drawable.placeholder_case;


                    taskIdDB = (String) singleSnapshot.child("taskId").getValue();
                    caseIdDB = (String) singleSnapshot.child("caseId").getValue();

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


    public String getTitleDB(){
        return titleDB;
    }

    public String getDescription(){
        return commentDB;
    }

    public boolean getTaskCompeted(){
        return taskCompletedDB;
    }

    public String getTaskid(){
        return taskIdDB;
    }

    public String getCaseId(){
        return caseIdDB;
    }
}
