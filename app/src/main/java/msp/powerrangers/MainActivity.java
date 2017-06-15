package msp.powerrangers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/** Start screen, where a user can register via his email
 * Auth via Firebase
 */

public class MainActivity extends AppCompatActivity {

    private EditText editTextMail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find UI elements
        editTextMail = (EditText) findViewById(R.id.editTextMail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        /*set OnClick Listener on Button for registering unregistered users via email
         * A user can only register, if he enters both his mailaddress and a password
         */
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = editTextMail.getText().toString().trim();
                String password = editTextPassword.getText().toString();
                if (TextUtils.isEmpty(mail)){
                    Toast.makeText(MainActivity.this, "Please enter your mail for da real power! :)", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty((password))){
                    Toast.makeText(MainActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "Please enter your mail for da real power! :)", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty((password))){
                    Toast.makeText(MainActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                } else {
                    signIn(mail, password);
                }
            }
        });
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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //user is successfully registered
                            Toast.makeText(MainActivity.this, "Power registered successfully!", Toast.LENGTH_SHORT).show();
                            //TODO: switch View
                        } else {
                            Toast.makeText(MainActivity.this, "Noooooooo! Try again!", Toast.LENGTH_SHORT).show();
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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Log in successful!", Toast.LENGTH_SHORT).show();
                            //TODO: switch View
                        } else {
                            Toast.makeText(MainActivity.this, "Noooooooo! Try again!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

}
