package msp.powerrangers.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

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

    public FragmentDetailConfirmerCase() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fr_detail_confirmer_case, container, false);

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



        // Inflate the layout for this fragment
        return view;
    }


}
