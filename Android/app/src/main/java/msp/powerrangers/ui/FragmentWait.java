package msp.powerrangers.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public FragmentWait() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String tag = getTag();
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
            casesListItem = new ConfirmerCasesListItem();
            casesListItem.fill_with_data(this);
            confirmerTabClicked = true;
        }

        if (tag.equals(rangerTag)) {
            rangerTasksListItem = new RangerTasksListItem();
            rangerTasksListItem.fill_with_data(this);
            rangerTabClicked = true;
        }

        if (tag.equals(openTaskTag)) {


        }

        if (tag.equals(startTag)) {
            replaceFragment(startTag, true, this);
        }

    }


    public void changeToContentUOT() {
        replaceFragmentUOT(this);
    }

    public void changeToContentView(boolean isaboutVoting) {
        String tag;
        if (isaboutVoting) {
            tag = (getString(R.string.votingTasksTag));
        } else {
            tag = getTag();
        }

        replaceFragment(tag, true, this);

    }

    public void initReplacingFragmentOpenTasks(UsersOpenTasksListItem fragment, String userDbId) {
        usersOpenTaksListItem = fragment;
        fragment.fill_with_data(this, userDbId);


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


    public VotingTasksListItem getVotingTasksListItem() {
        return votingTasksListItem;
    }

    public UsersOpenTasksListItem getUsersOpenTasksListItem() {
        return usersOpenTaksListItem;
    }
}
