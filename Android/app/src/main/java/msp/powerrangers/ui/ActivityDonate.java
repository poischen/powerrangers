package msp.powerrangers.ui;

import android.content.Intent;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import msp.powerrangers.R;
import msp.powerrangers.logic.Donation;
import msp.powerrangers.logic.User;

public class ActivityDonate extends AppCompatActivity {

    // view
    EditText editTextDonateValue;
    Button ButtonDonateNow;

    // firebase instances
    private DatabaseReference dbRefDonate;
    private DatabaseReference refPathCurrentUser;

    User us;
    String usDbId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        Log.v("ActivityDonate", "ActivityDonate onCreate");
        Log.i("KATJA", "********************   new donation   **************************");
        Log.i("KATJA", "ActivityDonate onCreate");

        //get current User Object from Intent
        Intent myIntent = getIntent();
        us = (User) myIntent.getSerializableExtra("USER");
        usDbId = us.getDbId();
        Log.i("KATJA", "user dbId:" + usDbId);

        // firebase references to donations and current user
        dbRefDonate = FirebaseDatabase.getInstance().getReference("donations");
        refPathCurrentUser = FirebaseDatabase.getInstance().getReference().child("users").child(usDbId);

        editTextDonateValue = (EditText) findViewById(R.id.aDonate_donateValue);
        ButtonDonateNow = (Button) findViewById(R.id.aDonate_buttonDonate);

        ButtonDonateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = editTextDonateValue.getText().toString();

                if (!TextUtils.isEmpty(value)) {

                    final double donateValue = Double.parseDouble(value);

                    try {
                        // create instance of donation class
                        String donationDbId = dbRefDonate.push().getKey();
                        Donation d = new Donation(donationDbId, usDbId, donateValue);

                        // write to in database donations
                        dbRefDonate.child(donationDbId).setValue(d);

                        // update user balance & number reported cases
                        refPathCurrentUser.addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        String currentDonatedValue = String.valueOf(dataSnapshot.child("donatedValue").getValue());
                                        double newDV = Double.parseDouble(currentDonatedValue)+donateValue;
                                        dataSnapshot.getRef().child("donatedValue").setValue(newDV);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });

                        editTextDonateValue.setText("");
                        Toast.makeText(ActivityDonate.this, R.string.aDonate_toastSuccessDonate, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(ActivityDonate.this, R.string.aDonate_toastFailDonate, Toast.LENGTH_SHORT).show();
                    }

                    // return to main activity
                    finish();

                }
            }
        });

    }

}