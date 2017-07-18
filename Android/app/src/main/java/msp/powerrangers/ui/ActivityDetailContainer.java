package msp.powerrangers.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import msp.powerrangers.R;

public class ActivityDetailContainer extends AppCompatActivity {

    private String target;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_container);

        Bundle  bundle = getIntent().getExtras();
        target = bundle.getString(String.valueOf(R.string.activityDetailContainer_targetFr));

        // open details about RangerTask
        if (target.equals(getString(R.string.targetFrDetailRangerTask))) {
            FragmentDetailRangerTask f = new FragmentDetailRangerTask();
            f.setArguments(getIntent().getExtras());
            //getSupportFragmentManager().beginTransaction().add(R.id.activity_detail_container, f).commit();
            FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
            frag.add(R.id.activity_detail_container, f).commit();
            frag.addToBackStack(null);
        }

        // open details about UsersOpenTask
        else if (target.equals(getString(R.string.targetFrDetailUsersOpenTask))) {
            FragmentDetailUsersOpenTask f = new FragmentDetailUsersOpenTask();
            f.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.activity_detail_container, f).commit();
        }

        // open details about ConfirmerCase
        else if (target.equals(getString(R.string.targetFrDetailConfirmerCase))) {
            FragmentDetailConfirmerCase f = new FragmentDetailConfirmerCase();
            f.setArguments(getIntent().getExtras());
            FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
            frag.add(R.id.activity_detail_container, f).commit();
            frag.addToBackStack(null);
        }


     }

    // Set action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.fragment_details_ranger_tasks, menu);

        if (getActionBar() != null) {
            getActionBar().setHomeButtonEnabled(false); // disable the button
            getActionBar().setDisplayHomeAsUpEnabled(true); // remove the left caret
            getActionBar().setDisplayShowHomeEnabled(true); // remove the icon
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }


}
