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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.app.Activity.RESULT_OK;


/** Start screen, where a user can see information about his account and navigate between Tasks & Fälle
 */
public class FragmentStart extends Fragment implements View.OnClickListener {
    private static final int CHOOSE_IMAGE_REQUEST = 123;
    private static final int STORAGE_PERMISSION_REQUEST = 234;

    CircleImageView userImage;
    TextView openTasks;

    private StorageReference storageRef;

    public FragmentStart() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);


        //find View elements
        userImage = (CircleImageView) getActivity().findViewById(R.id.userimage);
        openTasks = (TextView) getActivity().findViewById(R.id.numberOpenTasks);
        //TODO: userImage.setOnClickListener(this);
        //TODO: openTasks.setOnClickListener(this);

        //get and show profile pic
        showUserPic();

        //TODO: get and show user name
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Firebase stuff
        storageRef = FirebaseStorage.getInstance().getReference();

    }


    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
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
     * method to upload a profile picture via firebase if the user is logged in
     * @param filePath Path of image on device storage
     */
    private void uploadFile(Uri filePath) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = null;
            //get UID to identify user
            for (UserInfo profile : user.getProviderData()) {
                uid = profile.getUid();
            };

            //give user uploading feedback by a progress dialog
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
     * method gets current user profiel picture fromk Firebase and shows it
     */
    //TODO: store profile picture as local file, so it does not have to be downlaoded all the time, and check first, if it is available on the device: "You can also download to device memory using getBytes()"
    private void showUserPic() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        File localFile = null;
        if (user != null) {
            String uid = null;
            //get UID to identify user
            for (UserInfo profile : user.getProviderData()) {
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
