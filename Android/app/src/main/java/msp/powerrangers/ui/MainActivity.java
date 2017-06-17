package msp.powerrangers.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import msp.powerrangers.R;


public class MainActivity extends FragmentActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseUser currentUser = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentLogin fl = new FragmentLogin();
        fl.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fragment_container, fl).commit();

        //create login/register fragment, if no user is signed in or creates tabs with start screen fragment if a user is signed in
        firebaseAuth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.v("MainActivity", "state changed");
                //FirebaseUser user = firebaseAuth.getCurrentUser();
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null){
                    changeToLoggedInView();
                }
            }
        };

}

    private void changeToLoggedInView() {
        FragmentTabs ft = new FragmentTabs();
        ft.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fragment_container, ft).commit();
    }


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
    }
}
