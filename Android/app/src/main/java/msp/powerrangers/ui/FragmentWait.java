package msp.powerrangers.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import msp.powerrangers.R;
import msp.powerrangers.ui.listitems.ConfirmerCasesListItem;
import msp.powerrangers.ui.listitems.RangerTasksListItem;
import msp.powerrangers.ui.listitems.UsersOpenTasksListItem;
import msp.powerrangers.ui.listitems.VotingTasksListItem;


public class FragmentWait extends Fragment {

    private ConfirmerCasesListItem casesListItem;
    private RangerTasksListItem rangerTasksListItem;
    private UsersOpenTasksListItem usersOpenTasksListItem;
    private VotingTasksListItem votingTasksListItem;

    public FragmentWait() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String tag = getTag();
        Log.i("KATJA", "tag: " + tag);

        String startTag = getString(R.string.startTag);
        String votingTag = getString(R.string.votingTasksTag);
        String confirmerTag = getString(R.string.confirmCasesTag);
        String rangerTag = getString(R.string.rangerTasksTag);

        // check which tab was clicked and get the data from the db
        if (tag.equals(votingTag)) {
            votingTasksListItem = new VotingTasksListItem();
            votingTasksListItem.fill_with_data(this);

        } else if (tag.equals(confirmerTag)) {
            casesListItem = new ConfirmerCasesListItem();
            casesListItem.fill_with_data(this);

        } else if (tag.equals(rangerTag)) {
            rangerTasksListItem = new RangerTasksListItem();
            rangerTasksListItem.fill_with_data(this);
        }


    }

    public void changeToContentView() {
        FragmentConfirmerCases confirmerCasesFragment = new FragmentConfirmerCases();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundles = new Bundle();
        bundles.putSerializable("caseListItems", casesListItem);
        confirmerCasesFragment.setArguments(bundles);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.activity_main_fragment_container, confirmerCasesFragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fr_wait, container, false);
    }

}
