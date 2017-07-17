package msp.powerrangers.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import msp.powerrangers.R;

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
            transaction.commit();
            getChildFragmentManager().executePendingTransactions();
        } else if (tag.equals(getString(R.string.votingTasksTag))) {
            FragmentVotingTasks fragmentVotingTasks = new FragmentVotingTasks();
            transaction.replace(R.id.container_framelayout, fragmentVotingTasks);
            Bundle bundles = new Bundle();
            bundles.putSerializable(getString(R.string.votingTasksSerializable), fragmentWait.getVotingTasksListItem());
            bundles.putString("tag", tag);
            fragmentVotingTasks.setArguments(bundles);
            transaction.commit();
            getChildFragmentManager().executePendingTransactions();
        } else if (tag.equals(getString(R.string.rangerTasksTag))) {
            FragmentRangerTasks fragmentRangerTasks = new FragmentRangerTasks();
            transaction.replace(R.id.container_framelayout, fragmentRangerTasks);
            Bundle bundles = new Bundle();
            bundles.putSerializable(getString(R.string.rangerTasksSerializable), fragmentWait.getRangerTasksListItem());
            bundles.putString("tag", tag);
            fragmentRangerTasks.setArguments(bundles);
            transaction.commit();
            getChildFragmentManager().executePendingTransactions();
        } else if (tag.equals(getString(R.string.startTag))) {
            //TODO go back to original fragment instead of creating new?
            FragmentStart fragmentStart = new FragmentStart();
            transaction.replace(R.id.container_framelayout, fragmentStart);
            Bundle bundles = new Bundle();
            //bundles.putSerializable(getString(R.string.tabHostSerializable), this);
            fragmentStart.setArguments(bundles);
            transaction.commit();
            getChildFragmentManager().executePendingTransactions();
        } else if (tag.equals(getString(R.string.usersOpenTasks))) {
            //TODO go back to original fragment instead of creating new?
            FragmentUsersOpenTasks fragmentUsersOpenTasks = new FragmentUsersOpenTasks();
            transaction.replace(R.id.container_framelayout, fragmentUsersOpenTasks);
            Bundle bundles = new Bundle();
            //bundles.putSerializable(getString(R.string.tabHostSerializable), this);
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


    public boolean popFragment() {
        Log.e("test", "pop fragment: " + getChildFragmentManager().getBackStackEntryCount());
        boolean isPop = false;
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getChildFragmentManager().popBackStack();
        }
        return isPop;
    }

}