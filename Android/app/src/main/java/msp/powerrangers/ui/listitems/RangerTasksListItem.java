package msp.powerrangers.ui.listitems;

import java.util.ArrayList;
import java.util.List;

import msp.powerrangers.R;

/**
 * == Data
 * Tutorial: https://www.sitepoint.com/mastering-complex-lists-with-the-android-recyclerview/
 *
 */
public class RangerTasksListItem {

    public String title;
    public String desc;
    public int imageID;

    private RangerTasksListItem(String title, String desc, int imageID) {
        this.title = title;
        this.desc = desc;
        this.imageID = imageID;
    }

    /**
     * Generates RT-Objects for RecyclerView's adapter.
     */
    public static List<RangerTasksListItem> fill_with_data() {
        List<RangerTasksListItem> data = new ArrayList<>();
        data.add(new RangerTasksListItem("Kathmandu, Nepal", "Summary of the case in Kathmandu....", R.drawable.placeholder_task));
        data.add(new RangerTasksListItem("Munich, Germany", "Summary of the case in Munich....", R.drawable.placeholder_task));
        data.add(new RangerTasksListItem("Chennai, India", "Summary of the case in Chennai....", R.drawable.placeholder_task));
        data.add(new RangerTasksListItem("Pattaya, Thailand", "Summary of the case in Pattaya....", R.drawable.placeholder_task));
        data.add(new RangerTasksListItem("Hanoi, Vietnam", "Summary of the case in Hanoi....", R.drawable.placeholder_task));
        data.add(new RangerTasksListItem("Kemer, Turkey", "Summary of the case in Kemer....", R.drawable.placeholder_task));
        return data;
    }

}
