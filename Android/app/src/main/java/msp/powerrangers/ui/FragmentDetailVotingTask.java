package msp.powerrangers.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Iterator;

import msp.powerrangers.R;
import msp.powerrangers.logic.Global;


public class FragmentDetailVotingTask extends Fragment {

    //  ui elements
    private TextView textViewVotingTitle;
    private TextView textViewVotingLocation;
    private Button buttonOk;
    private Button buttonNotOK;
    private ImageView imageBefore;
    private ImageView imageAfter;

    // firebase
    private StorageReference storageRef;
    private DatabaseReference dbRefTasks;
    private DatabaseReference refPathRanger;

    // current user from shared preferences
    SharedPreferences sharedPrefs;
    String userDbID;  // current user
    String rangerDbId; // Ranger

    private int position;
    private String taskDBId;
    private String location;
    private String title;
    private int reward;

    // images
    String imgBeforeURL;
    String imgAfterURL;
    Bitmap imgBeforeBitmap;
    Bitmap imgAfterBitmap;

    public FragmentDetailVotingTask() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageRef = FirebaseStorage.getInstance().getReference();

        Bundle bund = getArguments();
        position = bund.getInt("PositionVotingTask");
        taskDBId = bund.getString("TaskDbIdVotingTask");
        title = bund.getString("TitleVotingTask");
        location = bund.getString("LocationVotingTask");
        reward = bund.getInt("RewardVotingTask");

        // get the urls or the bitmaps from bundle
        imgBeforeURL = bund.getString("imageBeforeUrl");
        imgAfterURL = bund.getString("imageAfterUrl");


        // get the current user
        sharedPrefs = getContext().getSharedPreferences(getResources().getString(R.string.sharedPrefs_userDbIdPrefname), 0);
        userDbID = sharedPrefs.getString(getResources().getString(R.string.sharedPrefs_userDbId), null);

        dbRefTasks = FirebaseDatabase.getInstance().getReference("tasks");

        // Set action bar menu
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fr_detail_voting_task, container, false);

        // find UI elements
        final TextView textViewVotingTitle = (TextView) view.findViewById(R.id.textViewVotingTitle);
        final TextView textViewVotingLocation = (TextView) view.findViewById(R.id.textViewVotingLocation);
        Button buttonOk = (Button) view.findViewById(R.id.buttonOk);
        Button buttonNotOk = (Button) view.findViewById(R.id.buttonNotOk);
        final ImageView imageBefore = (ImageView) view.findViewById(R.id.ivVotingPic1);
        final ImageView imageAfter = (ImageView) view.findViewById(R.id.ivVotingPic2);

        // set image before
        if (imgBeforeURL != null) {     // no bitmap from bundle

            try {   // download pic before
                final File localFile = File.createTempFile("images", "jpg");
                StorageReference riversRef = storageRef.child(Global.getThumbUrl(imgBeforeURL));
                riversRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.i("Viki DetailVotingTask", "download erfolgreich, imageBefore");
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                imageBefore.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.i("Viki DetailVotingTask", exception.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.i("Viki DetailVotingTask", e.getMessage());
            }

        }

        // set image after
        if (imgAfterURL != null) {     // no bitmap from bundle

            try {   // download pic before
                final File localFile = File.createTempFile("images", "jpg");
                StorageReference riversRef = storageRef.child(Global.getThumbUrl(imgAfterURL));
                riversRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.i("Viki DetailVotingTask", "download erfolgreich, imageAfter");
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                imageAfter.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.i("Viki DetailVotingTask", exception.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.i("Viki DetailVotingTask", e.getMessage());
            }

        }


        // set title and location
        textViewVotingTitle.setText(title);
        textViewVotingLocation.setText(location);

        buttonNotOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbRefTasks.orderByChild("taskCompleted").equalTo(true)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Iterator iter = dataSnapshot.getChildren().iterator();
                                for (int i = 0; i < position; i++) {
                                    iter.next();
                                }

                                DataSnapshot singleSnapshot = (DataSnapshot) iter.next();

                                rangerDbId = (String) singleSnapshot.child("rangerDbId").getValue();

                                refPathRanger = FirebaseDatabase.getInstance().getReference().child("users").child(rangerDbId);

                                // ranger can`t vote for his own task :)
                                if (rangerDbId.equals(userDbID)) {
                                    Toast.makeText(getContext(), R.string.errorRangerVotesHisOwnTask, Toast.LENGTH_LONG).show();
                                } else {
                                    // when voted: update in user opentasks and completedtasks,
                                    // in task : set taskVoted true
                                    refPathRanger.addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnap) {

                                                    // decrement the number of open tasks
                                                    String currentNOT = (String) dataSnap.child("numberOpenTasks").getValue();
                                                    int newNOT = Integer.parseInt(currentNOT) - 1;
                                                    dataSnap.getRef().child("numberOpenTasks").setValue(newNOT);

                                                    // decrement the number of completed tasks
                                                    String currentNCT = (String) dataSnap.child("numberCompletedTasks").getValue();
                                                    int newNCT = Integer.parseInt(currentNCT) - 1;
                                                    dataSnap.getRef().child("numberCompletedTasks").setValue(newNCT);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    Log.e("FrDetailVotingTask", "The currentUser read failed: " + databaseError.getMessage());
                                                }

                                            });

                                    dbRefTasks.child(taskDBId).child("taskVoted").setValue(true);
                                    Toast.makeText(getContext(), R.string.votingNotOk, Toast.LENGTH_LONG).show();
                                }

                                // go back to FragmentStart
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                startActivity(i);
                                ((Activity) getActivity()).overridePendingTransition(0, 0);
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

                dbRefTasks.orderByChild("taskCompleted").equalTo(true)
                        .addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        Iterator iter = dataSnapshot.getChildren().iterator();
                                        for (int i = 0; i < position; i++) {
                                            iter.next();
                                        }
                                        DataSnapshot singleSnapshot = (DataSnapshot) iter.next();

                                        rangerDbId = String.valueOf(singleSnapshot.child("rangerDbId").getValue());
                                        refPathRanger = FirebaseDatabase.getInstance().getReference().child("users").child(rangerDbId);

                                        // ranger can`t vote for his own task :)
                                        if (rangerDbId.equals(userDbID)) {
                                            Toast.makeText(getContext(), R.string.errorRangerVotesHisOwnTask, Toast.LENGTH_LONG).show();
                                        } else {

                                            // when voted,
                                            // update user opentasks
                                            refPathRanger.addListenerForSingleValueEvent(
                                                    new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnap) {

                                                            // decrement bubble number opentasks
                                                            String currentNOT = (String) dataSnap.child("numberOpenTasks").getValue();
                                                            Log.i("Viki", "currentNOT" + currentNOT);
                                                            int newNOT = Integer.parseInt(currentNOT) - 1;
                                                            dataSnap.getRef().child("numberOpenTasks").setValue(String.valueOf(newNOT));
                                                            Log.i("Viki", "newNOT" + newNOT);

                                                            // update balance with reward
                                                            //int fixedReward = Integer.valueOf(reward);
                                                            String currentBalance = String.valueOf(dataSnap.child("balance").getValue());
                                                            Log.i("Viki", "Old Balance" + currentBalance);
                                                            int newBalance = Integer.parseInt(currentBalance) + reward;
                                                            dataSnap.getRef().child("balance").setValue(newBalance);
                                                            Log.i("Viki", "new balance" + newBalance);

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                            Log.e("FrDetailVotingTask", "The currentUser read failed: " + databaseError.getMessage());
                                                        }

                                                    });

                                            dbRefTasks.child(taskDBId).child("taskVoted").setValue(true);
                                            Toast.makeText(getContext(), R.string.votingOk, Toast.LENGTH_LONG).show();
                                        }

                                        // go back to FragmentStart
                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                        startActivity(i);
                                        ((Activity) getActivity()).overridePendingTransition(0, 0);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("FrDetailVotingTask", "The currentCase read failed: " + databaseError.getMessage());
                                    }
                                });


            }
        });

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
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                return true;
            default:
                return false;
        }
    }

}