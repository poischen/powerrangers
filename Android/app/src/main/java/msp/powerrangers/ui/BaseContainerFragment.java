package msp.powerrangers.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.io.Serializable;

import msp.powerrangers.R;
import msp.powerrangers.ui.listitems.UsersOpenTasksListItem;

public class BaseContainerFragment extends Fragment {

    public void replaceFragment(String tag, boolean addToBackStack, FragmentWait fragmentWait) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        if (tag.equals(getString(R.string.confirmCasesTag))) {
            FragmentConfirmerCases confirmerCasesFragment = new FragmentConfirmerCases();
            transaction.replace(R.id.container_framelayout, confirmerCasesFragment);
            Bundle bundles = new Bundle();
            bundles.putSerializable(getString(R.string.confirmCasesSerializable), fragmentWait.getCasesListItem());
            bundles.putString("tag", tag);
            confirmerCasesFragment.setArguments(bundles);
            transaction.commitAllowingStateLoss();
            getChildFragmentManager().executePendingTransactions();
        } else if (tag.equals(getString(R.string.votingTasksTag))) {
            FragmentVotingTasks fragmentVotingTasks = new FragmentVotingTasks();
            transaction.replace(R.id.container_framelayout, fragmentVotingTasks);
            Bundle bundles = new Bundle();
            bundles.putSerializable(getString(R.string.votingTasksSerializable), fragmentWait.getVotingTasksListItem());
            bundles.putString("tag", tag);
            fragmentVotingTasks.setArguments(bundles);
            transaction.commitAllowingStateLoss();
            getChildFragmentManager().executePendingTransactions();
        } else if (tag.equals(getString(R.string.rangerTasksTag))) {
            FragmentRangerTasks fragmentRangerTasks = new FragmentRangerTasks();
            transaction.replace(R.id.container_framelayout, fragmentRangerTasks, getString(R.string.usersOpenTasks));
            Bundle bundles = new Bundle();
            bundles.putSerializable(getString(R.string.rangerTasksSerializable), fragmentWait.getRangerTasksListItem());
            bundles.putString("tag", tag);
            fragmentRangerTasks.setArguments(bundles);
            transaction.commit();
            getChildFragmentManager().executePendingTransactions();
        } else if (tag.equals(getString(R.string.startTag))) {
            FragmentStart fragmentStart = new FragmentStart();
            transaction.replace(R.id.container_framelayout, fragmentStart);
            Bundle bundles = new Bundle();
            //bundles.putSerializable(getString(R.string.tabHostSerializable), this);
            fragmentStart.setArguments(bundles);
            transaction.commit();
            getChildFragmentManager().executePendingTransactions();
        } else if (tag.equals(getString(R.string.usersOpenTasks))) {
            Log.v("BaseContainer", "replace Fragment, UsersOpenTasks");
            FragmentUsersOpenTasks fragmentUsersOpenTasks = new FragmentUsersOpenTasks();
            transaction.replace(R.id.container_framelayout, fragmentUsersOpenTasks);
            Bundle bundles = new Bundle();
            bundles.putSerializable(getString(R.string.openTasksSerializable), fragmentWait.getUsersOpenTasksListItem());
            Log.v("BaseContainer", "data: " + fragmentWait.getUsersOpenTasksListItem());
            fragmentUsersOpenTasks.setArguments(bundles);
            transaction.commit();
            getChildFragmentManager().executePendingTransactions();
        }


    }


    public void replaceFragmentDetailRanger(FragmentDetailRangerTask fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container_framelayout, fragment);
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public void replaceFragmentDetailVoting(FragmentDetailVotingTask fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container_framelayout, fragment);
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public void replaceFragmentDetailOpenTask(FragmentDetailUsersOpenTask fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container_framelayout, fragment);
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public void replaceFragmentDetailConfirmer(FragmentDetailConfirmerCase fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container_framelayout, fragment);
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public void replaceFragmentOpenTasks(FragmentUsersOpenTasks fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container_framelayout, fragment, (getString(R.string.usersOpenTasks)));
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public void replaceFragmentWait(FragmentWait fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container_framelayout, fragment, (getString(R.string.fragmentWaitTag)));
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public boolean popFragment() {
        Log.e("test", "pop fragment: " + getChildFragmentManager().getBackStackEntryCount());
        boolean isPop = false;
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getChildFragmentManager().popBackStack();
        }
        return isPop;
    }


    public void replaceFragmentUOT(FragmentWait fw){
        if (!isAdded()) return;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Log.v("BaseContainer", "replace Fragment, UsersOpenTasks");
        FragmentUsersOpenTasks fragmentUsersOpenTasks = new FragmentUsersOpenTasks();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container_framelayout, fragmentUsersOpenTasks);
        Bundle bundles = new Bundle();
        Log.i("Viki replaceFragmentUOT", "vor put serializable");
        bundles.putSerializable(getString(R.string.openTasksSerializable), fw.getUsersOpenTasksListItem());
        Log.i("Viki replaceFragmentUOT", "nach put serializable");
        Log.v("BaseContainer", "data: " + fw.getUsersOpenTasksListItem());
        fragmentUsersOpenTasks.setArguments(bundles);
        transaction.commitAllowingStateLoss();
        getChildFragmentManager().executePendingTransactions();
    }

}