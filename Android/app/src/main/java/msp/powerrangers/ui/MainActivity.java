package msp.powerrangers.ui;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import msp.powerrangers.R;
import msp.powerrangers.logic.User;
import msp.powerrangers.ui.listitems.ConfirmerCasesListItem;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseUser currentUser = null;
    private User user;

    FragmentLogin fl;

    private List<ConfirmerCasesListItem> dataConfirmerCases;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl = new FragmentLogin();
        fl.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fragment_container, fl).commit();

        //create login/register fragment, if no firebaseUser is signed in or creates tabs with start screen fragment if a firebaseUser is signed in
        firebaseAuth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    changeToLoggedInView();
                }
            }
        };

    }

    private void changeToLoggedInView() {
        //create the waiting fragment
        //let LoginFragment instantiate the user -> extra methode, übergebe waiting fragment
        //hole restliche user daten
        //lasse login view das waiting fragment benachrichtigen, welches den login macht
        fl.createNewUserObject(currentUser, null, null, null, null);

    }


    public void setDataConfirmerCases(List<ConfirmerCasesListItem> data) {
        this.dataConfirmerCases = data;
    }

    public List<ConfirmerCasesListItem> getDataConfirmerCases() {
        return dataConfirmerCases;
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
