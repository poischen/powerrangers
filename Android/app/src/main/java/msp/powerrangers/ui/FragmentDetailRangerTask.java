package msp.powerrangers.ui;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import msp.powerrangers.R;
import msp.powerrangers.logic.Global;
import msp.powerrangers.logic.Ranger;
import msp.powerrangers.logic.User;

public class FragmentDetailRangerTask extends Fragment {

    private TextView rangerTaskName;
    private TextView textRangerReward;
    private TextView textNumberRangers;
    private TextView textPollutionLevel;
    private ImageView iconMoney;
    private ImageView iconRanger;
    private ImageView iconPollution;
    private TextView rangerTaskDescription;
    private Button buttonJoin;
    private int position;

    // firebase storage Ref and swipe gallery
    private StorageReference storageRef;
    List<Bitmap> pictureBitmapList;
    String caseImageUrl;
    String taskImageUrl;
    ViewPager viewPager;

    // firebase db instances
    private DatabaseReference dbRefTasks;

    // task ID
    private String taskID;
    private  String taskDBId;
    Boolean isAssigned;

    SharedPreferences sharedPrefs;
    String userDbID;
    DatabaseReference refPathCurrentUser;


    public FragmentDetailRangerTask() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageRef = FirebaseStorage.getInstance().getReference();

        Bundle bund = getArguments();
        position = bund.getInt("PositionRanger");

        caseImageUrl = bund.getString("caseImageUrl");
        taskImageUrl = bund.getString("taskImageUrl");
        pictureBitmapList = new ArrayList<>();

        Log.i("KATJA", "FrDetailRT onCreate");

        // Set action bar menu
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fr_detail_ranger_task, container, false);

        // Elements
        rangerTaskName = (TextView) view.findViewById(R.id.taskDetailName);
        textRangerReward = (TextView) view.findViewById(R.id.textRangerReward);
        textNumberRangers = (TextView) view.findViewById(R.id.textNumberRangers);
        textPollutionLevel = (TextView) view.findViewById(R.id.textScalePollution);
        rangerTaskDescription = (TextView) view.findViewById(R.id.detailTaskDescription);

        // fancy icons
        iconMoney = (ImageView) view.findViewById(R.id.rangerReward);
        iconMoney.setImageResource(R.drawable.iconrewardsmall);
        iconRanger = (ImageView) view.findViewById(R.id.imageNumberRangers);
        iconRanger.setImageResource(R.drawable.iconranger);
        iconPollution = (ImageView) view.findViewById(R.id.imagePollutionLevel);

        viewPager = (ViewPager) view.findViewById(R.id.fRangerTaskDetailViewPager);
        FragmentDetailRangerTask.ImageAdapter adapter = new ImageAdapter(this.getContext());
        viewPager.setAdapter(adapter);

        // download the task image (url from bundle)
        if (taskImageUrl != null) {
            try {
                final File localFileTask = File.createTempFile("images", "jpg");
                StorageReference riversRefTask = storageRef.child(Global.getDisplayUrl(taskImageUrl));
                riversRefTask.getFile(localFileTask)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.i("KATJA FrDetailRT", "task image download erfolgreich");
                                Bitmap taskBitmap = BitmapFactory.decodeFile(localFileTask.getAbsolutePath());
                                pictureBitmapList.add(taskBitmap);
                                updateImageViews();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.i("KATJA FrDetailRT", exception.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.i("KATJA FrDetailRT", "no image available or some other error occured");
            }
        }

        // download the case image (url from bundle)
        if (caseImageUrl != null) {
            try {
                final File localFileTask = File.createTempFile("images", "jpg");
                StorageReference riversRefTask = storageRef.child(Global.getDisplayUrl(caseImageUrl));
                riversRefTask.getFile(localFileTask)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.i("KATJA FrDetailRT", "case image download erfolgreich");
                                Bitmap caseBitmap = BitmapFactory.decodeFile(localFileTask.getAbsolutePath());
                                pictureBitmapList.add(caseBitmap);
                                updateImageViews();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.i("KATJA FrDetailRT", exception.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.i("KATJA FrDetailRT", "no image available or some other error occured");
            }
        }

        // fill in information from task
        dbRefTasks = FirebaseDatabase.getInstance().getReference("tasks");
        Query filteredTasks = dbRefTasks.orderByChild("assigned").equalTo(false);

        filteredTasks.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("KATJA", "filtered tasks, onDataChange");
                Iterator iter = dataSnapshot.getChildren().iterator();
                for(int i = 0; i < position; i++) {
                    iter.next();
                }

                DataSnapshot singleSnapshot = (DataSnapshot) iter.next();

                // Fetch the data from the DB
                String city = (String) singleSnapshot.child("city").getValue();
                String country = (String) singleSnapshot.child("country").getValue();
                String reward = String.valueOf(singleSnapshot.child("reward").getValue());
                String scale = String.valueOf(singleSnapshot.child("scale").getValue());
                String comment = (String) singleSnapshot.child("comment").getValue();

                taskID =  (String) singleSnapshot.child("taskId").getValue();
                taskDBId = (String) singleSnapshot.child("taskDbId").getValue();
                isAssigned = (Boolean) singleSnapshot.child("assigned").getValue();

                Log.i("KATJA", "taskID: "+taskID);
                Log.i("KATJA", "taskDBID: "+taskDBId);
                Log.i("KATJA", "isAssigned: "+isAssigned.toString());

                rangerTaskName.setText( city + " , " + country);
                textRangerReward.setText(reward);

                String numberRangers = String.valueOf(singleSnapshot.child("numberRangers").getValue());
                Log.i("KATJA", "#rangers: "+numberRangers);

                textNumberRangers.setText(numberRangers);

                textPollutionLevel.setText(convertScaleToText(scale));
                comment = comment + getString(R.string.detailRangerTaskComment);
                rangerTaskDescription.setText(comment);

                // set appropriate icon
                switch(scale){

                    case "1":
                        iconPollution.setImageResource(R.drawable.icon_pollution_low);
                        break;

                    case "2":
                        iconPollution.setImageResource(R.drawable.icon_pollution_medium);
                        break;

                    case "3":
                        iconPollution.setImageResource(R.drawable.icon_pollution_high);
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // set some fancy icons
        buttonJoin = (Button) view.findViewById(R.id.buttonJoinAsRanger);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("KATJA", "button Join as Ranger is clicked ");

                sharedPrefs = getContext().getSharedPreferences(getResources().getString(R.string.sharedPrefs_userDbIdPrefname), 0);
                userDbID = sharedPrefs.getString(getResources().getString(R.string.sharedPrefs_userDbId), null);

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                refPathCurrentUser = db.child("users").child(userDbID);

                refPathCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.i("KATJA", "path current user, onDataChange");
                        User userInfo = dataSnapshot.getValue(User.class);

                        // Create a new Ranger and fill additional information in the DB Tasks
                        Ranger ranger = new Ranger(userInfo, taskID);
                        dbRefTasks.child(taskDBId).child("rangerDbId").setValue(ranger.getDbId());
                        Log.i("KATJA", "rangerDbId is set: "+ranger.getDbId());
                        dbRefTasks.child(taskDBId).child("assigned").getRef().setValue(true);

                        // update the number of user open tasks
                        String currentNOT = (String) dataSnapshot.child("numberOpenTasks").getValue();
                        int newCount = Integer.parseInt(currentNOT) + 1;
                        refPathCurrentUser.child("numberOpenTasks").setValue(String.valueOf(newCount));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Toast.makeText(v.getContext(), "You have joined the task :-)!", Toast.LENGTH_LONG).show();

                // move to Main Activity (FragmentStart)
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0,0);

            }
        });

        return view;
    }


    public String convertScaleToText(String scale){

        String result = "";

        switch(scale){
            case ("1"):
                result = "Low";
                break;
            case("2"):
                result = "Middle";
                break;
            case("3"):
                result = "High";
                break;
        }

        return result;
    }

    public void updateImageViews(){
        viewPager.getAdapter().notifyDataSetChanged();
    }

    public class ImageAdapter extends PagerAdapter {
        Context context;

        ImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getItemPosition(Object object) {
            if (pictureBitmapList.contains((View) object)){
                return pictureBitmapList.indexOf((View) object);
            } else {
                return POSITION_NONE;
            }
        }

        @Override
        public int getCount() {
            return pictureBitmapList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            final Bitmap bmp = pictureBitmapList.get(position);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(bmp);

            imageView.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Dialog builder = new Dialog(getContext());
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                        }
                    });

                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageBitmap(bmp);
                    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    builder.show();
                }
            });

            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                return true;
            default:
                return false;
        }
    }
}