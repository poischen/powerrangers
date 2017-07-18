package msp.powerrangers.ui;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import msp.powerrangers.R;
import msp.powerrangers.logic.Global;

import static android.app.Activity.RESULT_OK;

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
    private Bitmap taskImageBefore;
    private String taskImageUrl;
    private boolean isTaskAlreadyCompleted;

    private String taskID;
    private String caseID;

    private Bitmap taskImageAfter;
    private Uri uriAfterImage;
    private static final int CHOOSE_IMAGE_REQUEST = 123;

    public FragmentDetailUsersOpenTask() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        position = bundle.getInt("PositionUsersOpenTask");
        taskTitle = bundle.getString("TitleUsersOpenTask");
        //TODO: key anpassen
        taskID = bundle.getString("OpenTaskID");
        caseID = bundle.getString("OpenTaskCaseID");
        taskDescription = bundle.getString("DescriptionUsersOpenTask");

        taskImageUrl = bundle.getString("taskImageUrl");

 /*       try {
            taskImageBefore = BitmapFactory.decodeByteArray(
                    bundle.getByteArray("ImageUsersOpenTask"),0,bundle.getByteArray("ImageUsersOpenTask").length);
        } catch(Exception e){
            taskImageUrl = bundle.getString("taskImageUrl");
        }*/
     //   isTaskAlreadyCompleted = bundle.getBoolean("StatusUsersOpenTask");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fr_detail_users_open_task, container, false);

        tvTaskName = (TextView) view.findViewById(R.id.taskNameUOT);
        tvTaskName.setText(taskTitle);

        ivTaskImage = (ImageView) view.findViewById(R.id.taskImageUOT);
        if (taskImageBefore !=null){
            ivTaskImage.setImageBitmap(taskImageBefore);
        } else {
            try {
                final File localFile = File.createTempFile("images", "jpg");
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference riversRef = storageRef.child(Global.getThumbUrl(taskImageUrl));
                riversRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.v("FragmentStart", "download erfolgreich");
                                Bitmap bmp = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                ivTaskImage.setImageBitmap(bmp);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("FragmentStart", exception.getMessage());
                        ivTaskImage.setImageResource(R.drawable.placeholder_task);

                    }
                });
            } catch (Exception e) {
                Log.d("FragmentStart", "no image available or some other error occured");
                ivTaskImage.setImageResource(R.drawable.placeholder_task);
            }
        }

        tvTaskDesc = (TextView) view.findViewById(R.id.taskDescUOT);
        tvTaskDesc.setText(taskDescription);

        // hint to upload an image
        tvActionToUpload = (TextView) view.findViewById(R.id.textImagesToUpload);
        ivUploadedImage = (ImageView) view.findViewById(R.id.taskUploadedImageRanger);

        buttonUploadImages = (Button) view.findViewById(R.id.buttonUploadImageUOT);
        buttonUploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        buttonCompleteTask = (Button) view.findViewById(R.id.buttonCompleteTask);
        buttonCompleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeTask();
            }
        });

        //give feedback, when task is already completed
        if (isTaskAlreadyCompleted){
            buttonUploadImages.setEnabled(false);
            buttonCompleteTask.setEnabled(false);
            tvActionToUpload.setText(getString(R.string.textNoNeedToUpload));
        }

        return view;
    }


    /**
     * method to show file chooser for images
     */
    private void showFileChooser() {
        Intent getimageintent = new Intent();
        getimageintent.setType("image/*");
        getimageintent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        getimageintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(getimageintent, getResources().getString(R.string.chooseProfilePicture)), CHOOSE_IMAGE_REQUEST);

    }

    /**
     * method to handle the image chooser activity result:
     * get the picture and upload it
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                uriAfterImage = data.getData();
                taskImageAfter = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriAfterImage);
                ivUploadedImage.setVisibility(View.VISIBLE);
                ivTaskImage.setImageBitmap(taskImageAfter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * method to upload case pictures via firebase
     */
    private void completeTask() {
        Toast.makeText(getContext(), "Please wait, while picture is uploaded.", Toast.LENGTH_LONG).show();

        //upload picture
        final String storageAndDBPath;
        //TODO: pictureName anpassen: Position?!
        storageAndDBPath = "images/cases/" + caseID + "/" + position + "_after.jpg";

        //upload to Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = storageRef.child(storageAndDBPath);
        riversRef.putFile(uriAfterImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //write picture into db and change taskstatus
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("tasks").child(taskID);
                        db.child(getString(R.string.tasks_taskCompleted)).setValue(true);
                        db.child(getString(R.string.tasks_taskPictureAfter)).setValue(storageAndDBPath);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getContext(), R.string.errorUpload, Toast.LENGTH_LONG).show();
                        Log.d("DetailUsersOpenTask", exception.getMessage());
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });

        Toast.makeText(getContext(), "Task completed! " +
                "You will get your reward after task confirmation.", Toast.LENGTH_LONG).show();

        // move to Main Activity (FragmentStart)
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0,0);
    }


}