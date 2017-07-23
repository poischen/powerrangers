package msp.powerrangers.ui;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import msp.powerrangers.R;
import msp.powerrangers.logic.Case;
import msp.powerrangers.logic.Detective;
import msp.powerrangers.logic.User;


public class ActivityReportCase extends AppCompatActivity {

    private static final int CHOOSE_CASEIMAGE_REQUEST = 123;
    private static final int CHOOSE_TASKIMAGES_REQUEST = 789;
    private static final int STORAGE_PERMISSION_REQUEST = 234;
    final long ONE_MEGABYTE = 1024 * 1024;

    // edit Texts
    private EditText editTextCaseTitle;
    private EditText editTextCaseCity;
    private EditText editTextCaseCountry;
    private NumberPicker numberPickerCaseXCoordinate;
    private NumberPicker numberPickerCaseYCoordinate;
    private EditText editTextCaseInformation;

    // radio buttons scale
    private RadioButton radioButtonCaseLow;
    private RadioButton radioButtonCaseMiddle;
    private RadioButton radioButtonCaseHigh;

    // buttons
    private Button buttonCaseReport;
    private ImageButton imageButtonUploadPictureCase;
    private ImageButton imageButtonUploadPicturesTasks;

    //swipegallery
    ViewPager viewPagerTaskPics;
    ImageView imgViewCasePic;

    private User us;
    String userDbId;

    private Case c;
    private List<Uri> pictureUrisList;
    private List<Bitmap> pictureBitmapList;
    List<String> casePictures;
    boolean isDefaultPic;
    Bitmap bmpCaseImgage;
    Uri bmpUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_case);

        //stuff for pic upload
        pictureUrisList = new ArrayList<>();
        pictureBitmapList = new ArrayList<>();

        casePictures = new ArrayList<>();
        isDefaultPic = true;

        //get current User Object from Intent
        Intent myIntent = getIntent();
        us = (User) myIntent.getSerializableExtra("USER");
        userDbId = us.getDbId();

        // find UI elements
        editTextCaseTitle = (EditText) findViewById(R.id.editTextCaseTitle);
        editTextCaseCity = (EditText) findViewById(R.id.editTextCaseCity);
        editTextCaseCountry = (EditText) findViewById(R.id.editTextCaseCountry);
        numberPickerCaseXCoordinate = (NumberPicker) findViewById(R.id.numberPickerCaseXCoordinate);
        numberPickerCaseYCoordinate = (NumberPicker) findViewById(R.id.numberPickerCaseYCoordinate);
        numberPickerCaseXCoordinate.setMinValue(1);
        numberPickerCaseYCoordinate.setMinValue(1);
        numberPickerCaseXCoordinate.setMaxValue(100);
        numberPickerCaseYCoordinate.setMaxValue(100);
        editTextCaseInformation = (EditText) findViewById(R.id.editTextCaseInformation);
        radioButtonCaseLow = (RadioButton) findViewById(R.id.radioButtonCaseLow);
        radioButtonCaseMiddle = (RadioButton) findViewById(R.id.radioButtonCaseMiddle);
        radioButtonCaseHigh = (RadioButton) findViewById(R.id.radioButtonCaseHigh);
        imageButtonUploadPictureCase = (ImageButton) findViewById(R.id.imageButtonUploadPictureCase);
        imageButtonUploadPicturesTasks = (ImageButton) findViewById(R.id.imageButtonUploadPictureTask);
        buttonCaseReport = (Button) findViewById(R.id.buttonCaseReport);
        viewPagerTaskPics = (ViewPager) findViewById(R.id.aReportCaseViewPager);
        imgViewCasePic = (ImageView) findViewById(R.id.aReportCaseTaskPicture);
        ActivityReportCase.ImageAdapter adapter = new ActivityReportCase.ImageAdapter(this);
        viewPagerTaskPics.setAdapter(adapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get and show photos
        imageButtonUploadPicturesTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDefaultPic) {
                    isDefaultPic = false;
                    pictureBitmapList.clear();
                    updateImageViews();
                }
                showFileChooser(CHOOSE_TASKIMAGES_REQUEST);

            }
        });

        imageButtonUploadPictureCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser(CHOOSE_CASEIMAGE_REQUEST);
            }
        });


        // allow only one radio button to be checked!
        radioButtonCaseLow.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                radioButtonCaseHigh.setChecked(false);
                radioButtonCaseMiddle.setChecked(false);
                radioButtonCaseLow.setChecked(true);
                radioButtonCaseLow.setBackground(getDrawable(R.drawable.radiobutton_background_checked));
                radioButtonCaseMiddle.setBackground(getDrawable(R.drawable.radiobutton_background));
                radioButtonCaseHigh.setBackground(getDrawable(R.drawable.radiobutton_background));
            }
        });


        radioButtonCaseMiddle.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                radioButtonCaseLow.setChecked(false);
                radioButtonCaseHigh.setChecked(false);
                radioButtonCaseMiddle.setChecked(true);
                radioButtonCaseLow.setBackground(getDrawable(R.drawable.radiobutton_background));
                radioButtonCaseMiddle.setBackground(getDrawable(R.drawable.radiobutton_background_checked));
                radioButtonCaseHigh.setBackground(getDrawable(R.drawable.radiobutton_background));
            }
        });


        radioButtonCaseHigh.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                radioButtonCaseLow.setChecked(false);
                radioButtonCaseMiddle.setChecked(false);
                radioButtonCaseHigh.setChecked(true);
                radioButtonCaseLow.setBackground(getDrawable(R.drawable.radiobutton_background));
                radioButtonCaseMiddle.setBackground(getDrawable(R.drawable.radiobutton_background));
                radioButtonCaseHigh.setBackground(getDrawable(R.drawable.radiobutton_background_checked));
            }
        });


        // report a case
        buttonCaseReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // firebase db instances
                DatabaseReference dbRefCases;
                DatabaseReference refPathCurrentUser;

                dbRefCases = FirebaseDatabase.getInstance().getReference("cases");
                refPathCurrentUser = FirebaseDatabase.getInstance().getReference().child("users").child(userDbId);

                // get attributes from a case
                String dbId = dbRefCases.push().getKey();
                String caseId = UUID.randomUUID().toString();

                //get input values
                int areaX = numberPickerCaseXCoordinate.getValue();
                int areaY = numberPickerCaseYCoordinate.getValue();
                String caseTitle = editTextCaseTitle.getText().toString().trim();
                String caseCity = editTextCaseCity.getText().toString().trim();
                String caseCountry = editTextCaseCountry.getText().toString().trim();
                int scaleValue = getScaleValue(radioButtonCaseLow, radioButtonCaseMiddle, radioButtonCaseHigh);
                String caseInformation = editTextCaseInformation.getText().toString();

                if (areaX > 0 && areaY > 0
                        && !TextUtils.isEmpty(caseTitle)
                        && !TextUtils.isEmpty(caseCity)
                        && !TextUtils.isEmpty(caseCountry)
                        && pictureUrisList.size() > 1) {

                    casePictures = uploadFiles(caseId, pictureUrisList);

                    //Create Case
                    c = new Case(dbId,
                            us.getDbId(),
                            caseId,
                            caseTitle,
                            caseCity,
                            caseCountry,
                            scaleValue,
                            areaX,
                            areaY,
                            casePictures,
                            caseInformation
                    );

                    // create a detective
                    Detective detective = new Detective(us, caseId);
                    final int rewardForCase = detective.getRewardPerCase();

                    // write case to in database cases
                    dbRefCases.child(dbId).setValue(c);

                    // update user balance & number reported cases
                    refPathCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            long currentBalance = (long) dataSnapshot.child("balance").getValue();
                            dataSnapshot.getRef().child("balance").setValue(currentBalance + rewardForCase);

                            String currentCount = String.valueOf(dataSnapshot.child("numberReportedCases").getValue());
                            int newCount = Integer.valueOf(currentCount) + 1;
                            dataSnapshot.getRef().child("numberReportedCases").setValue(String.valueOf(newCount));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Toast.makeText(getApplicationContext(), R.string.reportCaseSuccess, Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), R.string.reportCaseFillFields, Toast.LENGTH_LONG).show();
                }
            }

        });
    }


    /**
     * Get the value of a checkbox
     *
     * @param low
     * @param medium
     * @param high
     * @return
     */
    public int getScaleValue(RadioButton low, RadioButton medium, RadioButton high) {
        if (low.isChecked()) return 1;
        if (medium.isChecked()) return 2;
        if (high.isChecked()) return 3;
        else return -1;
    }

    /**
     * method to show file chooser for images
     */
    private void showFileChooser(int requestCode) {
        Intent getimageintent = new Intent();
        getimageintent.setType("image/*");
        getimageintent.setAction(Intent.ACTION_GET_CONTENT);
        if (requestCode == CHOOSE_CASEIMAGE_REQUEST) {
            startActivityForResult(Intent.createChooser(getimageintent, getResources().getString(R.string.reportChooseCasePic)), requestCode);
        } else {
            getimageintent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(getimageintent, getResources().getString(R.string.reportChooseTaskPics)), requestCode);
        }
    }

    /**
     * method to handle the image chooser activity result:
     * get the picture and upload it
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_TASKIMAGES_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri uri = data.getData();
                pictureUrisList.add(uri);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                pictureBitmapList.add(bitmap);
                viewPagerTaskPics.getAdapter().notifyDataSetChanged();
                viewPagerTaskPics.setBackground(null);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CHOOSE_CASEIMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                bmpUri = data.getData();
                bmpCaseImgage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), bmpUri);
                imgViewCasePic.setImageBitmap(bmpCaseImgage);
                imgViewCasePic.setBackground(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CHOOSE_TASKIMAGES_REQUEST && resultCode == RESULT_OK && data != null && data.getClipData() != null) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    try {
                        Uri uri = item.getUri();
                        pictureUrisList.add(uri);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        pictureBitmapList.add(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                viewPagerTaskPics.getAdapter().notifyDataSetChanged();
            }
        }

    }

    /**
     * method to upload case pictures via firebase
     *
     * @param caseID id of the created case
     * @return List<String> of the urls of the pictures in the database
     */
    private List<String> uploadFiles(String caseID, List<Uri> pictureUrisList) {
        final List<String> storageUrls = new ArrayList<>();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        //upload case pic
        final String storageAndDBPathCase;
        storageAndDBPathCase = "images/cases/" + caseID + "/0.jpg";
        storageUrls.add(storageAndDBPathCase);

        StorageReference riversRefCase = storageRef.child(storageAndDBPathCase);

        riversRefCase.putFile(bmpUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i("ReportCase", "Case img was uploaded!");
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
                    }
                });

        //upload task pics
        for (int i = 0; i < pictureUrisList.size(); i++) {

            int j = i + 1;
            final String storageAndDBPath;
            storageAndDBPath = "images/cases/" + caseID + "/" + j + ".jpg";

            //write path from storage into list for case-db
            storageUrls.add(storageAndDBPath);

            //upload to Firebase Storage
            StorageReference riversRefTasks = storageRef.child(storageAndDBPath);

            riversRefTasks.putFile(pictureUrisList.get((i)))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.i("ReportCase", "Task img was uploaded!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception exception) {
                            //upload not successful
                            Toast.makeText(getApplicationContext(), R.string.errorUpload, Toast.LENGTH_LONG).show();
                            storageUrls.remove(storageAndDBPath);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        }
                    });
        }

        return storageUrls;
    }

    public void updateImageViews() {
        viewPagerTaskPics.getAdapter().notifyDataSetChanged();
    }

    /*
    Method to get the picturename of the file from the filechooser
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


    public class ImageAdapter extends PagerAdapter {
        Context context;

        ImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return pictureBitmapList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            if (pictureBitmapList.contains((View) object)) {
                return pictureBitmapList.indexOf((View) object);
            } else {
                return POSITION_NONE;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            int padding = 10;
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(pictureBitmapList.get(position));
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

}