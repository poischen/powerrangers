package msp.powerrangers.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import msp.powerrangers.R;
import msp.powerrangers.logic.User;

/**
 * Login screen, where a firebaseUser can fLogin_Register or login via his email
 * Auth via Firebase
 */
public class FragmentLogin extends Fragment {
    private EditText editTextMail;
    private EditText editTextPassword;
    private EditText editTextName;
    private Button buttonRegisterSignin;
    private TextView textViewSwitchRegisterSignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private User user;
    private boolean wantsUserToRegister;

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
        editTextName = (EditText) view.findViewById(R.id.editTextName);
        buttonRegisterSignin = (Button) view.findViewById(R.id.buttonRegisterSignin);
        textViewSwitchRegisterSignin = (TextView) view.findViewById(R.id.textViewSignin);
        progressDialog = new ProgressDialog(getContext());
        //set flag to decive if the view shall show the option to register or to signin
        wantsUserToRegister = true;
        /*set OnClick Listener on Button for registering unregistered users via email or sign in registered users
         * A firebaseUser can only fLogin_Register, if he enters his mailaddress, a password, and a name (analog sign in only with both mailadress and password)
         */
        buttonRegisterSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = editTextMail.getText().toString().trim();
                String password = editTextPassword.getText().toString();
                String name = editTextName.getText().toString().trim();
                if (TextUtils.isEmpty(mail)) {
                    Toast.makeText(getActivity(), "Please enter your mail for da real power! :)", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty((password))) {
                    Toast.makeText(getActivity(), "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }
                //see, if the firebaseUser wants to fLogin_Register or to sign in
                if (!wantsUserToRegister) {
                    signIn(mail, password);
                    return;
                } else if (wantsUserToRegister && TextUtils.isEmpty(name)) {
                    Log.v("wantsUserToRegister", wantsUserToRegister + "");
                    Log.v("TextUtils.isEmpty(name)", TextUtils.isEmpty(name) + "");
                    Log.v("name", name + "");
                    Toast.makeText(getActivity(), "Nah, nah! No name, no power, sorry!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    registerUser(mail, password, name);
                }
            }
        });
        /* set OnClick Listener on TextView for sign in via email
         * firebaseUser can only sign in, if he enters both his mailaddress and a password
         */
        textViewSwitchRegisterSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reverseView(textViewSwitchRegisterSignin.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
    }

    /**
     * Method for registering the firebaseUser via Firebase
     * after the firebaseUser registers, he will be written into the database
     *
     * @param mail     String
     * @param password String
     * @param name     String
     **/
    public void registerUser(String mail, String password, final String name) {
        //receiving progress feedback while registering
        progressDialog.setMessage("Registering the power...");
        progressDialog.show();
        final String currentName = name;
        final String currentMail = mail;
        firebaseAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //firebaseUser is successfully registered
                            Toast.makeText(getActivity(), "Power registered successfully!", Toast.LENGTH_SHORT).show();
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
                            //user = new User(dbId, userId, currentName, currentMail);
                            // pushing firebaseUser to 'users' node using the userId
                            database.child(dbId).setValue(createNewUserObject(null, dbId, userId, currentName, currentMail));
                            //switch to Start
                            /*FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
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
                        } else {
                            Toast.makeText(getActivity(), "Noooooooo! Try again!", Toast.LENGTH_SHORT).show();
                        }*/
                            /*FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            FragmentTabs fragmentTabs = new FragmentTabs();
                            Bundle bundles = new Bundle();
                            if (user != null) {
                                bundles.putSerializable("USER", user);
                                Log.i("USER", "IS NOT NULL");
                            } else {
                                Log.i("USER", "IS NULL");
                            }
                            fragmentTabs.setArguments(bundles);
                            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fragment_container, fragmentTabs).commit();*/
                        } else {
                            Toast.makeText(getActivity(), "Noooooooo! Try again!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    /**
     * Method for signing in via email and Firebase
     *
     * @param mail     String
     * @param password String
     **/
    public void signIn(String mail, String password) {
        final String givenMail = mail;
        //receiving progress feedback while registering
        progressDialog.setMessage("Sign in...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Log in successful!", Toast.LENGTH_SHORT).show();

                            FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();

                            createNewUserObject(firebaseUser, null, null, null, null);

                            /*FragmentTabs ft = new FragmentTabs();
                            ft.setArguments(getActivity().getIntent().getExtras());
                            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fragment_container, ft).commit();*/
                        } else {
                            Toast.makeText(getActivity(), "Noooooooo! Try again!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    /*
    Create a new user object, create a tabhost and give the user to the tabhost, which stores it for all concerning fragments
     */
    public User createNewUserObject(FirebaseUser firebaseUser, String dbId, String userId, String currentName, String currentMail) {
        //if called by login
        if (firebaseUser != null) {
            SharedPreferences sharedPrefs = getContext().getSharedPreferences(getResources().getString(R.string.sharedPrefs_userDbIdPrefname), 0);
            final String userDbID = sharedPrefs.getString(getResources().getString(R.string.sharedPrefs_userDbId), null);
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            DatabaseReference refPath = db.child("users").child(userDbID);
            refPath.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        User userInfo = dataSnapshot.getValue(User.class);
                        String name = userInfo.getName();
                        String dbId = userInfo.getDbId();
                        String userId = userInfo.getId();
                        String mail = userInfo.getEmail();
                        user = new User(dbId, userId, name, mail);
                        ((MainActivity)getActivity()).setUser(user);
                        FragmentTabs ft = new FragmentTabs();
                        //Bundle bundle = new Bundle();
                        //bundle.putSerializable(getString(R.string.intent_current_user), user);
                        //ft.setArguments(bundle);

                        Intent intent = new Intent(getActivity(), FragmentTabs.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(getString(R.string.intent_current_user), user);
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);

                       /* FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.activity_main_fragment_container, ft, "tabHostFragment");
                        transaction.addToBackStack(null);
                        transaction.commit();
                        Log.v("FragmentLogin", "TAG: " + ft.getTag());*/

                    } catch (Exception e) {
                        Log.d("FragmentStart", "An error occured, user has to be signed out");
                        FirebaseAuth.getInstance().signOut();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            return user;
        } else {
            //if called by register
            user = new User(dbId, userId, currentName, currentMail);

            Intent intent = new Intent(getActivity(), FragmentTabs.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.intent_current_user), user);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);

            /*FragmentTabs ft = new FragmentTabs();
            Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.intent_current_user), user);
            ft.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.activity_main_fragment_container, ft);
            transaction.addToBackStack(null);
            transaction.commit();*/

            return user;

        }
    }


    /**
     * This method changes the current view between fLogin_Register and fLogin_Signin modus
     *
     * @param command String text value from the textEditSwitchRegisterSignin
     */
    public void reverseView(String command) {
        if (command.equals(getString(R.string.fLogin_AlreadyRegistered))) {
            wantsUserToRegister = false;
            Log.v("FragmentLogin", "switch view to sign in");
            buttonRegisterSignin.setText(R.string.fLogin_Signin);
            editTextName.setVisibility(View.GONE);
            textViewSwitchRegisterSignin.setText(R.string.fLogin_NotRegisteredYet);
        } else if (command.equals(getString(R.string.fLogin_NotRegisteredYet))) {
            wantsUserToRegister = true;
            Log.v("FragmentLogin", "switch view to register");
            buttonRegisterSignin.setText(R.string.fLogin_Register);
            editTextName.setVisibility(View.VISIBLE);
            textViewSwitchRegisterSignin.setText(R.string.fLogin_AlreadyRegistered);
        }
    }
}