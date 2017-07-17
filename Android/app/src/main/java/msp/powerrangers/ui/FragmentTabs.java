package msp.powerrangers.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import java.io.Serializable;
import java.util.List;

import msp.powerrangers.R;
import msp.powerrangers.logic.User;
import msp.powerrangers.ui.listitems.ConfirmerCasesListItem;

public class FragmentTabs extends FragmentActivity implements Serializable {
    private FragmentTabHost tabHost;
    private User user;

      /*  public FragmentTabs() {

        }*/

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            user = (User) getIntent().getExtras().get(getString(R.string.intent_current_user));
            setContentView(R.layout.fragment_tabs);
            initView();
        }

   /*     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_tabs,container, false);
            Log.v("FragmentTabs", "layout inflated " + rootView.getLayoutParams());
            tabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
            tabHost.setup(getBaseContext(), getSupportFragmentManager(), android.R.id.tabcontent);

            Bundle arg1 = new Bundle();
            arg1.putSerializable(getString(R.string.intent_current_user), user);
            tabHost.addTab(tabHost.newTabSpec(getString(R.string.startTag)).setIndicator(getString(R.string.start)),
                    FragmentStart.class, arg1);

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

            return rootView;
        }*/

    /*public TabHost getTabHost(){
        return tabHost;
    }*/


    private void initView() {
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(getBaseContext(), getSupportFragmentManager(), android.R.id.tabcontent);

        Bundle arg1 = new Bundle();
        arg1.putSerializable(getString(R.string.intent_current_user), user);
        tabHost.addTab(tabHost.newTabSpec(getString(R.string.startTag)).setIndicator(getString(R.string.start)),
                FragmentStart.class, arg1);

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

/*
    public void replaceFragment(String tag, boolean addToBackStack, FragmentWait fragmentWait) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        if (tag.equals(getString(R.string.confirmCasesTag))){
            FragmentConfirmerCases confirmerCasesFragment = new FragmentConfirmerCases();
            transaction.replace(android.R.id.tabcontent, confirmerCasesFragment);
            Bundle bundles = new Bundle();
            bundles.putSerializable(getString(R.string.confirmCasesSerializable), fragmentWait.getCasesListItem());
            bundles.putString("tag", tag);
            confirmerCasesFragment.setArguments(bundles);
            transaction.commit();
            getChildFragmentManager().executePendingTransactions();
        }
        else if (tag.equals(getString(R.string.votingTasksTag))){
            FragmentVotingTasks fragmentVotingTasks = new FragmentVotingTasks();
            transaction.replace(android.R.id.tabcontent, fragmentVotingTasks);
            Bundle bundles = new Bundle();
            bundles.putSerializable(getString(R.string.votingTasksSerializable), fragmentWait.getVotingTasksListItem());
            bundles.putString("tag", tag);
            fragmentVotingTasks.setArguments(bundles);
            transaction.commit();
            getChildFragmentManager().executePendingTransactions();
        }
        else if (tag.equals(getString(R.string.rangerTasksTag))){
            FragmentRangerTasks fragmentRangerTasks = new FragmentRangerTasks();
            transaction.replace(android.R.id.tabcontent, fragmentRangerTasks);
            Bundle bundles = new Bundle();
            bundles.putSerializable(getString(R.string.rangerTasksSerializable), fragmentWait.getRangerTasksListItem());
            bundles.putString("tag", tag);
            fragmentRangerTasks.setArguments(bundles);
            transaction.commit();
            getChildFragmentManager().executePendingTransactions();
        }
        else if (tag.equals(getString(R.string.startTag))){
            //TODO go back to original fragment instead of creating new?
            FragmentStart fragmentStart = new FragmentStart();
            transaction.replace(android.R.id.tabcontent, fragmentStart);
            Bundle bundles = new Bundle();
            //bundles.putSerializable(getString(R.string.tabHostSerializable), this);
            fragmentStart.setArguments(bundles);
            transaction.commit();
            getChildFragmentManager().executePendingTransactions();
        }

        else if (tag.equals(getString(R.string.usersOpenTasks))){
            //TODO go back to original fragment instead of creating new?
            FragmentUsersOpenTasks fragmentUsersOpenTasks = new FragmentUsersOpenTasks();
            transaction.replace(android.R.id.tabcontent, fragmentUsersOpenTasks);
            Bundle bundles = new Bundle();
            //bundles.putSerializable(getString(R.string.tabHostSerializable), this);
            fragmentUsersOpenTasks.setArguments(bundles);
            transaction.commit();
            getChildFragmentManager().executePendingTransactions();
        }

    }

    public boolean popFragment() {
        Log.e("test", "pop fragment: " + getChildFragmentManager().getBackStackEntryCount());
        boolean isPop = false;
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getChildFragmentManager().popBackStack();
        }
        return isPop;
    }*/

}