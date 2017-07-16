package msp.powerrangers.ui.listitems;

import android.graphics.Bitmap;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import msp.powerrangers.R;
import msp.powerrangers.ui.FragmentWait;

/**
 * == Data
 * Tutorial: https://www.sitepoint.com/mastering-complex-lists-with-the-android-recyclerview/
 *
 */
public class RangerTasksListItem implements Serializable {

    public String title;
    public String city;
    public  String country;
    public String comment;
    //public int imageId;
    public String taskImageUrlDB;
    public String caseImageUrlDB;
    public Bitmap taskImage;

    List<RangerTasksListItem> data = new ArrayList<>();
    private DatabaseReference dbRefTasks;

    String titleDB;
    String cityDB;
    String countryDB;
    String commentDB;
    int imageIdDB;

    //public RangerTasksListItem(String title, String city, String country, String comment, int imageID) {
    public RangerTasksListItem(String title, String city, String country, String comment, String taskImageUrlDB, String caseImageUrlDB) {
        this.title = title;
        this.city = city;
        this.country = country;
        this.comment = comment;
        //this.imageId = imageId;
        this.taskImageUrlDB = taskImageUrlDB;
        this.caseImageUrlDB = caseImageUrlDB;
    }
    public RangerTasksListItem(){
    }

    public void setTaskBitmap(Bitmap image){
        this.taskImage = image;
    }

    public Bitmap getTaskBitmap(){
        return taskImage;
    }

    public String getCaseUrl(){
        return caseImageUrlDB;
    }

    public String getTaskUrl(){
        return taskImageUrlDB;
    }

    /**
     * Generates RT-Objects for RecyclerView's adapter.
     */
    public List<RangerTasksListItem> fill_with_data(final FragmentWait fragmentWait) {

        // get the reference to the db cases
        dbRefTasks = FirebaseDatabase.getInstance().getReference("tasks");
        dbRefTasks.orderByChild("assigned").equalTo(false).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // get data for each task from the db
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    titleDB = (String) singleSnapshot.child("name").getValue();
                    cityDB = (String) singleSnapshot.child("city").getValue();
                    countryDB = (String) singleSnapshot.child("country").getValue();
                    commentDB = (String) singleSnapshot.child("comment").getValue();
                    //imageIdDB = R.drawable.placeholder_case;
                    taskImageUrlDB = (String) singleSnapshot.child("taskPicture").getValue();
                    caseImageUrlDB = (String) singleSnapshot.child("casePicture").getValue();
                    data.add(new RangerTasksListItem(titleDB, cityDB, countryDB, commentDB, taskImageUrlDB, caseImageUrlDB));

                }

                fragmentWait.changeToContentView(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*
        data.add(new RangerTasksListItem("Kathmandu, Nepal", "Summary of the case in Kathmandu....", R.drawable.placeholder_task));
        data.add(new RangerTasksListItem("Munich, Germany", "Summary of the case in Munich....", R.drawable.placeholder_task));
        data.add(new RangerTasksListItem("Chennai, India", "Summary of the case in Chennai....", R.drawable.placeholder_task));
        data.add(new RangerTasksListItem("Pattaya, Thailand", "Summary of the case in Pattaya....", R.drawable.placeholder_task));
        data.add(new RangerTasksListItem("Hanoi, Vietnam", "Summary of the case in Hanoi....", R.drawable.placeholder_task));
        data.add(new RangerTasksListItem("Kemer, Turkey", "Summary of the case in Kemer....", R.drawable.placeholder_task));
        */

        return data;
    }

    public List<RangerTasksListItem> getData(){
        return data;
    }


}
