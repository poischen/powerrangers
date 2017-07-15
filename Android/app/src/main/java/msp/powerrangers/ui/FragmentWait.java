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


public class FragmentWait extends Fragment {

    private ConfirmerCasesListItem casesListItem;

    public FragmentWait() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String tag = getTag();
        Log.i("KATJA", "tag: "+tag);
        casesListItem = new ConfirmerCasesListItem();
        casesListItem.fill_with_data(this);

    }

    public void changeToContentView(){
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
