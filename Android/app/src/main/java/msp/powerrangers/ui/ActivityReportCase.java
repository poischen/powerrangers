package msp.powerrangers.ui;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import msp.powerrangers.logic.Case;
import msp.powerrangers.logic.Detective;
import msp.powerrangers.logic.User;

public class ActivityReportCase extends AppCompatActivity {

    private static final int CHOOSE_IMAGE_REQUEST = 123;
    private static final int STORAGE_PERMISSION_REQUEST = 234;
    final long ONE_MEGABYTE = 1024 * 1024;

    // TODO: set all fields in res to requiered!
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

    // TODO: only one radio button should be selectable
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

    // firebase db instances
    private DatabaseReference dbRefCases;
    private DatabaseReference dbRefUsers;


    // current firebaseUser
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private User us;

    private Case c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_case);

        //Firebase stuff
        storageRef = FirebaseStorage.getInstance().getReference();
        dbRefCases =  FirebaseDatabase.getInstance().getReference("cases");
        dbRefUsers =  FirebaseDatabase.getInstance().getReference("users");


        //get current User Object from Intent


        Intent myIntent = getIntent();
        us = (User) myIntent.getSerializableExtra("USER");


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


                // get attributes from a case
                String dbId = dbRefCases.push().getKey();
                String caseId = UUID.randomUUID().toString();

                // get values for the X and Y area 
                int areaX = Integer.valueOf(editTextCaseXCoordinate.getText().toString());
                int areaY = Integer.valueOf(editTextCaseYCoordinate.getText().toString());

                c = new Case(dbId, us.getId(), caseId,
                        editTextCaseTitle.getText().toString(),
                        editTextCaseCity.getText().toString(),
                        editTextCaseCountry.getText().toString(),
                        getScaleValue(radioButtonCaseLow, radioButtonCaseMiddle, radioButtonCaseHigh),
                        areaX,
                        areaY,
                        editTextCaseInformation.getText().toString());

             //   Toast.makeText(getApplicationContext(), "Created dat case", Toast.LENGTH_LONG).show();

                // write in database cases
                dbRefCases.child(dbId).setValue(c);

                Detective detective = new Detective(us, caseId);
                us.addCaseIDtoList(caseId);

                Toast.makeText(getApplicationContext(), R.string.reportCaseSuccess, Toast.LENGTH_LONG).show();

                finish();


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
            Uri filePath = data.getData();
            uploadFile(filePath);
        }

        if (requestCode == CHOOSE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getClipData() != null) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    // Call upload method
                    uploadFile(uri);
                }
            }
        }

    }
    /**
     * method to upload case pictures via firebase
     * @param filePath Path of image on device storage
     */
    private void uploadFile(Uri filePath) {

        if (firebaseUser != null) {
            String uid = null;
            //get UID to identify firebaseUser
            for (UserInfo profile : firebaseUser.getProviderData()) {
                uid = profile.getUid();
            };

            //get filename to write url into database later
            //String pictureName = getFileName(filePath);

            //create Path in Storage
            //final String storageAndDBPath = "images/cases/" + c.getId() + "/testimage.jpg";

            String imageID = UUID.randomUUID().toString();
            //StorageReference riversRef = storageRef.child("images/" + uid + "/cases/case1/casepicture.jpg");
            StorageReference riversRef = storageRef.child("images/cases" + c.getId() + "/cases/" + imageID + ".jpg");

            Toast.makeText(getApplicationContext(), R.string.uploadPicture, Toast.LENGTH_LONG).show();

            //upload to Firebase Storage
            //StorageReference riversRef = storageRef.child(storageAndDBPath);
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //upload successful, write url in database
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("cases");
                            String caseDbId = c.getId();
                            //db.child(caseDbId).child("caseImages").setValue(storageAndDBPath);

                            //upload successful, give information
                            // Toast.makeText(getApplicationContext(), R.string.uploaded, Toast.LENGTH_LONG).show();
                            //showUploadedPic();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception exception) {
                            //upload not successful
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
        File localFile = null;
        if (firebaseUser != null) {
            String uid = null;
            //get UID to identify firebaseUser
            for (UserInfo profile : firebaseUser.getProviderData()) {
                uid = profile.getUid();
            };
            try {
                localFile = File.createTempFile("images", "jpg");
                StorageReference riversRef = storageRef.child("images/" + uid + "/cases/");
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
                    public void onFailure(Exception exception) {
                        Toast.makeText(getApplicationContext(), R.string.reportCanNotShowUploadedPicture, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), R.string.reportException, Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
    Method to get the picturename of the file from the filechooser
    https://developer.android.com/guide/topics/providers/document-provider.html
    */
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = ActivityReportCase.this.getContentResolver().query(uri, null, null, null, null);
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