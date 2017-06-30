package msp.powerrangers.ui;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import msp.powerrangers.R;

public class FragmentDetailRangerTask extends Fragment {

    private ImageView rangerImage;
    private TextView textRangerReward;
    private TextView textNumberRangers;
    private TextView textScalePollution;
    private ImageView iconMoney;
    private ImageView iconRanger;
    private ImageView iconPollution;

    public FragmentDetailRangerTask() {
        // Required empty public constructor
    }
    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDetailRangerTask.

    // TODO: Rename and change types and number of parameters
    public static FragmentDetailRangerTask newInstance(String param1, String param2) {
        FragmentDetailRangerTask fragment = new FragmentDetailRangerTask();
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
        View view = inflater.inflate(R.layout.fr_detail_ranger_task, container, false);
        // Get all layout elements
        rangerImage = (ImageView) view.findViewById(R.id.rangerImage);
        rangerImage.setImageResource(R.drawable.polluted_beach1);

        textRangerReward = (TextView) view.findViewById(R.id.textRangerReward);
        textNumberRangers = (TextView) view.findViewById(R.id.textNumberRangers);
        textScalePollution = (TextView) view.findViewById(R.id.textScalePollution);

        iconRanger = (ImageView) view.findViewById(R.id.imageNumberRangers);
        iconRanger.setImageResource(R.drawable.iconranger);

        iconMoney = (ImageView) view.findViewById(R.id.rangerReward);
        iconMoney.setImageResource(R.drawable.iconrewardsmall);

        iconPollution = (ImageView) view.findViewById(R.id.imageScalePollution);
        iconPollution.setImageResource(R.drawable.iconpollutionmedium);

        return view;
    }

}