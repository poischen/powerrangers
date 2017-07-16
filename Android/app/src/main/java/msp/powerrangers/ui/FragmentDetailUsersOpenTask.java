package msp.powerrangers.ui;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import msp.powerrangers.R;
/**
 * Screen to show details of a user's open task and complete it
 */
public class FragmentDetailUsersOpenTask extends Fragment {

    private TextView tvTaskName;
    private ImageView ivTaskImage;
    private TextView tvTaskDesc;
    private TextView tvActionToUpload;
    private Button buttonUploadImages;
    private ImageView ivUploadedImage;
    private Button buttonCompleteTask;

    private int position;
    private String taskTitle;
    private String taskDescription;
    private Bitmap taskImage;
    private boolean isTaskAlreadyCompleted;
    private String openTaskID;
    private String openTaskCaseID;

    public FragmentDetailUsersOpenTask() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        //TODO: get right position tag from FragmentUsersOpenTasks
        //TODO: get right task title tag from FragmentUsersOpenTasks
        //TODO: get right image tag from FragmentUsersOpenTasks
        //TODO: get right description tag from FragmentUsersOpenTasks
        //TODO: get status from FragmentUsersOpenTasks
        position = bundle.getInt("PositionUsersOpenTask");
        taskTitle = bundle.getString("TitleUsersOpenTask");
        taskDescription = bundle.getString("DescriptionUsersOpenTask");
        taskImage = BitmapFactory.decodeByteArray(
                bundle.getByteArray("ImageUsersOpenTask"),0,bundle.getByteArray("ImageUsersOpenTask").length);
        isTaskAlreadyCompleted = bundle.getBoolean("StatusUsersOpenTask");
        openTaskCaseID = bundle.getString("OpenTaskCaseID");
        openTaskID = bundle.getString("OpenTaskID");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fr_detail_users_open_task, container, false);

        tvTaskName = (TextView) view.findViewById(R.id.taskNameUOT);
        tvTaskName.setText(taskTitle);

        ivTaskImage = (ImageView) view.findViewById(R.id.taskImageUOT);
        if (taskImage!=null){
            ivTaskImage.setImageBitmap(taskImage);
        } else {
            ivTaskImage.setImageResource(R.drawable.placeholder_task);
        }

        tvTaskDesc = (TextView) view.findViewById(R.id.taskDescUOT);
        tvTaskDesc.setText(taskDescription);

        // hint to upload an image
        tvActionToUpload = (TextView) view.findViewById(R.id.textImagesToUpload);
        ivUploadedImage = (ImageView) view.findViewById(R.id.taskUploadedImageRanger);

        // TODO: implement uploading images (like in ActivityReportCase)
        buttonUploadImages = (Button) view.findViewById(R.id.buttonUploadImageUOT);
        buttonUploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "To implement: upload images", Toast.LENGTH_SHORT).show();
                ivUploadedImage.setImageResource(R.drawable.defaultuser);
                tvActionToUpload.setVisibility(View.GONE);
                ivUploadedImage.setVisibility(View.VISIBLE);
            }
        });


        // TODO: write to db (tasks to confirm from community)
        buttonCompleteTask = (Button) view.findViewById(R.id.buttonCompleteTask);
        buttonCompleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Task completed! " +
                    "You will get your reward after task confirmation.", Toast.LENGTH_LONG).show();

           // move to Main Activity (FragmentStart)
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
            ((Activity) getActivity()).overridePendingTransition(0,0);

            }
        });

        return view;
    }


}