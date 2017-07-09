package msp.powerrangers.ui;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


    public FragmentDetailRangerTask() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bund = getArguments();
        position = bund.getInt("Position");

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


                rangerTaskName.setText( city + " , " + country);
                textRangerReward.setText(reward);
                // here hard coded because we decided, that 1 task 1 ranger
                textNumberRangers.setText("1");
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
                Toast.makeText(v.getContext(), "You have joined the task! \n", Toast.LENGTH_LONG).show();
                // TODO: Ranger erstellen
                // TODO: task zuweisen und in die db eintragen (users, tasks)
                // TODO: Anzeige in FragmentStart andern (openTasks +1 )

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