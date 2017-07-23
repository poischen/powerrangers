package msp.powerrangers.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuInflater;
import java.io.Serializable;
import msp.powerrangers.R;
import msp.powerrangers.logic.User;

public class FragmentTabs extends FragmentActivity implements Serializable {
    private FragmentTabHost tabHost;
    private User user;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) getIntent().getExtras().get(getString(R.string.intent_current_user));
        setContentView(R.layout.fragment_tabs);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    private void initView() {
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(getBaseContext(), getSupportFragmentManager(), android.R.id.tabcontent);

        Bundle arg1 = new Bundle();
        arg1.putSerializable(getString(R.string.intent_current_user), user);
        tabHost.addTab(tabHost.newTabSpec(getString(R.string.startTag)).setIndicator(getString(R.string.start)),
                FragmentWait.class, arg1);

        Bundle arg2 = new Bundle();
        //arg2.putSerializable(getString(R.string.tabHostSerializable), this);
        tabHost.addTab(tabHost.newTabSpec(getString(R.string.votingTasksTag)).setIndicator(getString(R.string.confirmTasks)),
                FragmentWait.class, arg2);

        Bundle arg3 = new Bundle();
        //arg3.putSerializable(getString(R.string.tabHostSerializable), this);
        tabHost.addTab(tabHost.newTabSpec(getString(R.string.confirmCasesTag)).setIndicator(getString(R.string.confirmCases)),
                FragmentWait.class, arg3);

        Bundle arg4 = new Bundle();
        //arg4.putSerializable(getString(R.string.tabHostSerializable), this);
        tabHost.addTab(tabHost.newTabSpec(getString(R.string.rangerTasksTag)).setIndicator(getString(R.string.findYorJob)),
                FragmentWait.class, arg4);

        // set the width of Confirm-Tab (else the text appears over two lines)
        tabHost.getTabWidget().getChildAt(2).getLayoutParams().width = 80;

    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}