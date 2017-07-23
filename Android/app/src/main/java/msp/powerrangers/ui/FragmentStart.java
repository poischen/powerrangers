package msp.powerrangers.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;

import msp.powerrangers.R;
import msp.powerrangers.logic.Global;
import msp.powerrangers.logic.User;
import msp.powerrangers.ui.listitems.UsersOpenTasksListItem;

import static android.app.Activity.RESULT_OK;


/**
 * Start screen, where a firebaseUser can see information about his account and navigate between Tasks & Fälle
 */
public class FragmentStart extends Fragment implements View.OnClickListener {

    private static final int CHOOSE_IMAGE_REQUEST = 123;
    private static final int STORAGE_PERMISSION_REQUEST = 234;
    private static final String TAG = "StartFragment";

    private ProgressBar progressBar;
    private ScrollView scrollView;

    CircleImageView userImage;
    TextView tvUserName;
    Button donateButton;
    Button reportACaseButton;
    Bitmap userPicBmp;

    // bubbles to update
    TextView balance;
    TextView nOpenTasks;
    LinearLayout usersOpenTasks;
    TextView nReportedCases;
    TextView nConfirmedCases;
    TextView nCompletedTasks;
    TextView nDonation;
    FirebaseUser firebaseUser;
    private User u;
    private StorageReference storageRef;
    private boolean isInit = false;

    public FragmentStart() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.fStart_progressBar);
        scrollView = (ScrollView) view.findViewById(R.id.fStart_ScrollView);
        scrollView.setVisibility(View.GONE);

        //find View elements
        // display user name
        tvUserName = (TextView) view.findViewById(R.id.textViewUserName);
        tvUserName.setText(u.getName());

        //interactive elements
        userImage = (CircleImageView) view.findViewById(R.id.userimage);
        nOpenTasks = (TextView) view.findViewById(R.id.numberOpenTasks);
        usersOpenTasks = (LinearLayout) view.findViewById(R.id.usersOpenTasks);
        userImage.setOnClickListener(this);
        usersOpenTasks.setOnClickListener(this);

        // bubbles to update
        balance = (TextView) view.findViewById(R.id.numberBalance);
        nReportedCases = (TextView) view.findViewById(R.id.numberReportedCases);
        nConfirmedCases = (TextView) view.findViewById(R.id.numberConfirmedCases);
        nCompletedTasks = (TextView) view.findViewById(R.id.numberCompletedTasks);
        nDonation = (TextView) view.findViewById(R.id.numberDonatedPower);

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
        u = ((FragmentTabs) getActivity()).getUser();
        downloadUserPic();
        setUserInfos();

    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        progressBar.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (userPicBmp != null) {
            userImage.setImageBitmap(userPicBmp);
        }

        // get the bubble values and the user name
        Bundle extras = new Bundle();
        Intent intent = getActivity().getIntent();
        extras = intent.getExtras();

        if (extras != null) {
            balance.setText(extras.getString("balance"));
            nOpenTasks.setText(extras.getString("nOpTasks"));
            nReportedCases.setText(extras.getString("nRepCases"));
            nConfirmedCases.setText(extras.getString("nConfCases"));
            nCompletedTasks.setText(extras.getString("nCompCases"));
            nDonation.setText(extras.getString("nDonPower"));
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // store current values (they will be set in onResume after switching tabs)
        Bundle extras = new Bundle();
        extras.putString("name", tvUserName.getText().toString());
        extras.putString("balance", balance.getText().toString());
        extras.putString("nOpTasks", nOpenTasks.getText().toString());
        extras.putString("nRepCases", nReportedCases.getText().toString());
        extras.putString("nConfCases", nConfirmedCases.getText().toString());
        extras.putString("nCompCases", nCompletedTasks.getText().toString());
        extras.putString("nDonPower", nDonation.getText().toString());

        getActivity().getIntent().putExtras(extras);
    }

    @Override
    public void onClick(View view) {
        android.support.v4.app.FragmentManager fragmentManager;
        android.support.v4.app.FragmentTransaction fragmentTransaction;
        switch (view.getId()) {
            case R.id.userimage:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);

                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
                    }
                } else {
                    showFileChooser();
                }

                break;
            case R.id.usersOpenTasks:
                FragmentWait fw = new FragmentWait();
                UsersOpenTasksListItem uotList = new UsersOpenTasksListItem();
                fw.initReplacingFragmentOpenTasks(uotList, u.getDbId());
                ((BaseContainerFragment) getParentFragment()).replaceFragmentWait(fw);
                break;

            case R.id.reportACaseButton:
                if (u != null) {
                    Intent intentReportCase = new Intent(getActivity(), ActivityReportCase.class);
                    intentReportCase.putExtra("USER", u);
                    startActivity(intentReportCase);
                } else {
                    FirebaseAuth.getInstance().signOut();
                }
                break;

            case R.id.donateButton:
                Intent intentDonate = new Intent(getActivity(), ActivityDonate.class);
                intentDonate.putExtra("USER", u);
                startActivity(intentDonate);
        }
    }

    /**
     * Set bubble values in fragment start screen and the user name
     *
     * @param
     */
    public void setUserInfos() {
        //set listener
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference refPath = db.child("users").child(u.getDbId());
        refPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User userInfo = dataSnapshot.getValue(User.class);
                    // get & set values for bubbles
                    balance.setText(String.valueOf(dataSnapshot.child("balance").getValue()));
                    nOpenTasks.setText(String.valueOf(dataSnapshot.child("numberOpenTasks").getValue()));
                    nConfirmedCases.setText(String.valueOf(dataSnapshot.child("numberConfirmedCases").getValue()));
                    nReportedCases.setText(String.valueOf(dataSnapshot.child("numberReportedCases").getValue()));
                    nCompletedTasks.setText(String.valueOf(dataSnapshot.child("numberCompletedTasks").getValue()));
                    nDonation.setText(String.valueOf(dataSnapshot.child("donatedValue").getValue()));

                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
            uploadFile(filePath);
        }
    }

    /**
     * method to upload a profile picture via firebase if the firebaseUser is logged in
     *
     * @param filePath Path of image on device storage
     */
    private void uploadFile(Uri filePath) {
        //   FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {

            //give firebaseUser uploading feedback by a progress dialog
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getString(R.string.uploadPicture));
            progressDialog.show();

            //get filename to write url into database later
            String pictureName = getFileName(filePath);

            //create Path in Storage
            final String storageAndDBPath = "images/" + u.getDbId() + "/" + pictureName;

            //upload to Firebase Storage
            StorageReference riversRef = storageRef.child(storageAndDBPath);
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //upload succesfull, update view and write url into database
                            progressDialog.dismiss();

                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");
                            String usersDbId = u.getDbId();
                            db.child(usersDbId).child("userPic").setValue(storageAndDBPath);

                            //show pic in app after upload was successful
                            downloadUserPic();
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

                        }
                    });
        } else {
            Toast.makeText(getContext(), R.string.errorSignUpFirst, Toast.LENGTH_LONG).show();
        }

    }


    public User getUser() {
        return u;
    }

    public void setUser(User u) {
        this.u = u;
    }


    /**
     * method gets current firebaseUser profile picture fromk Firebase and shows it
     */
    private void downloadUserPic() {

        if (firebaseUser != null) {

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            String userDbId = u.getDbId();
            DatabaseReference refPath = db.child("users").child(userDbId).child("userPic");

            refPath.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        final File localFile = File.createTempFile("images", "jpg");
                        try {
                            String picUrlFromDB = dataSnapshot.getValue(String.class);
                            StorageReference riversRef = storageRef.child(Global.getThumbUrl(picUrlFromDB));
                            riversRef.getFile(localFile)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            userPicBmp = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                            userImage.setImageBitmap(userPicBmp);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.d("FragmentStart", exception.getMessage());
                                }
                            });
                        } catch (Exception e) {
                            Log.d("FragmentStart", "no image available or some other error occured");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } else {
            userImage.setImageBitmap(userPicBmp);
        }
    }


    /*
    Callback method after requesting storage permission. Try to upload user pic, when user gave permission or give feedback, if he did not.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(this.getClass().getName(), "show file chooser after giving permission");
            showFileChooser();
        } else {
            Toast.makeText(getContext(), R.string.frStart_permissionNeeded, Toast.LENGTH_SHORT).show();
        }
    }

    /*
    Method to get the picturename of the file from the filechooser
     */
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}