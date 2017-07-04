package msp.powerrangers.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
            getSupportFragmentManager().beginTransaction().add(R.id.activity_detail_container, f).commit();
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
            getSupportFragmentManager().beginTransaction().add(R.id.activity_detail_container, f).commit();
        }


    }
}
