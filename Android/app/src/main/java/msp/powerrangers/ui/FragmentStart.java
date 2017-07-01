package msp.powerrangers.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import msp.powerrangers.R;
import msp.powerrangers.logic.User;

import static android.app.Activity.RESULT_OK;


/** Start screen, where a firebaseUser can see information about his account and navigate between Tasks & Fälle
 */
public class FragmentStart extends Fragment implements View.OnClickListener {

    private static final int CHOOSE_IMAGE_REQUEST = 123;
    private static final int STORAGE_PERMISSION_REQUEST = 234;


    CircleImageView userImage;
    TextView tvUserName;
    TextView openTasks;
    Button donateButton;
    Button reportACaseButton;

    FirebaseUser firebaseUser;
    private User u;

    private StorageReference storageRef;

    public FragmentStart() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        // display user name
        tvUserName = (TextView) view.findViewById(R.id.textViewUserName);
        tvUserName.setText(firebaseUser.getDisplayName());

        //find View elements
        userImage = (CircleImageView) getActivity().findViewById(R.id.userimage);
        openTasks = (TextView) view.findViewById(R.id.numberOpenTasks);
        //TODO: userImage.setOnClickListener(this);

        openTasks.setOnClickListener(this);

        // call to action buttons
        donateButton = (Button) view.findViewById(R.id.donateButton);
        reportACaseButton = (Button) view.findViewById(R.id.reportACaseButton);

        donateButton.setOnClickListener(this);
        reportACaseButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Firebase stuff
        storageRef = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        //get current User Object
        Bundle bundle = getArguments();
        //Log.i("BUNDLE START" , bundle.toString());
        u = (User) bundle.getSerializable("USER");
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();

        //get and show profile pic
        showUserPic();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userimage:

                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, STORAGE_PERMISSION_REQUEST);}
                else {
                    showFileChooser();
                }

                /* (storagePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
                } else {
                    showFileChooser();
                }*/
                break;
            case R.id.numberOpenTasks:
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragmentUsersOpenTasks fuot = new FragmentUsersOpenTasks();
                fragmentTransaction.replace(R.id.activity_main_fragment_container, fuot);
                fragmentTransaction.commit();
                break;

            case R.id.reportACaseButton:

                //Log.i("User USERNAME IN START" , u.getId());
                Intent intentReportCase = new Intent(getActivity(), ActivityReportCase.class);
                intentReportCase.putExtra("USER", u);
                Log.i("I AM AFTER PUTEXTRA" , "IN REPORT A CASE");

                //Bundle bundle = new Bundle();
                //bundle.putSerializable(String.valueOf(R.string.intent_current_user), u);
                //Toast.makeText(getContext(), u.toString(), Toast.LENGTH_LONG).show();
                //intentReportCase.putExtras(bundle);
                startActivity(intentReportCase);
                break;
        }
    }

/**
 * method to show file chooser for images
 */
    private void showFileChooser() {
        Intent getimageintent = new Intent();
        getimageintent.setType("image/*");
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
            Uri filePath = data.getData();
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
            uploadFile(filePath);
        }
    }

    /**
     * method to upload a profile picture via firebase if the firebaseUser is logged in
     * @param filePath Path of image on device storage
     */
    private void uploadFile(Uri filePath) {
     //   FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String uid = null;
            //get UID to identify firebaseUser
            for (UserInfo profile : firebaseUser.getProviderData()) {
                uid = profile.getUid();
            };

            //give firebaseUser uploading feedback by a progress dialog
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getString(R.string.uploadPicture));
            progressDialog.show();

            StorageReference riversRef = storageRef.child("images/" + uid + "/profilepic.jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //upload succesfull, update view
                            progressDialog.dismiss();
                            showUserPic();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //upload not successfull
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), R.string.errorUpload, Toast.LENGTH_LONG).show();
                            Log.d("FragmentStart", exception.getMessage());
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage(((int) progress) + getString(R.string.uploaded));
                        }
                    });
        } else {
            Toast.makeText(getContext(), R.string.errorSignUpFirst, Toast.LENGTH_LONG).show();
        }


    }

    /**
     * method gets current firebaseUser profiel picture fromk Firebase and shows it
     */
    //TODO: store profile picture as local file, so it does not have to be downlaoded all the time, and check first, if it is available on the device: "You can also download to device memory using getBytes()"
    private void showUserPic() {
     //   FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        File localFile = null;
        if (firebaseUser != null) {
            String uid = null;
            //get UID to identify firebaseUser
            for (UserInfo profile : firebaseUser.getProviderData()) {
                uid = profile.getUid();
            };
            try {
                localFile = File.createTempFile("images", "jpg");
                final Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                StorageReference riversRef = storageRef.child("images/" + uid + "/profilepic.jpg");
                riversRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                userImage.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("FragmentStart", exception.getMessage());
                    }
                });

            } catch (IOException e) {
                Log.d("FragmentStart", e.getMessage());
            }

        }


    }

}
