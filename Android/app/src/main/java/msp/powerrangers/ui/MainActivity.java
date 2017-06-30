package msp.powerrangers.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import msp.powerrangers.R;
import msp.powerrangers.ui.listitems.FragmentConfirmerCasesListItem;
import msp.powerrangers.ui.listitems.FragmentRangerTasksListItem;
import msp.powerrangers.ui.listitems.FragmentUsersOpenTasksListItem;
import msp.powerrangers.ui.listitems.FragmentVotingTasksListItem;


public class MainActivity extends FragmentActivity implements FragmentConfirmerCases.OnListFragmentInteractionListener, FragmentRangerTasks.OnListFragmentInteractionListener, FragmentUsersOpenTasks.OnListFragmentInteractionListener, FragmentVotingTasks.OnListFragmentInteractionListener{

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
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentTabs ft = new FragmentTabs();
        fragmentTransaction.replace(R.id.activity_main_fragment_container, ft);
        fragmentTransaction.commit();
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
    public void onListFragmentInteraction(FragmentConfirmerCasesListItem.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(FragmentRangerTasksListItem.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(FragmentUsersOpenTasksListItem.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(FragmentVotingTasksListItem.DummyItem item) {

    }
}
