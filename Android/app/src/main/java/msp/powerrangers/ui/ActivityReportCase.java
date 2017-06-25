package msp.powerrangers.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import java.util.Map;

import msp.powerrangers.R;

import static java.security.AccessController.getContext;

public class ActivityReportCase extends AppCompatActivity {

    private static final int CHOOSE_IMAGE_REQUEST = 123;
    private static final int STORAGE_PERMISSION_REQUEST = 234;

    // text Views
    private TextView textViewCaseTitle;
    private TextView textViewCaseCity;
    private TextView textViewCaseCountry;
    private TextView textViewCaseScala;
    private TextView textViewCaseXCoordinate;
    private TextView textViewCaseYCoordinate;
    private TextView textViewCasePicture;
    private TextView textViewCaseInformation;

    // edit Texts
    private EditText editTextCaseTitle;
    private EditText editTextCaseCity;
    private EditText editTextCaseCountry;
    private EditText editTextCaseXCoordinate;
    private EditText editTextCaseYCoordinate;
    private EditText editTextCaseInformation;

    // checkboxes scale
    private CheckBox checkBoxCaseLow;
    private CheckBox checkBoxCaseMiddle;
    private CheckBox checkBoxCaseHigh;

    // buttons
    private Button buttonCaseReport;
    private ImageButton imageButtonUploadPicture;

    // firebase storage Ref
    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_case);

        //Firebase stuff
        storageRef = FirebaseStorage.getInstance().getReference();

        // find UI elements
        textViewCaseTitle = (TextView) findViewById(R.id.textViewCaseTitle);
        textViewCaseCity = (TextView) findViewById(R.id.textViewCaseCity);
        textViewCaseCountry = (TextView) findViewById(R.id.textViewCaseCountry);
        textViewCaseScala = (TextView) findViewById(R.id.textViewCaseScala);
        textViewCaseXCoordinate = (TextView) findViewById(R.id.textViewCaseXCoordinate);
        textViewCaseYCoordinate = (TextView) findViewById(R.id.textViewCaseYCoordinate);
        textViewCasePicture = (TextView) findViewById(R.id.textViewCasePicture);
        textViewCaseInformation = (TextView) findViewById(R.id.textViewCaseInformation);

        editTextCaseTitle = (EditText) findViewById(R.id.editTextCaseTitle);
        editTextCaseCity = (EditText) findViewById(R.id.editTextCaseCity);
        editTextCaseCountry = (EditText) findViewById(R.id.editTextCaseCountry);
        editTextCaseXCoordinate = (EditText) findViewById(R.id.editTextCaseXCoordinate);
        editTextCaseYCoordinate = (EditText) findViewById(R.id.editTextCaseYCoordinate);
        editTextCaseInformation = (EditText) findViewById(R.id.editTextCaseInformation);

        checkBoxCaseLow = (CheckBox) findViewById(R.id.checkBoxCaseLow);
        checkBoxCaseMiddle = (CheckBox) findViewById(R.id.checkBoxCaseMiddle);
        checkBoxCaseHigh = (CheckBox) findViewById(R.id.checkBoxCaseHigh);

        imageButtonUploadPicture = (ImageButton) findViewById(R.id.imageButtonUploadPicture);

        buttonCaseReport = (Button) findViewById(R.id.buttonCaseReport);




        // add action bar going back to parent
        // TODO: im Moment geht man zurück zur MainActivity, trotzdem zur FragmentStart, abchecken wie man das sauber macht
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        //upload an image
        imageButtonUploadPicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showFileChooser();


            }
        });


        // report a case
        buttonCaseReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if edittexts empty, if yes maketoast, if not create new case
                Toast.makeText(v.getContext(), "to implement ;-)", Toast.LENGTH_LONG).show();

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
     * method to upload case pictures via firebase
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

            StorageReference riversRef = storageRef.child("images/" + uid + "/cases/case1/casepicture.jpg");
            Toast.makeText(getApplicationContext(), R.string.uploadPicture, Toast.LENGTH_LONG).show();
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //upload succesfull, give information
                          Toast.makeText(getApplicationContext(), R.string.uploaded, Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //upload not successfull
                            Toast.makeText(getApplicationContext(), R.string.errorUpload, Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                           // progressDialog.setMessage(((int) progress) + getString(R.string.uploaded));
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), R.string.errorSignUpFirst, Toast.LENGTH_LONG).show();
        }


    }





}

