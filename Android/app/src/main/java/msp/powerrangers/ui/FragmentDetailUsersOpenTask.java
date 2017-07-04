package msp.powerrangers.ui;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    public FragmentDetailUsersOpenTask() {
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
        View view = inflater.inflate(R.layout.fr_detail_users_open_task, container, false);

        // TODO: set the task name from db
        tvTaskName = (TextView) view.findViewById(R.id.taskNameUOT);
        tvTaskName.setText("Karaganda");

        // TODO: set the image for the task from db (cases)
        ivTaskImage = (ImageView) view.findViewById(R.id.taskImageUOT);
        ivTaskImage.setImageResource(R.drawable.polluted_beach1);

        // TODO: set the description from the db (cases)
        tvTaskDesc = (TextView) view.findViewById(R.id.taskDescUOT);
        tvTaskDesc.setText("The summary of the task...");

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


<<<<<<< HEAD
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        public void onFragmentInteraction(String title);
=======
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
>>>>>>> ui
    }


}