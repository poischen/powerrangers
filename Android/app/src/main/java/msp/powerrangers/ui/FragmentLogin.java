package msp.powerrangers.ui;
import android.app.ProgressDialog;
<<<<<<< HEAD
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
=======
import android.content.SharedPreferences;
>>>>>>> ui
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import msp.powerrangers.R;


/** Login screen, where a user can register or login via his email
 * Auth via Firebase
 */
public class FragmentLogin extends Fragment {
    private EditText editTextMail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
<<<<<<< HEAD
=======
    private User user;
>>>>>>> ui

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //getActivity().getActionBar().hide();
        //getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33000000")));

        firebaseAuth = FirebaseAuth.getInstance();

        //find UI elements
        editTextMail = (EditText) view.findViewById(R.id.editTextMail);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);
        buttonRegister = (Button) view.findViewById(R.id.buttonRegister);
        textViewSignin = (TextView) view.findViewById(R.id.textViewSignin);
        progressDialog = new ProgressDialog(getContext());

        /*set OnClick Listener on Button for registering unregistered users via email
         * A user can only register, if he enters both his mailaddress and a password
         */
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = editTextMail.getText().toString().trim();
                String password = editTextPassword.getText().toString();
                if (TextUtils.isEmpty(mail)){
                    Toast.makeText(getActivity(), "Please enter your mail for da real power! :)", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty((password))){
                    Toast.makeText(getActivity(), "Please enter a password", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(mail, password);
                }
            }
        });

        /* set OnClick Listener on TextView for sign in via email
         * user can only sign in, if he enters both his mailaddress and a password
         */
        textViewSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = editTextMail.getText().toString().trim();
                String password = editTextPassword.getText().toString();
                if (TextUtils.isEmpty(mail)){
                    Toast.makeText(getActivity(), "Please enter your mail for da real power! :)", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty((password))){
                    Toast.makeText(getActivity(), "Please enter your password", Toast.LENGTH_SHORT).show();
                } else {
                    signIn(mail, password);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
    }

    /** Method for registering the user via Firebase
     * @param mail String
     * @param password String
     **/
    public void registerUser(String mail, String password){
        //receiving progress feedback while registering
        progressDialog.setMessage("Registering the power...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //user is successfully registered
                            Toast.makeText(getActivity(), "Power registered successfully!", Toast.LENGTH_SHORT).show();
<<<<<<< HEAD
                            //switch to App
                            //TODO: ask for nickname and set this name before switching View
                            FragmentTabs ft = new FragmentTabs();
                            ft.setArguments(getActivity().getIntent().getExtras());
                            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fragment_container, ft).commit();
=======
                            //store name of the firebaseUser in Firebase Auth
                            FirebaseUser currentuser = firebaseAuth.getCurrentUser();
                            if (currentuser != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();
                                currentuser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                }
                                            }
                                        });
                            }


                            //write firebaseUser into the database
                            //Tutorial for database: http://www.androidhive.info/2016/10/android-working-with-firebase-realtime-database/
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                            String dbId = database.push().getKey();
                            String userId = currentuser.getUid();

                            //save users db key in shared preferances
                            SharedPreferences sharedPrefs = getContext().getSharedPreferences(getResources().getString(R.string.sharedPrefs_userDbIdPrefname), 0);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.putString(getResources().getString(R.string.sharedPrefs_userDbId), dbId);
                            editor.commit();
                            //create new user object
                            user = new User(dbId, userId, currentName, currentMail);
                            // pushing firebaseUser to 'users' node using the userId
                            database.child(dbId).setValue(user);
                            Log.i("THE USER ID" , user.getId());

                            Log.i("The USER" , user.toString());

                            //switch to Start

                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            FragmentStart fragmentStart = new FragmentStart();


                            Bundle bundles = new Bundle();

                            if (user != null){
                                bundles.putSerializable("USER" , user);
                                Log.i("USER" , "IS NOT NULL");
                            } else {
                                Log.i("USER" , "IS NULL");
                            }

                            fragmentStart.setArguments(bundles);
                            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fragment_container, fragmentStart).commit();

>>>>>>> ui
                        } else {
                            Toast.makeText(getActivity(), "Noooooooo! Try again!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    /** Method for signing in via email and Firebase
     * @param mail String
     * @param password String
     **/
    public void signIn(String mail, String password){
        final String givenMail = mail;
        //receiving progress feedback while registering
        progressDialog.setMessage("Sign in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "Log in successful!", Toast.LENGTH_SHORT).show();
                            FragmentTabs ft = new FragmentTabs();
                            ft.setArguments(getActivity().getIntent().getExtras());
                            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fragment_container, ft).commit();
                        } else {
                            Toast.makeText(getActivity(), "Noooooooo! Try again!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

}
