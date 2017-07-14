package msp.powerrangers.ui;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.Iterator;

import msp.powerrangers.R;
import msp.powerrangers.logic.Ranger;
import msp.powerrangers.logic.User;

public class FragmentDetailRangerTask extends Fragment {

    private TextView rangerTaskName;
    private ImageView rangerTaskDetailImage;
    private TextView textRangerReward;
    private TextView textNumberRangers;
    private TextView textPollutionLevel;
    private ImageView iconMoney;
    private ImageView iconRanger;
    private ImageView iconPollution;
    private TextView rangerTaskDescription;
    private Button buttonJoin;
    private int position;


    // firebase storage Ref
    private StorageReference storageRef;

    // firebase db instances
    private DatabaseReference dbRefTasks;

    // task ID
    private String taskID;
    private  String taskDBId;

    SharedPreferences sharedPrefs;
    String userDbID;
    DatabaseReference refPathCurrentUser;
    String currentCount;


    public FragmentDetailRangerTask() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bund = getArguments();
        position = bund.getInt("PositionRanger");

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
        rangerTaskDetailImage = (ImageView) view.findViewById(R.id.rangerTaskDetailImage);
        // fancy icons
        iconMoney = (ImageView) view.findViewById(R.id.rangerReward);
        iconMoney.setImageResource(R.drawable.iconrewardsmall);
        iconRanger = (ImageView) view.findViewById(R.id.imageNumberRangers);
        iconRanger.setImageResource(R.drawable.iconranger);
        iconPollution = (ImageView) view.findViewById(R.id.imagePollutionLevel);



        // fill in information from task
        dbRefTasks = FirebaseDatabase.getInstance().getReference("tasks");

        dbRefTasks.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator iter = dataSnapshot.getChildren().iterator();

                for(int i = 0; i < position; i++) {

                    iter.next();
                }

                DataSnapshot singleSnapshot = (DataSnapshot) iter.next();

                // Fetch the data from the DB

                String city = (String) singleSnapshot.child("city").getValue();
                String country = (String) singleSnapshot.child("country").getValue();
                // TODO set image
                String reward = String.valueOf(singleSnapshot.child("reward").getValue());
                String scale = String.valueOf(singleSnapshot.child("scale").getValue());
                String taskInfo = (String) singleSnapshot.child("comment").getValue();

                // get the task id
                taskID =  (String) singleSnapshot.child("taskId").getValue();
                taskDBId = (String) singleSnapshot.child("taskDbId").getValue();
                rangerTaskName.setText( city + " , " + country);
                textRangerReward.setText(reward);
                String numberRangers = (String) singleSnapshot.child("numberRangers").getValue();
                textNumberRangers.setText(numberRangers);

                textPollutionLevel.setText(convertScaleToText(scale));
                rangerTaskDescription.setText(taskInfo + "\nWe need you! ;-)");


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



        // TODO: get & set image for this task from the db

        int imageId = R.drawable.polluted_beach1;
        rangerTaskDetailImage.setImageResource(imageId);



        // set some fancy icons

        buttonJoin = (Button) view.findViewById(R.id.buttonJoinAsRanger);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Ranger erstellen
                // TODO: task zuweisen und in die db eintragen (users, tasks)
                // TODO: Anzeige in FragmentStart andern (nOpenTasks +1 )

                Toast.makeText(v.getContext(), "You have joined the task :-)!", Toast.LENGTH_LONG).show();
                sharedPrefs = getContext().getSharedPreferences(getResources().getString(R.string.sharedPrefs_userDbIdPrefname), 0);
                userDbID = sharedPrefs.getString(getResources().getString(R.string.sharedPrefs_userDbId), null);

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                refPathCurrentUser = db.child("users").child(userDbID);

                refPathCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User userInfo = dataSnapshot.getValue(User.class);
                        currentCount = String.valueOf(dataSnapshot.child("numberOpenTasks").getValue());

                        // Create a new Ranger and fill additional information in the DB Tasks
                        Ranger ranger = new Ranger(userInfo, taskID);
                        dbRefTasks.child(taskDBId).child("rangerID").setValue(ranger.getId());
                        dbRefTasks.child(taskDBId).child("assigned").setValue(true);

                        // update the number of rangers open tasks
                        int newCount = Integer.valueOf(currentCount) + 1;
                        refPathCurrentUser.child("numberOpenTasks").setValue(String.valueOf(newCount));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



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


}