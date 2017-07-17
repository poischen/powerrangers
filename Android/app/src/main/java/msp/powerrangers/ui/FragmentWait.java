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

import msp.powerrangers.R;
import msp.powerrangers.ui.listitems.ConfirmerCasesListItem;
import msp.powerrangers.ui.listitems.RangerTasksListItem;
import msp.powerrangers.ui.listitems.UsersOpenTasksListItem;
import msp.powerrangers.ui.listitems.VotingTasksListItem;


public class FragmentWait extends BaseContainerFragment {


    private ConfirmerCasesListItem casesListItem;
    private RangerTasksListItem rangerTasksListItem;
    private UsersOpenTasksListItem usersOpenTasksListItem;
    private VotingTasksListItem votingTasksListItem;
    private UsersOpenTasksListItem usersOpenTaksListItem;

    private boolean votingTabClicked = false;
    private boolean confirmerTabClicked = false;
    private boolean rangerTabClicked = false;
    private boolean openTasksClicked = false;

    //private FragmentTabs fragmentTabs;

    public FragmentWait() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Bundle bundle = getArguments();
        //fragmentTabs = (FragmentTabs) bundle.getSerializable("tabhost");

        String tag = getTag();
        Log.i("KATJA", "**********************   new tag  ********************* ");
        Log.i("KATJA", "tag: " + tag);

        String startTag = getString(R.string.startTag);
        String votingTag = getString(R.string.votingTasksTag);
        String confirmerTag = getString(R.string.confirmCasesTag);
        String rangerTag = getString(R.string.rangerTasksTag);
        String openTaskTag = getString(R.string.usersOpenTasks);

        // check which tab was clicked and get the data from the db


        if (tag.equals(votingTag)) {
            votingTasksListItem = new VotingTasksListItem();
            votingTasksListItem.fill_with_data(this);
            votingTabClicked = true;
        }

        if (tag.equals(confirmerTag)) {
            Log.i("KATJA"," tag equals confirmerTag");
            casesListItem = new ConfirmerCasesListItem();
            casesListItem.fill_with_data(this);
            confirmerTabClicked = true;
            Log.i("KATJA"," confirmerTabClicked set to true");

        }

        if (tag.equals(rangerTag)) {
            Log.i("KATJA"," tag equals rangerTag");
            rangerTasksListItem = new RangerTasksListItem();
            rangerTasksListItem.fill_with_data(this);
            rangerTabClicked = true;
            Log.i("KATJA"," rangerTabClicked set to true");
        }

        if (tag.equals(openTaskTag)) {
            usersOpenTaksListItem = new UsersOpenTasksListItem();
            usersOpenTaksListItem.fill_with_data(this, ((FragmentTabs)getActivity()).getUser().getId());
            openTasksClicked = true;
        }

       if (tag.equals(startTag)) {
            replaceFragment(startTag, true, this);
        }

    }

    public void changeToContentView(boolean isaboutUsersOpenTasks) {
        String tag;
        if (isaboutUsersOpenTasks){
            Log.v("FragmentWait", "it is about UsersOpenTasks!");
            tag = (getString(R.string.usersOpenTasks));
        } else {
            tag = getTag();
        }

        /*Log.v("FragmentWait", "Fragments: " + getFragmentManager().getFragments());

        FragmentTabs tabHost = (FragmentTabs) getFragmentManager().findFragmentByTag(getString(R.string.fragmentTabsTag));
        Log.v("TABHOST ", ""+ tabHost);*/

        replaceFragment(tag, true, this);

        /*if (confirmerTabClicked){
            Log.i("KATJA","confirmerTabClicked in FragmentWait");
            confirmerTabClicked = false;
            fragmentTabs.replaceFragment(tag, true, this);

           FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

            Bundle bundles = new Bundle();
            bundles.putSerializable(getString(R.string.confirmCasesSerializable), casesListItem);
            confirmerCasesFragment.setArguments(bundles);
            Log.i("KATJA","putSerializable in FragmentWait");

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.replace(R.id.activity_main_fragment_container, confirmerCasesFragment);
            ft.addToBackStack(null);
            ft.commit();

        } else if (votingTabClicked){
            votingTabClicked = false;
            fragmentTabs.replaceFragment(tag, true, this);

            FragmentVotingTasks fragmentVotingTasks = new FragmentVotingTasks();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

            Bundle bundles = new Bundle();
            bundles.putSerializable(getString(R.string.votingTasksSerializable), votingTasksListItem);
            fragmentVotingTasks.setArguments(bundles);

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.replace(R.id.activity_main_fragment_container, fragmentVotingTasks);
            ft.addToBackStack(null);
            ft.commit();

        } else if (rangerTabClicked){
            Log.i("KATJA","rangerTabClicked in FragmentWait");
            FragmentRangerTasks fragmentRangerTasks = new FragmentRangerTasks();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

            Bundle bundles = new Bundle();
            bundles.putSerializable(getString(R.string.rangerTasksSerializable), rangerTasksListItem);
            fragmentRangerTasks.setArguments(bundles);
            Log.i("KATJA","putSerializable in FragmentWait");

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.replace(R.id.activity_main_fragment_container, fragmentRangerTasks);
            ft.addToBackStack(null);
            ft.commit();

        }*/
    }

    public void initReplacingFragmentOpenTasks(UsersOpenTasksListItem fragment, String userID){
        fragment.fill_with_data(this, userID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fr_wait, null);
    }

    public ConfirmerCasesListItem getCasesListItem() {
        return casesListItem;
    }

    public RangerTasksListItem getRangerTasksListItem() {
        return rangerTasksListItem;
    }

    public UsersOpenTasksListItem getUsersOpenTasksListItem() {
        return usersOpenTasksListItem;
    }

    public VotingTasksListItem getVotingTasksListItem() {
        return votingTasksListItem;
    }
}
