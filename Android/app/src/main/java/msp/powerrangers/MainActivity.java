package msp.powerrangers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends FragmentActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        //create login/register fragment, if no user is signed in or creates tabs with start screen fragment if a user is signed in
      /* Log.v("MainActivity", "1");
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.v("MainActivity", "state changed");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    if (findViewById(R.id.activity_main_fragment_container) != null){
                        if (savedInstanceState != null)
                            Log.v("MainActivity", "1a");

                            return;
                    }
                    else {
                        Log.v("MainActivity", "User is not logged in");
                        FragmentLogin fl = new FragmentLogin();
                        fl.setArguments(getIntent().getExtras());
                        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fragment_container, fl).commit();
                    }
                } else {
                    // User is signed out
                    if (findViewById(R.id.activity_main_fragment_container) != null){
                        if (savedInstanceState != null){
                            Log.v("MainActivity", "2a");

                            return;
                        }
                    } else {
                        Log.v("MainActivity", "User is logged in");
                        FragmentTabs ft = new FragmentTabs();
                        ft.setArguments(getIntent().getExtras());
                        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fragment_container, ft).commit();
                    }
                }
            }
        };*/

        FragmentTabs ft = new FragmentTabs();
        ft.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fragment_container, ft).commit();


    }
/*
    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            firebaseAuth.removeAuthStateListener(authListener);
        }
    }*/

}
