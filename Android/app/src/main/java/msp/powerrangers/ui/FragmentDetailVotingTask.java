package msp.powerrangers.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import msp.powerrangers.R;
import msp.powerrangers.logic.Global;


public class FragmentDetailVotingTask extends Fragment {

    // text Views
    private TextView textViewVotingTitle;
    private TextView textViewVotingLocation;


    // buttons
    private Button buttonOk;
    private Button buttonNotOK;

    private ImageView imageBefore;
    private ImageView imageAfter;


    private int position;

    // firebase storage Ref
    private StorageReference storageRef;

    // firebase db instances
    private DatabaseReference dbRefTasks;

    // current user from shared preferences
    SharedPreferences sharedPrefs;
    DatabaseReference refPathCurrentUser;
    String userDbID;  // Current User
    String rangerID; // Ranger


    List<String> pictureURLs;
    List<Bitmap> pictureBitmapList;

    public FragmentDetailVotingTask() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bund = getArguments();
        position = bund.getInt("PositionVotingTask");
        storageRef = FirebaseStorage.getInstance().getReference();
        pictureURLs = new ArrayList<>();
        pictureBitmapList = new ArrayList<>();
        Bitmap defaultPic = BitmapFactory.decodeResource(getResources(), R.drawable.nopicyet);
        pictureBitmapList.add(defaultPic);

        // get the current user
        sharedPrefs = getContext().getSharedPreferences(getResources().getString(R.string.sharedPrefs_userDbIdPrefname), 0);
        userDbID = sharedPrefs.getString(getResources().getString(R.string.sharedPrefs_userDbId), null);
        refPathCurrentUser = FirebaseDatabase.getInstance().getReference().child("users").child(userDbID);


        // Set action bar menu
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fr_detail_voting_task, container, false);

        //give user progress feedback while downloading pictures
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.uploadPicture));
        progressDialog.show();

        // find UI elements
        // text Views
         TextView textViewVotingTitle = (TextView) view.findViewById(R.id.textViewVotingTitle);
         TextView textViewVotingLocation = (TextView) view.findViewById(R.id.textViewVotingLocation);

        // buttons
         Button buttonOk = (Button) view.findViewById(R.id.buttonOk);
         Button buttonNotOk = (Button) view.findViewById(R.id.buttonNotOk);

        final ImageView imageBefore = (ImageView) view.findViewById(R.id.ivVotingPic1);
        ImageView imageAfter = (ImageView) view.findViewById(R.id.ivVotingPic2);

        // refernece to db tasks
        dbRefTasks = FirebaseDatabase.getInstance().getReference("tasks");

        dbRefTasks.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator iter = dataSnapshot.getChildren().iterator();

                        for (int i = 0; i < position; i++) {
                            iter.next();
                        }
                        DataSnapshot singleSnapshot = (DataSnapshot) iter.next();

                        //get task and after picture urls from db, download pictures from storage and show them
                        String beforePicURL = (String) singleSnapshot.child("taskPicture").getValue();
                        String afterPicURL = (String) singleSnapshot.child("taskPictureAfter").getValue();


                        // download pic before
                        try {
                            final File localFile = File.createTempFile("images", "jpg");
                            StorageReference riversRef = storageRef.child(Global.getThumbUrl(beforePicURL));
                            riversRef.getFile(localFile)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Log.v("Download", "download erfolgreich");
                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                            imageBefore.setImageBitmap(bitmap);
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


                        while (dsPictureURLs.hasNext()) {
                            DataSnapshot dataSnapshotChild = dsPictureURLs.next();
                            String url = dataSnapshotChild.getValue(String.class);
                            pictureURLs.add(url);



                        }

                        // set all data in the Detail View except of pictures
                        editTextConfirmCaseTitle.setText(caseTitle);
                        editTextConfirmCaseCity.setText(caseCity);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



        buttonNotOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbRefTasks.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Iterator iter = dataSnapshot.getChildren().iterator();
                        for (int i = 0; i < position; i++) {
                            iter.next();
                        }
                        DataSnapshot singleSnapshot = (DataSnapshot) iter.next();

                        rangerID = (String) singleSnapshot.child("rangerID").getValue();

                        // ranger can`t vote for his own task :)
                        if (rangerID.equals(userDbID)) {
                            Toast.makeText(getContext(), R.string.errorRangerVotesHisOwnTask, Toast.LENGTH_LONG).show();
                        }

                        // fill in new values
                        else {
                            // TODO function votes
                            // TODO: taskVoted auf true
                            // TODO: reward entsprechend setzen und earned in Start aktualisieren
                            // TODO: anzahl open Tasks in start dekrementieren, hier eventuell prüfen in der query dass dieses case nicht in open tasks angezegt wird
                            // TODO:


                            // when voted,
                            // update user opentasks
                            refPathCurrentUser.addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnap) {

                                            // WAS SOLL GEMACHT WERDEN WENN NICHT GESCHAFFT??? OPEN TASKS BLEIBT GLEICH ODER WAS?

                                            String currentNumberOpenTasks = (String) dataSnap.child("numberOpenTasks").getValue();
                                            // TODO: warum sind numberOpenTasks string????
                                            int newNumberOfOpenTasks = Integer.parseInt(currentNumberOpenTasks) - 1;
                                            String stringValueOfNewNOT = String.valueOf(newNumberOfOpenTasks);
                                            dataSnap.getRef().child("numberOpenTasks").setValue(stringValueOfNewNOT);


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e("FrDetailConfirmerCase", "The currentUser read failed: " + databaseError.getMessage());
                                        }

                                    });






                            // go back to FragmentStart
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            startActivity(i);
                            ((Activity) getActivity()).overridePendingTransition(0, 0);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });



        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Query filteredCases = dbRefCases.orderByChild("confirmed").equalTo(false);
                dbRefTasks.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Iterator iter = dataSnapshot.getChildren().iterator();
                                for (int i = 0; i < position; i++) {
                                    iter.next();
                                }
                                DataSnapshot singleSnapshot = (DataSnapshot) iter.next();

                                rangerID = String.valueOf(singleSnapshot.child("rangerID").getValue());

                                // ranger can`t vote for his own task :)
                                if (rangerID.equals(userDbID)) {
                                    Toast.makeText(getContext(), R.string.errorRangerVotesHisOwnTask, Toast.LENGTH_LONG).show();
                                }

                                // fill in new values
                                else {
                                    // TODO function votes
                                    // TODO: taskVoted auf true
                                    // TODO: reward entsprechend setzen und earned in Start aktualisieren
                                    // TODO: anzahl open Tasks in start dekrementieren
                                    // TODO:


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


                                    // when voted,
                                    // update user opentasks
                                    refPathCurrentUser.addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnap) {

                                                    String currentNumberOpenTasks = (String) dataSnap.child("numberOpenTasks").getValue();
                                                    // TODO: warum sind numberOpenTasks string????
                                                    int newNumberOfOpenTasks = Integer.parseInt(currentNumberOpenTasks) - 1;
                                                    String stringValueOfNewNOT = String.valueOf(newNumberOfOpenTasks);
                                                    dataSnap.getRef().child("numberOpenTasks").setValue(stringValueOfNewNOT);

                                                    String currentCount = String.valueOf(dataSnap.child("numberCompletedTasks").getValue());
                                                    int newCount = Integer.parseInt(currentCount) + 1;
                                                    dataSnap.getRef().child("numberCompletedTasks").setValue(newCount);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    Log.e("FrDetailConfirmerCase", "The currentUser read failed: " + databaseError.getMessage());
                                                }

                                            });
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