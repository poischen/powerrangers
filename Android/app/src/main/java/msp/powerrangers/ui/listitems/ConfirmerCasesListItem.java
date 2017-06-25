package msp.powerrangers.ui.listitems;

import java.util.ArrayList;
import java.util.List;

import msp.powerrangers.R;

/**
 * == Data
 * Tutorial: https://www.sitepoint.com/mastering-complex-lists-with-the-android-recyclerview/
 *
 */
public class ConfirmerCasesListItem {

    public String title;
    public String desc;
    public int imageID;

    private ConfirmerCasesListItem(String title, String desc, int imageID) {
        this.title = title;
        this.desc = desc;
        this.imageID = imageID;
    }

    /**
     * Generates RT-Objects for RecyclerView's adapter.
     */
    public static List<ConfirmerCasesListItem> fill_with_data() {
        List<ConfirmerCasesListItem> data = new ArrayList<>();
        data.add(new ConfirmerCasesListItem("Munich, Germany", "English Garden, 25.06.2017 ...", R.drawable.placeholder_case));
        data.add(new ConfirmerCasesListItem("Munich, Germany", "Oettingenstr, 23.06.2017 ...", R.drawable.placeholder_case));
        data.add(new ConfirmerCasesListItem("Casablanca, Marokko", "Summary: ....", R.drawable.placeholder_case));
        data.add(new ConfirmerCasesListItem("Colombo, Sri Lanka", "Summary of the case in Colombo...", R.drawable.placeholder_case));
        return data;
    }

}
