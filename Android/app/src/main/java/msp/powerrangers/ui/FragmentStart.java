package msp.powerrangers.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import msp.powerrangers.logic.User;

import static android.app.Activity.RESULT_OK;


/** Start screen, where a firebaseUser can see information about his account and navigate between Tasks & Fälle
 */
public class FragmentStart extends Fragment implements View.OnClickListener {

    private static final int CHOOSE_IMAGE_REQUEST = 123;
    private static final int STORAGE_PERMISSION_REQUEST = 234;
    private static final String TAG = "StartFragment";

    CircleImageView userImage;
    TextView tvUserName;
    Button donateButton;
    Button reportACaseButton;
    Button logoutButton;

    Bitmap userPicBmp;

    // bubbles to update
    TextView balance;
    TextView nOpenTasks;
    TextView nReportedCases;
    TextView nConfirmedCases;
    TextView nCompletedTasks;

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

        //find View elements
        //logout button
        logoutButton = (Button) view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);
        // display user name
        tvUserName = (TextView) view.findViewById(R.id.textViewUserName);

        //interactive elements
        userImage = (CircleImageView) view.findViewById(R.id.userimage);
        nOpenTasks = (TextView) view.findViewById(R.id.numberOpenTasks);
        userImage.setOnClickListener(this);
        nOpenTasks.setOnClickListener(this);

        // bubbles to update
        balance = (TextView) view.findViewById(R.id.numberBalance);
        nReportedCases = (TextView) view.findViewById(R.id.numberReportedCases);
        nConfirmedCases = (TextView) view.findViewById(R.id.numberConfirmedCases);
        nCompletedTasks = (TextView) view.findViewById(R.id.numberCompletedTasks);

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
        u = (User) bundle.getSerializable("USER");
        Log.v("USER: ", u + "");


        if (u == null){
            if (firebaseUser != null) {
                //get user infos from database via users db id and instantiate User Object
                SharedPreferences sharedPrefs = getContext().getSharedPreferences(getResources().getString(R.string.sharedPrefs_userDbIdPrefname), 0);
                final String userDbID = sharedPrefs.getString(getResources().getString(R.string.sharedPrefs_userDbId), null);
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                DatabaseReference refPath = db.child("users").child(userDbID);
                refPath.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {

                            User userInfo = dataSnapshot.getValue(User.class);
                            String name = userInfo.getName();
                            tvUserName.setText(name);
                            String dbId = userInfo.getDbId();
                            String userId = userInfo.getId();
                            String mail = userInfo.getEmail();

                            setUserInfos(dataSnapshot);
                            // create user object (user is registered)
                            u = new User(dbId,userId, name, mail);
                            // set user pic
                            downloadUserPic();

                        } catch (Exception e){
                            Log.d("FragmentStart", "An error occured, user has to be signed out");
                            FirebaseAuth.getInstance().signOut();
                            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FragmentLogin fl = new FragmentLogin();
                            fragmentTransaction.replace(R.id.activity_main_fragment_container, fl);
                            fragmentTransaction.commit();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getContext(), getResources().getString(R.string.fStart_closeAppError), Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }
                });

            } else {

                Toast.makeText(getContext(), getResources().getString(R.string.fStart_closeAppError), Toast.LENGTH_LONG).show();
                getActivity().finish();
            }

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
    }

    @Override
    public void onResume() {
        super.onResume();
        //downloadUserPic(false);
        if (userPicBmp!=null){
            userImage.setImageBitmap(userPicBmp);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        android.support.v4.app.FragmentManager fragmentManager;
        android.support.v4.app.FragmentTransaction fragmentTransaction;
        switch (view.getId()) {
            case R.id.userimage:
                if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Log.v(this.getClass().getName(), "request permission");
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_REQUEST);

                    } else
                    {
                        Log.v(this.getClass().getName(), "request permission");
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_REQUEST);
                    }
                } else {
                    Log.v(this.getClass().getName(), "show file chooser");
                    showFileChooser();
                }

                break;
            case R.id.numberOpenTasks:
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentUsersOpenTasks fuot = new FragmentUsersOpenTasks();
                fragmentTransaction.replace(R.id.activity_main_fragment_container, fuot);
                fragmentTransaction.commit();
                break;

            case R.id.reportACaseButton:
                if (u != null){
                    Log.i("User USERNAME IN START" , u.getId());
                    Intent intentReportCase = new Intent(getActivity(), ActivityReportCase.class);
                    intentReportCase.putExtra("USER", u);
                    Log.i("I AM AFTER PUTEXTRA" , "IN REPORT A CASE");
                    startActivity(intentReportCase);
                } else {
                    FirebaseAuth.getInstance().signOut();
                }
                break;

            case R.id.logoutButton:
                FirebaseAuth.getInstance().signOut();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentLogin fl = new FragmentLogin();
                fragmentTransaction.replace(R.id.activity_main_fragment_container, fl);
                fragmentTransaction.commit();
                break;
        }
    }

    /**
     * Set bubble values in fragment start screen
     * @param ds
     */
    private void setUserInfos(DataSnapshot ds) {
        // get values for bubbles
        String earned = String.valueOf(ds.child("balance").getValue());
        String nOpTasks = String.valueOf(ds.child("numberOpenTasks").getValue());
        String nConfCases = String.valueOf(ds.child("numberConfirmedCases").getValue());
        String nRepCases = String.valueOf(ds.child("numberReportedCases").getValue());
        String nComplTasks = String.valueOf(ds.child("numberCompletedTasks").getValue());

        // set values for bubbles
        balance.setText(earned);
        nOpenTasks.setText(nOpTasks);
        nConfirmedCases.setText(nConfCases);
        nReportedCases.setText(nRepCases);
        nCompletedTasks.setText(nComplTasks);
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
     * @param filePath Path of image on device storage
     */
    private void uploadFile(Uri filePath) {
     //   FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            /*String uid = null;
            //get UID to identify firebaseUser
            for (UserInfo profile : firebaseUser.getProviderData()) {
                uid = profile.getUid();
            };*/

            //give firebaseUser uploading feedback by a progress dialog
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getString(R.string.uploadPicture));
            progressDialog.show();

            //get filename to write url into database later
            String pictureName = getFileName(filePath);

            //create Path in Storage
            final String storageAndDBPath = "images/" + u.getDbId() + "/" + pictureName;
            Log.v("storageAndDBPath", storageAndDBPath);

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
                            //TODO: give feedback - progressDialog.setMessage(((int) progress) + getString(R.string.uploaded));
                        }
                    });
        } else {
            Toast.makeText(getContext(), R.string.errorSignUpFirst, Toast.LENGTH_LONG).show();
        }


    }

    //https://stackoverflow.com/questions/38017765/retrieving-child-value-firebase
    /**
     * method gets current firebaseUser profile picture fromk Firebase and shows it
     */
    //TODO: store profile picture as local file, so it does not have to be downlaoded all the time, and check first, if it is available on the device: "You can also download to device memory using getBytes()"
    private void downloadUserPic() {
        //   FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            /*String uid = null;
            //get UID to identify firebaseUser
            for (UserInfo profile : firebaseUser.getProviderData()) {
                uid = profile.getUid();
            };*/

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
                            StorageReference riversRef = storageRef.child(picUrlFromDB);
                            riversRef.getFile(localFile)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Log.v("Download", "download erfolgreich");
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
                            Log.d("Fail", "no image available or some other error occured");
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
            Log.v("No Download", "userPicBmp " + userPicBmp);

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
    https://developer.android.com/guide/topics/providers/document-provider.html
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
