package msp.powerrangers.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import msp.powerrangers.R;

public class FragmentTabs extends Fragment {
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
            arg1.putInt("Arg for Frag1", 1);
            tabHost.addTab(tabHost.newTabSpec(getString(R.string.start)).setIndicator(getString(R.string.start)),
                    FragmentStart.class, arg1);

            Bundle arg2 = new Bundle();
            arg2.putInt("Arg for Frag2", 2);
            tabHost.addTab(tabHost.newTabSpec(getString(R.string.confirmTasks)).setIndicator(getString(R.string.confirmTasks)),
                    FragmentStart.class, arg2);
//TODO: entsprechendes Fragment einsetzen

            Bundle arg3 = new Bundle();
            arg3.putInt("Arg for Frag3", 3);
            tabHost.addTab(tabHost.newTabSpec(getString(R.string.openTasks)).setIndicator(getString(R.string.openTasks)),
                    FragmentStart.class, arg3);
//TODO: entsprechendes Fragment einsetzen

            Bundle arg4 = new Bundle();
            arg2.putInt("Arg for Frag4", 4);
            tabHost.addTab(tabHost.newTabSpec(getString(R.string.findYorJob)).setIndicator(getString(R.string.findYorJob)),
                    FragmentStart.class, arg4);
//TODO: entsprechendes Fragment einsetzen

            return rootView;
        }

}