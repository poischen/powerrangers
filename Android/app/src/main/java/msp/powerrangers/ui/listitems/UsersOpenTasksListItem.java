package msp.powerrangers.ui.listitems;

import java.util.ArrayList;
import java.util.List;

import msp.powerrangers.R;

/**
 * == Data
 * Tutorial: https://www.sitepoint.com/mastering-complex-lists-with-the-android-recyclerview/
 *
 */
public class UsersOpenTasksListItem {

    public String title;
    public String desc;
    public int imageID;

    private UsersOpenTasksListItem(String title, String desc, int imageID) {
        this.title = title;
        this.desc = desc;
        this.imageID = imageID;
    }

    /**
     * Generates UOT-Objects for RecyclerView's adapter.
     */
    public static List<UsersOpenTasksListItem> fill_with_data() {
        List<UsersOpenTasksListItem> data = new ArrayList<>();
        data.add(new UsersOpenTasksListItem("Kathmandu, Nepal", "Summary of the case in Kathmandu....", R.drawable.placeholder_task));
        data.add(new UsersOpenTasksListItem("Munich, Germany", "Summary of the case in Munich....", R.drawable.placeholder_task));
        data.add(new UsersOpenTasksListItem("Chennai, India", "Summary of the case in Chennai....", R.drawable.placeholder_task));
        data.add(new UsersOpenTasksListItem("Pattaya, Thailand", "Summary of the case in Pattaya....", R.drawable.placeholder_task));

        return data;
    }

}
