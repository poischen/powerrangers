package msp.powerrangers.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import msp.powerrangers.R;
import msp.powerrangers.logic.Global;
import msp.powerrangers.logic.User;


public class FragmentDetailConfirmerCase extends Fragment {

    // text Views
    private TextView textViewConfirmCaseTitle;
    private TextView textViewConfirmCaseCity;
    private TextView textViewConfirmCaseCountry;
    private TextView textViewConfirmCaseScala;
    private TextView textViewConfirmCaseAreaCoordinates;
    private TextView textViewConfirmCaseXCoordinate;
    private TextView textViewConfirmCaseYCoordinate;
    private TextView textViewConfirmCaseInformation;

    // edit Texts
    private EditText editTextConfirmCaseTitle;
    private EditText editTextConfirmCaseCity;
    private EditText editTextConfirmCaseCountry;
    private EditText editTextConfirmCaseXCoordinate;
    private EditText editTextConfirmCaseYCoordinate;
    private EditText editTextConfirmCaseInformation;

    // radio buttons scale
    private RadioButton radioButtonConfirmCaseLow;
    private RadioButton radioButtonConfirmCaseMiddle;
    private RadioButton radioButtonConfirmCaseHigh;

    // buttons
    private Button buttonConfirmCaseReport;


    //swipegallery
    ViewPager viewPager;
    List<String> pictureURLs;
    List<Bitmap> pictureBitmapList;

    private int position;

    // firebase storage Ref
    private StorageReference storageRef;

    // firebase db instances
    private DatabaseReference dbRefCases;

    // current user from shared preferences
    SharedPreferences sharedPrefs;
    DatabaseReference refPathCurrentUser;
    String userDbID;  // CONFIRMER
    String detectiveID; // DETECTIVE

    public FragmentDetailConfirmerCase() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bund = getArguments();
        position = bund.getInt("PositionConfirm");
        storageRef = FirebaseStorage.getInstance().getReference();
        pictureURLs = new ArrayList<>();
        pictureBitmapList = new ArrayList<>();
        Bitmap defaultPic = BitmapFactory.decodeResource(getResources(), R.drawable.nopicyet);
        pictureBitmapList.add(defaultPic);

        // get the current user [role: Confirmer]
        sharedPrefs = getContext().getSharedPreferences(getResources().getString(R.string.sharedPrefs_userDbIdPrefname), 0);
        userDbID = sharedPrefs.getString(getResources().getString(R.string.sharedPrefs_userDbId), null);
        refPathCurrentUser = FirebaseDatabase.getInstance().getReference().child("users").child(userDbID);


        // Set action bar menu
        setHasOptionsMenu(true);

        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fr_detail_confirmer_case, container, false);

        //give user progredd feedback while downloading pictures
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.uploadPicture));
        progressDialog.show();

        // find UI elements
        // text Views
        textViewConfirmCaseTitle = (TextView) view.findViewById(R.id.textViewConfirmCaseTitle);
        textViewConfirmCaseCity = (TextView) view.findViewById(R.id.textViewConfirmCaseCity);
        textViewConfirmCaseCountry = (TextView) view.findViewById(R.id.textViewConfirmCaseCountry);
        textViewConfirmCaseScala = (TextView) view.findViewById(R.id.textViewConfirmCaseScala);
        textViewConfirmCaseAreaCoordinates = (TextView) view.findViewById(R.id.textViewConfirmCaseAreaCoordinates);
        textViewConfirmCaseXCoordinate = (TextView) view.findViewById(R.id.textViewConfirmCaseXCoordinate);
        textViewConfirmCaseYCoordinate = (TextView) view.findViewById(R.id.textViewConfirmCaseYCoordinate);
        textViewConfirmCaseInformation = (TextView) view.findViewById(R.id.textViewConfirmCaseInformation);
        viewPager = (ViewPager) view.findViewById(R.id.fConfirmCaseViewPager);
        FragmentDetailConfirmerCase.ImageAdapter adapter = new FragmentDetailConfirmerCase.ImageAdapter(this.getContext());
        viewPager.setAdapter(adapter);

        // edit Texts
        editTextConfirmCaseTitle = (EditText) view.findViewById(R.id.editTextConfirmCaseTitle);
        editTextConfirmCaseCity = (EditText) view.findViewById(R.id.editTextConfirmCaseCity);
        editTextConfirmCaseCountry = (EditText) view.findViewById(R.id.editTextConfirmCaseCountry);
        editTextConfirmCaseXCoordinate = (EditText) view.findViewById(R.id.editTextConfirmCaseXCoordinate);
        editTextConfirmCaseYCoordinate = (EditText) view.findViewById(R.id.editTextConfirmCaseYCoordinate);
        editTextConfirmCaseInformation = (EditText) view.findViewById(R.id.editTextConfirmCaseInformation);


        // radio buttons scale
        radioButtonConfirmCaseLow = (RadioButton) view.findViewById(R.id.radioButtonConfirmCaseLow);
        radioButtonConfirmCaseMiddle = (RadioButton) view.findViewById(R.id.radioButtonConfirmCaseMiddle);
        radioButtonConfirmCaseHigh = (RadioButton) view.findViewById(R.id.radioButtonConfirmCaseHigh);


        // buttons
        buttonConfirmCaseReport = (Button) view.findViewById(R.id.buttonConfirmCaseReport);

        // fill in information from detective case in EditTexts
        dbRefCases = FirebaseDatabase.getInstance().getReference("cases");

        // get attributes from a case as default values to edit
        final Query filteredCases = dbRefCases.orderByChild("confirmed").equalTo(false);
        filteredCases.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator iter = dataSnapshot.getChildren().iterator();

                        for (int i = 0; i < position; i++) {
                            iter.next();
                        }
                        DataSnapshot singleSnapshot = (DataSnapshot) iter.next();

                        // Fetch the data from the DB
                        String caseTitle = (String) singleSnapshot.child("name").getValue();
                        String caseCity = (String) singleSnapshot.child("city").getValue();
                        String caseCountry = (String) singleSnapshot.child("country").getValue();
                        String caseComment = (String) singleSnapshot.child("comment").getValue();
                        String caseXCoord = String.valueOf(singleSnapshot.child("areaX").getValue());
                        String caseYCoord = String.valueOf(singleSnapshot.child("areaY").getValue());
                        String caseScale = String.valueOf(singleSnapshot.child("scale").getValue());

                        //get cases picture urls from db, download pictures from storage and show them
                        Iterator<DataSnapshot> dsPictureURLs = singleSnapshot.child("pictureURL").getChildren().iterator();
                        Log.v("DetailConfirmerCase", "dsPictureURLS: " + dsPictureURLs);

                        pictureBitmapList.clear();
                        updateImageViews();

                        while (dsPictureURLs.hasNext()) {
                            DataSnapshot dataSnapshotChild = dsPictureURLs.next();
                            String url = dataSnapshotChild.getValue(String.class);
                            pictureURLs.add(url);

                            try {
                                final File localFile = File.createTempFile("images", "jpg");
                                StorageReference riversRef = storageRef.child(Global.getDisplayUrl(url));
                                riversRef.getFile(localFile)
                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                Log.v("Download", "download erfolgreich");
                                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                pictureBitmapList.add(bitmap);
                                                updateImageViews();
                                                Log.v("DetailConfirmerCase", "picture Bitmap List new entry: " + bitmap);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Log.d("DetailConfirmerCase", exception.getMessage());
                                    }
                                });
                            } catch (Exception e) {
                                Log.d("DetailConfirmerCase", e.getMessage());
                            }

                        }

                        // set all data in the Detail View except of pictures
                        editTextConfirmCaseTitle.setText(caseTitle);
                        editTextConfirmCaseCity.setText(caseCity);
                        editTextConfirmCaseCountry.setText(caseCountry);
                        editTextConfirmCaseInformation.setText(caseComment);
                        editTextConfirmCaseXCoordinate.setText(caseXCoord);
                        editTextConfirmCaseYCoordinate.setText(caseYCoord);

                        switch (caseScale) {

                            case "1":
                                radioButtonConfirmCaseLow.setChecked(true);
                                break;

                            case "2":
                                radioButtonConfirmCaseMiddle.setChecked(true);
                                break;

                            case "3":
                                radioButtonConfirmCaseHigh.setChecked(true);
                                break;

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        radioButtonConfirmCaseLow.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                radioButtonConfirmCaseHigh.setChecked(false);
                radioButtonConfirmCaseMiddle.setChecked(false);
                radioButtonConfirmCaseLow.setChecked(true);
            }
        });


        radioButtonConfirmCaseMiddle.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                radioButtonConfirmCaseLow.setChecked(false);
                radioButtonConfirmCaseHigh.setChecked(false);
                radioButtonConfirmCaseMiddle.setChecked(true);
            }
        });


        radioButtonConfirmCaseHigh.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                radioButtonConfirmCaseLow.setChecked(false);
                radioButtonConfirmCaseMiddle.setChecked(false);
                radioButtonConfirmCaseHigh.setChecked(true);
            }
        });

        buttonConfirmCaseReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Query filteredCases = dbRefCases.orderByChild("confirmed").equalTo(false);
                filteredCases.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Iterator iter = dataSnapshot.getChildren().iterator();
                            for (int i = 0; i < position; i++) {
                                iter.next();
                            }
                            DataSnapshot singleSnapshot = (DataSnapshot) iter.next();

                            detectiveID = String.valueOf(singleSnapshot.child("detectiveID").getValue());

                            // detective can`t confirme his own case :)
                            if (detectiveID.equals(userDbID)) {
                                Toast.makeText(getContext(), R.string.detailConfirmerCaseDectiveError, Toast.LENGTH_LONG).show();
                            }

                            // fill in new values
                            else {
                                singleSnapshot.child("name").getRef().setValue(editTextConfirmCaseTitle.getText().toString());
                                singleSnapshot.child("city").getRef().setValue(editTextConfirmCaseCity.getText().toString());
                                singleSnapshot.child("country").getRef().setValue(editTextConfirmCaseCountry.getText().toString());
                                singleSnapshot.child("comment").getRef().setValue(editTextConfirmCaseInformation.getText().toString());
                                singleSnapshot.child("areaX").getRef().setValue(editTextConfirmCaseXCoordinate.getText().toString());
                                singleSnapshot.child("areaY").getRef().setValue(editTextConfirmCaseYCoordinate.getText().toString());
                                singleSnapshot.child("scale").getRef().setValue(getScaleValue(radioButtonConfirmCaseLow, radioButtonConfirmCaseMiddle, radioButtonConfirmCaseHigh));
                                singleSnapshot.child("confirmed").getRef().setValue(true);
                                // add the confirmer id to the childs
                                singleSnapshot.child("confirmerId").getRef().setValue(userDbID);

                                // update user balance & number confirmed cases
                                refPathCurrentUser.addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnap) {
                                        // TODO eigentlich sollte der fixedReward in der Confirmer-Instanz gesetzt werden. Zurzeit gibt es keinen Confimer objekt..
                                        // [getFixedReward vom Confirmer benutzen]
                                        int fixedReward = 5;
                                        String currentBalance = String.valueOf(dataSnap.child("balance").getValue());
                                        int newBalance =Integer.parseInt(currentBalance)+fixedReward;
                                        dataSnap.getRef().child("balance").setValue(newBalance);

                                        String currentCount = String.valueOf(dataSnap.child("numberConfirmedCases").getValue());
                                        int newCount = Integer.parseInt(currentCount)+1;
                                        dataSnap.getRef().child("numberConfirmedCases").setValue(newCount);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("FrDetailConfirmerCase", "The currentUser read failed: " + databaseError.getMessage());
                                    }

                                });

                                Toast.makeText(getContext(), R.string.detailConfirmerCaseConfirmMessage, Toast.LENGTH_LONG).show();

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("FrDetailConfirmerCase", "The currentCase read failed: " + databaseError.getMessage());
                        }
                    });


                // go back to FragmentStart
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);

            }
        });

        progressDialog.cancel();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (pictureBitmapList.size() != pictureURLs.size()) {
            for (int i=0; i<=pictureURLs.size(); i++){
                try {final File localFile = File.createTempFile("images", "jpg");
                    StorageReference riversRef = storageRef.child(pictureURLs.get(i));
                    riversRef.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.v("Download", "download erfolgreich");
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    pictureBitmapList.add(bitmap);
                                    Log.v("DetailConfirmerCase", "picture Bitmap List new entry: " + bitmap);

                                    getActivity().runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            viewPager.getAdapter().notifyDataSetChanged();
                                        }
                                    });

                                    //viewPager.getAdapter().notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("DetailConfirmerCase", exception.getMessage());
                        }
                    });


                } catch (Exception e) {
                    Log.d("DetailConfirmerCase", e.getMessage());
                }
            }

        }*/

    }



    //TODO: add childEventListener for new added entrys
    /*ref.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
            Post newPost = dataSnapshot.getValue(Post.class);
            System.out.println("Author: " + newPost.author);
            System.out.println("Title: " + newPost.title);
            System.out.println("Previous Post ID: " + prevChildKey);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {}

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

        @Override
        public void onCancelled(DatabaseError databaseError) {}
    });*/


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

    public void updateImageViews(){
        viewPager.getAdapter().notifyDataSetChanged();
    }


    public class ImageAdapter extends PagerAdapter {
        Context context;

        ImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getItemPosition(Object object) {
            if (pictureBitmapList.contains((View) object)){
                return pictureBitmapList.indexOf((View) object);
            } else {
                return POSITION_NONE;
            }
        }

        @Override
        public int getCount() {
            return pictureBitmapList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            final Bitmap bmp = pictureBitmapList.get(position);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(bmp);

            imageView.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Dialog builder = new Dialog(getContext());
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            //nothing;
                        }
                    });

                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageBitmap(bmp);
                    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    builder.show();
                }
            });

            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

    // Get back to activity on back button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                return true;
            default:
                return false;
        }
    }

}