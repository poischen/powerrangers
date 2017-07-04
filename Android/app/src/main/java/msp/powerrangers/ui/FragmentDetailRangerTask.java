package msp.powerrangers.ui;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.ImageView;
import android.widget.TextView;
=======
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
>>>>>>> ui

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

    private ImageView rangerImage;
    private TextView textRangerReward;
    private TextView textNumberRangers;
    private TextView textScalePollution;

    public FragmentDetailRangerTask() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
<<<<<<< HEAD
        View view = inflater.inflate(R.layout.fragment_fragment_detail_ranger_task, container, false);
        // Get all layout elements
        rangerImage = (ImageView) view.findViewById(R.id.rangerImage);
        textRangerReward = (TextView) view.findViewById(R.id.textRangerReward);
        textNumberRangers = (TextView) view.findViewById(R.id.textNumberRangers);
        textScalePollution = (TextView) view.findViewById(R.id.textScalePollution);

        return view;
    }
=======
        View view = inflater.inflate(R.layout.fr_detail_ranger_task, container, false);
>>>>>>> ui

        // TODO: get & set task name (location?) from db
        rangerTaskName = (TextView) view.findViewById(R.id.taskDetailName);
        rangerTaskName.setText("Kathmandu, Nepal");

        // TODO: get & set image for this task from the db
        rangerTaskDetailImage = (ImageView) view.findViewById(R.id.rangerTaskDetailImage);
        int imageId = R.drawable.polluted_beach1;
        rangerTaskDetailImage.setImageResource(imageId);

        textRangerReward = (TextView) view.findViewById(R.id.textRangerReward);
        textNumberRangers = (TextView) view.findViewById(R.id.textNumberRangers);
        textPollutionLevel = (TextView) view.findViewById(R.id.textScalePollution);

        // TODO: get & set the reward, #rangers & pollution level for this task from the db
        String rangerReward = "10";
        String nRangers = "5";
        String pollutionLevel = "high";
        textRangerReward.setText(rangerReward);
        textNumberRangers.setText(nRangers);
        textPollutionLevel.setText(pollutionLevel);

        // set some fancy icons
        // TODO: bitcoin icon :)
        iconMoney = (ImageView) view.findViewById(R.id.rangerReward);
        iconMoney.setImageResource(R.drawable.iconrewardsmall);

        iconRanger = (ImageView) view.findViewById(R.id.imageNumberRangers);
        iconRanger.setImageResource(R.drawable.iconranger);

        iconPollution = (ImageView) view.findViewById(R.id.imagePollutionLevel);
        // set appropriate icon
        if (pollutionLevel.equals("medium")) {
            iconPollution.setImageResource(R.drawable.icon_pollution_medium);
        }
        else if (pollutionLevel.equals("low")) {
            iconPollution.setImageResource(R.drawable.icon_pollution_low);
        }
        else if (pollutionLevel.equals("high")) {
            iconPollution.setImageResource(R.drawable.icon_pollution_high);
        }

        rangerTaskDescription = (TextView) view.findViewById(R.id.detailTaskDescription);
        rangerTaskDescription.setText("Summary of the case in Kathmandu....\nWe need you! ;)");

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

}