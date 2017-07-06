package msp.powerrangers.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Iterator;

import msp.powerrangers.R;



public class FragmentDetailConfirmerCase extends Fragment {

    // text Views
    private TextView textViewConfirmCaseTitle;
    private TextView textViewConfirmCaseCity;
    private TextView textViewConfirmCaseCountry;
    private TextView textViewConfirmCaseScala;
    private TextView textViewConfirmCaseAreaCoordinates;
    private TextView textViewConfirmCaseXCoordinate;
    private TextView textViewConfirmCaseYCoordinate;
    private TextView textViewConfirmUploadedPictures;
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
    private ImageView imageViewConfirmUploadedPicture;

    private int position;


    // firebase storage Ref
    private StorageReference storageRef;

    // firebase db instances
    private DatabaseReference dbRefCases;

    public FragmentDetailConfirmerCase() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bund = getArguments();
        position = bund.getInt("Position");
      //  Log.i("DIE POSITION !!!!!!!!!!" , String.valueOf(position));

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fr_detail_confirmer_case, container, false);

        // find UI elements
        // text Views
        textViewConfirmCaseTitle = (TextView) view.findViewById(R.id.textViewConfirmCaseTitle);
        textViewConfirmCaseCity = (TextView) view.findViewById(R.id.textViewConfirmCaseCity);
        textViewConfirmCaseCountry = (TextView) view.findViewById(R.id.textViewConfirmCaseCountry);
        textViewConfirmCaseScala = (TextView) view.findViewById(R.id.textViewConfirmCaseScala);
        textViewConfirmCaseAreaCoordinates = (TextView) view.findViewById(R.id.textViewConfirmCaseAreaCoordinates);
        textViewConfirmCaseXCoordinate = (TextView) view.findViewById(R.id.textViewConfirmCaseXCoordinate);
        textViewConfirmCaseYCoordinate = (TextView) view.findViewById(R.id.textViewConfirmCaseYCoordinate);
        textViewConfirmUploadedPictures = (TextView) view.findViewById(R.id.textViewConfirmUploadedPictures);
        textViewConfirmCaseInformation = (TextView) view.findViewById(R.id.textViewConfirmCaseInformation);

        // edit Texts
        editTextConfirmCaseTitle = (EditText) view.findViewById(R.id.editTextConfirmCaseTitle);
        editTextConfirmCaseCity = (EditText) view.findViewById(R.id.editTextConfirmCaseCity);
        editTextConfirmCaseCountry = (EditText) view.findViewById(R.id.editTextConfirmCaseCountry);
        editTextConfirmCaseXCoordinate = (EditText) view.findViewById(R.id.editTextConfirmCaseXCoordinate);
        editTextConfirmCaseYCoordinate = (EditText) view.findViewById(R.id.editTextConfirmCaseYCoordinate);
        editTextConfirmCaseInformation = (EditText) view.findViewById(R.id.editTextConfirmCaseInformation);


        // radio buttons scale
        radioButtonConfirmCaseLow = (RadioButton) view.findViewById(R.id.radioButtonConfirmCaseLow);
        radioButtonConfirmCaseMiddle= (RadioButton) view.findViewById(R.id.radioButtonConfirmCaseMiddle);;
        radioButtonConfirmCaseHigh= (RadioButton) view.findViewById(R.id.radioButtonConfirmCaseHigh);;

        // buttons
        buttonConfirmCaseReport = (Button) view.findViewById(R.id.buttonConfirmCaseReport);
        imageViewConfirmUploadedPicture = (ImageView) view.findViewById(R.id.imageViewConfirmUploadedPicture);

        // eventuell todo ActionBar


        // fill in information from detective case in EditTexts TODO
        dbRefCases = FirebaseDatabase.getInstance().getReference("cases");

        // get attributes from a case
       // final String dbId = dbRefCases.push().getKey();

        dbRefCases.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                            Iterator iter = dataSnapshot.getChildren().iterator();

                            for(int i = 0; i < position; i++) {
                                iter.next();
                            }

                            DataSnapshot singleSnapshot = (DataSnapshot) iter.next();

                            // Fetch the data from the DB
                            String caseTitle = (String) singleSnapshot.child("name").getValue();
                            String caseCity = (String) singleSnapshot.child("city").getValue();
                            String caseCountry = (String) singleSnapshot.child("country").getValue();
                            String caseComment = (String) singleSnapshot.child("comment").getValue();
                            String caseXCoord =  String.valueOf(singleSnapshot.child("areaX").getValue());
                            String caseYCoord =  String.valueOf(singleSnapshot.child("areaY").getValue());
                            String caseScale =  String.valueOf(singleSnapshot.child("scale").getValue());

                            //Toast.makeText(getContext(), caseTitle, Toast.LENGTH_LONG).show();

                            // set the data in the Detail View
                            editTextConfirmCaseTitle.setText(caseTitle);
                            editTextConfirmCaseCity.setText(caseCity);
                            editTextConfirmCaseCountry.setText(caseCountry);
                            editTextConfirmCaseInformation.setText(caseComment);
                            editTextConfirmCaseXCoordinate.setText(caseXCoord);
                            editTextConfirmCaseYCoordinate.setText(caseYCoord);

                            switch(caseScale){

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



        radioButtonConfirmCaseLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonConfirmCaseHigh.setChecked(false);
                radioButtonConfirmCaseMiddle.setChecked(false);
                radioButtonConfirmCaseLow.setChecked(true);
            }
        });


        radioButtonConfirmCaseMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonConfirmCaseLow.setChecked(false);
                radioButtonConfirmCaseHigh.setChecked(false);
                radioButtonConfirmCaseMiddle.setChecked(true);
            }
        });


        radioButtonConfirmCaseHigh.setOnClickListener(new View.OnClickListener() {
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
                Toast.makeText(getContext(), "to be implemented ;-)", Toast.LENGTH_SHORT).show();

                dbRefCases.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Iterator iter = dataSnapshot.getChildren().iterator();

                                for (int i = 0; i < position; i++) {
                                    iter.next();
                                }

                                DataSnapshot singleSnapshot = (DataSnapshot) iter.next();


                                // fill in new values
                                singleSnapshot.child("name").getRef().setValue(editTextConfirmCaseTitle.getText().toString());
                                singleSnapshot.child("city").getRef().setValue(editTextConfirmCaseCity.getText().toString());
                                singleSnapshot.child("country").getRef().setValue(editTextConfirmCaseCountry.getText().toString());
                                singleSnapshot.child("comment").getRef().setValue(editTextConfirmCaseInformation.getText().toString());
                                singleSnapshot.child("areaX").getRef().setValue(editTextConfirmCaseXCoordinate.getText().toString());
                                singleSnapshot.child("areaY").getRef().setValue(editTextConfirmCaseYCoordinate.getText().toString());
                                singleSnapshot.child("scale").getRef().setValue(getScaleValue(radioButtonConfirmCaseLow, radioButtonConfirmCaseMiddle, radioButtonConfirmCaseHigh));
                                singleSnapshot.child("confirmed").getRef().setValue(true);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                ///TODO: update Confirmed Cases Bubble on Start
                //TextView confirmedCases = (TextView) view.findViewById(R.id.numberConfirmedCases);

               // int n = Integer.parseInt(confirmedCases.getText().toString());
                //Toast.makeText(getActivity(), "cases: "+confirmedCases.getText(), Toast.LENGTH_SHORT).show();
                //n++;

                View startView = new View(getActivity());
                startView = inflater.inflate(R.layout.fragment_start, container, false);

                if (startView == null) {
                    Toast.makeText(getActivity(), "fucked up", Toast.LENGTH_SHORT).show();
                }

                TextView confirmedCases = (TextView) startView.findViewById(R.id.numberConfirmedCases);
                int nCases = Integer.parseInt(confirmedCases.getText().toString());
               // Toast.makeText(getActivity(), "cases: "+txt, Toast.LENGTH_SHORT).show();
                nCases++;
                // TODO: write the incremented value in the db (user)
                // TODO: all bubble values should be added to user!
                // -->  by clicking on the fragment start the values should be pulled and inserted!

                // go back to FragmentStart
                Intent i  = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0,0);
            }


        });

        // Inflate the layout for this fragment
        return view;
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


}
