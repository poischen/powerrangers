package msp.powerrangers.ui;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import msp.powerrangers.R;
/**
 * Screen to show details of a user's open task and complete it
 */
public class FragmentDetailUsersOpenTask extends Fragment {

    public FragmentDetailUsersOpenTask() {
        // Required empty public constructor
    }
    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDetailUsersOpenTask.

    // TODO: Rename and change types and number of parameters
    public static FragmentDetailUsersOpenTask newInstance(String param1, String param2) {
        FragmentDetailUsersOpenTask fragment = new FragmentDetailUsersOpenTask();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fr_detail_users_open_task, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

}