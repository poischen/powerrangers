package msp.powerrangers.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import msp.powerrangers.R;
import msp.powerrangers.logic.User;

public class ActivityDonate extends AppCompatActivity {

    EditText editTextDonateValue;
    Button ButtonDonateNow;
    User us;
    private DatabaseReference dbRefDonate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        Log.v("ActivityDonate", "ActivityDonate onCreate");
        dbRefDonate = FirebaseDatabase.getInstance().getReference("donate");

        //get current User Object from Intent
        Intent myIntent = getIntent();
        us = (User) myIntent.getSerializableExtra("USER");

        editTextDonateValue = (EditText) findViewById(R.id.aDonate_donateValue);
        ButtonDonateNow = (Button) findViewById(R.id.aDonate_buttonDonate);

        ButtonDonateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = editTextDonateValue.getText().toString();
                if (!TextUtils.isEmpty(value)){
                    double donateValue = Double.parseDouble(value);
                    try {
                        dbRefDonate.child(us.getDbId()).child("donation").push().setValue(donateValue);

                        editTextDonateValue.setText("");
                        Toast.makeText(ActivityDonate.this, R.string.aDonate_toastSuccessDonate, Toast.LENGTH_SHORT).show();
                    } catch (Exception e){
                        Toast.makeText(ActivityDonate.this, R.string.aDonate_toastFailDonate, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

}