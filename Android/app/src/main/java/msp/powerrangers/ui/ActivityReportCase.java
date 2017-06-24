package msp.powerrangers.ui;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import msp.powerrangers.R;

public class ActivityReportCase extends AppCompatActivity {

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
    private  EditText editTextCaseInformation;

    // checkboxes scale
    private CheckBox checkBoxCaseLow;
    private CheckBox checkBoxCaseMiddle;
    private CheckBox checkBoxCaseHigh;

    // buttons
    private Button buttonCaseReport;

    // additional



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_case);


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

        buttonCaseReport = (Button) findViewById(R.id.buttonCaseReport);



        // add action bar going back to parent
        // TODO: im Moment geht man zurück zur MainActivity, trotzdem zur FragmentStart, abchecken wie man das sauber macht
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);






        buttonCaseReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if edittexts empty, if yes maketoast, if not create new case
            }
        });



    }
}
