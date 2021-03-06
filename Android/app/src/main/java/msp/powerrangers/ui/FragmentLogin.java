package msp.powerrangers.ui;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import msp.powerrangers.R;
import msp.powerrangers.model.User;


/** Login screen, where a user can fLogin_Register or login via his email
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

        /*set OnClick Listener on Button for registering unregistered users via email or sign in registered users
         * A user can only fLogin_Register, if he enters his mailaddress, a password, and a name (analog sign in only with both mailadress and password)
         */
        buttonRegisterSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = editTextMail.getText().toString().trim();
                String password = editTextPassword.getText().toString();
                String name = editTextName.getText().toString().trim();
                if (TextUtils.isEmpty(mail)){
                    Toast.makeText(getActivity(), "Please enter your mail for da real power! :)", Toast.LENGTH_SHORT).show();
                } if (TextUtils.isEmpty((password))){
                    Toast.makeText(getActivity(), "Please enter a password", Toast.LENGTH_SHORT).show();
                }
                //see, if the user wants to fLogin_Register or to sign in
                else if (buttonRegisterSignin.getText().equals(R.string.fLogin_Signin)){
                    signIn(mail, password);
                } else if (TextUtils.isEmpty((name))){
                    Toast.makeText(getActivity(), "Nah, nah! No name, no power, sorry!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(mail, password, name);
                }
            }
        });

        /* set OnClick Listener on TextView for sign in via email
         * user can only sign in, if he enters both his mailaddress and a password
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

    /** Method for registering the user via Firebase
     * after the user registers, he will be written into the database
     * @param mail String
     * @param password String
     * @param name String
     **/
    public void registerUser(String mail, String password, final String name){
        //receiving progress feedback while registering
        progressDialog.setMessage("Registering the power...");
        progressDialog.show();
        final String currentName = name;
        final String currentMail = mail;

        firebaseAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //user is successfully registered
                            Toast.makeText(getActivity(), "Power registered successfully!", Toast.LENGTH_SHORT).show();

                            //store name of the user in Firebase Auth
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

                            //write user into the database
                            //Tutorial for database: http://www.androidhive.info/2016/10/android-working-with-firebase-realtime-database/
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                            String dbId = database.push().getKey();
                            String userId = currentuser.getUid();
                            User user = new User(currentName, dbId, userId, currentMail);
                            // pushing user to 'users' node using the userId
                            database.child(dbId).setValue(user);

                            //switch to Start
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

    /** Method for signing in via email and Firebase
     * @param mail String
     * @param password String
     **/
    public void signIn(String mail, String password){
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

    /** This method changes the current view between fLogin_Register and fLogin_Signin modus
     * @param command String text value from the textEditSwitchRegisterSignin
     */
    public void reverseView(String command){
       if (command.equals(getString(R.string.fLogin_AlreadyRegistered))){
           Log.v("FragmentLogin", "switch view to sign in");
           buttonRegisterSignin.setText(R.string.fLogin_Signin);
           editTextName.setVisibility(View.GONE);
           textViewSwitchRegisterSignin.setText(R.string.fLogin_NotRegisteredYet);
       } else if (command.equals(getString(R.string.fLogin_NotRegisteredYet))){
           Log.v("FragmentLogin", "switch view to register");
           buttonRegisterSignin.setText(R.string.fLogin_Register);
           editTextName.setVisibility(View.VISIBLE);
           textViewSwitchRegisterSignin.setText(R.string.fLogin_AlreadyRegistered);
       }

    }

}
