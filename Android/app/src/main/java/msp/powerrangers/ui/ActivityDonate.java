package msp.powerrangers.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import msp.powerrangers.R;

public class ActivityDonate extends AppCompatActivity {

    EditText editTextDonateValue;
    Button ButtonDonateNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        Log.v("ActivityDonate", "ActivityDonate onCreate");

        editTextDonateValue = (EditText) findViewById(R.id.aDonate_donateValue);
        ButtonDonateNow = (Button) findViewById(R.id.aDonate_buttonDonate);

        ButtonDonateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = editTextDonateValue.getText().toString();
                if (!TextUtils.isEmpty(value)){
                    double donateValue = Double.parseDouble(value);
                    //TODO: write value into database
                    editTextDonateValue.setText("");
                    //TODO: give feedback if successfull or not
                    Toast.makeText(ActivityDonate.this, R.string.aDonate_toastSuccessDonate, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

}



