package msp.powerrangers.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import msp.powerrangers.ui.listitems.ConfirmerCasesListItem;

public class FragmentTabs extends Fragment implements Serializable {
    private FragmentTabHost tabHost;
        public FragmentTabs() {

        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_tabs,container, false);
            tabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
            tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

            Bundle arg1 = new Bundle();
            arg1.putSerializable("tabhost", this);
            tabHost.addTab(tabHost.newTabSpec(getString(R.string.startTag)).setIndicator(getString(R.string.start)),
                    FragmentStart.class, arg1);

            Bundle arg2 = new Bundle();
            arg2.putSerializable("tabhost", this);
            tabHost.addTab(tabHost.newTabSpec(getString(R.string.votingTasksTag)).setIndicator(getString(R.string.confirmTasks)),
                    FragmentWait.class, arg2);

            Bundle arg3 = new Bundle();
            arg3.putSerializable("tabhost", this);
            tabHost.addTab(tabHost.newTabSpec(getString(R.string.confirmCasesTag)).setIndicator(getString(R.string.confirmCases)),
                    FragmentWait.class, arg3);

            Bundle arg4 = new Bundle();
            arg4.putSerializable("tabhost", this);
            tabHost.addTab(tabHost.newTabSpec(getString(R.string.rangerTasksTag)).setIndicator(getString(R.string.findYorJob)),
                    FragmentWait.class, arg4);

            // set the width of Confirm-Tab (else the text appears over two lines)
            tabHost.getTabWidget().getChildAt(2).getLayoutParams().width = 80;

            return rootView;
        }

    public TabHost getTabHost(){
        return tabHost;
    }

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
    }

}