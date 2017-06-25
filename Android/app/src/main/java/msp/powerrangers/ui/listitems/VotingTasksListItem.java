package msp.powerrangers.ui.listitems;

import java.util.ArrayList;
import java.util.List;

import msp.powerrangers.R;

/**
 * == Data
 * Tutorial: https://www.sitepoint.com/mastering-complex-lists-with-the-android-recyclerview/
 *
 */
public class VotingTasksListItem {

    public String title;
    public String location;
    public int imageID1;
    public int imageID2;

    private VotingTasksListItem(String title, String location, int imageID1, int imageID2) {
        this.title = title;
        this.location = location;
        this.imageID1 = imageID1;
        this.imageID2 = imageID2;
    }

    /**
     * Generates RT-Objects for RecyclerView's adapter.
     */
    public static List<VotingTasksListItem> fill_with_data() {
        List<VotingTasksListItem> data = new ArrayList<>();
        data.add(new VotingTasksListItem("Polluted Beach", "Tamaris, Marokko", R.drawable.polluted_beach1, R.drawable.clean_beach));
        data.add(new VotingTasksListItem("Waste on the beach", "Spain, Palma de Mallorca",  R.drawable.polluted_beach2, R.drawable.clean_beach2));
        return data;
    }

}
