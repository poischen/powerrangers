package msp.powerrangers.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import msp.powerrangers.R;
import msp.powerrangers.database.Case;

public class ActivityReportCase extends AppCompatActivity
        {

    private static final int CHOOSE_IMAGE_REQUEST = 123;
    private static final int STORAGE_PERMISSION_REQUEST = 234;
    final long ONE_MEGABYTE = 1024 * 1024;

    // text Views
    private TextView textViewCaseTitle;
    private TextView textViewCaseCity;
    private TextView textViewCaseCountry;
    private TextView textViewCaseScala;
    private TextView textViewCaseAreaCoordinates;
    private TextView textViewCaseXCoordinate;
    private TextView textViewCaseYCoordinate;
    private TextView textViewCasePicture;
    private TextView textViewCaseInformation;
    private TextView textViewUploadedPictures;

    // edit Texts
    private EditText editTextCaseTitle;
    private EditText editTextCaseCity;
    private EditText editTextCaseCountry;
    private EditText editTextCaseXCoordinate;
    private EditText editTextCaseYCoordinate;
    private EditText editTextCaseInformation;

    // radio buttons scale
    private RadioButton radioButtonCaseLow;
    private RadioButton radioButtonCaseMiddle;
    private RadioButton radioButtonCaseHigh;

    // buttons
    private Button buttonCaseReport;
    private ImageButton imageButtonUploadPicture;
    private ImageView imageViewUploadedPicture;

    // firebase storage Ref
    private StorageReference storageRef;

    // firebase db instance
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("cases");




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
        textViewCaseAreaCoordinates = (TextView) findViewById(R.id.textViewCaseAreaCoordinates);
        textViewCaseXCoordinate = (TextView) findViewById(R.id.textViewCaseXCoordinate);
        textViewCaseYCoordinate = (TextView) findViewById(R.id.textViewCaseYCoordinate);
        textViewCasePicture = (TextView) findViewById(R.id.textViewCasePicture);
        textViewCaseInformation = (TextView) findViewById(R.id.textViewCaseInformation);
        textViewUploadedPictures = (TextView) findViewById(R.id.textViewUploadedPictures);

        editTextCaseTitle = (EditText) findViewById(R.id.editTextCaseTitle);
        editTextCaseCity = (EditText) findViewById(R.id.editTextCaseCity);
        editTextCaseCountry = (EditText) findViewById(R.id.editTextCaseCountry);
        editTextCaseXCoordinate = (EditText) findViewById(R.id.editTextCaseXCoordinate);
        editTextCaseYCoordinate = (EditText) findViewById(R.id.editTextCaseYCoordinate);
        editTextCaseInformation = (EditText) findViewById(R.id.editTextCaseInformation);

        radioButtonCaseLow = (RadioButton) findViewById(R.id.radioButtonCaseLow);
        radioButtonCaseMiddle = (RadioButton) findViewById(R.id.radioButtonCaseMiddle);
        radioButtonCaseHigh = (RadioButton) findViewById(R.id.radioButtonCaseHigh);

        imageButtonUploadPicture = (ImageButton) findViewById(R.id.imageButtonUploadPicture);
        imageViewUploadedPicture = (ImageView) findViewById(R.id.imageViewUploadedPicture);

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
                Toast.makeText(v.getContext(), "to be implemented ;-)", Toast.LENGTH_LONG).show();

                // get attributes from a case
                String dbId = dbRef.push().getKey();
                String caseId = UUID.randomUUID().toString();

                Case c = new Case(dbId, caseId,
                        editTextCaseTitle.getText().toString(),
                        editTextCaseCity.getText().toString(),
                        editTextCaseCountry.getText().toString(),
                        getScaleValue(radioButtonCaseLow, radioButtonCaseMiddle, radioButtonCaseHigh),
                        Integer.parseInt(editTextCaseXCoordinate.getText().toString()),
                        Integer.parseInt(editTextCaseYCoordinate.getText().toString()),
                        editTextCaseInformation.getText().toString());

                // write in database cases
                dbRef.child(dbId).setValue(c);
                }
        });

    }


    /**
     * Get the value of a checkbox
     * @param low
     * @param medium
     * @param high
     * @return
     */
    public int getScaleValue(RadioButton low, RadioButton medium, RadioButton high) {
        if(low.isChecked()) return 1;
        if(medium.isChecked()) return 2;
        if(high.isChecked()) return 3;
        else return -1;
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
                         // Toast.makeText(getApplicationContext(), R.string.uploaded, Toast.LENGTH_LONG).show();
                            showUploadedPic();

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

                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), R.string.errorSignUpFirst, Toast.LENGTH_LONG).show();
        }


    }

    private void showUploadedPic() {
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
                StorageReference riversRef = storageRef.child("images/" + uid + "/cases/case1/casepicture.jpg");


                riversRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        textViewUploadedPictures.setVisibility(View.VISIBLE);
                        imageViewUploadedPicture.setImageBitmap(bitmap);
                        imageViewUploadedPicture.setVisibility(View.VISIBLE);
                    }
                    }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), R.string.reportCanNotShowUploadedPicture, Toast.LENGTH_LONG).show();

                    }
                });

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), R.string.reportException, Toast.LENGTH_LONG).show();
            }

        }


    }



}

