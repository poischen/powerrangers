package msp.powerrangers.ui;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import msp.powerrangers.R;
import msp.powerrangers.ui.listitems.VotingTasksListItem;


/**
 * A fragment representing a listItem of Items.
 */
public class FragmentVotingTasks extends Fragment {

    private static final String TAG = "VotingTasksFragment";
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected Recycler_View_Adapter mAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentVotingTasks() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_votingtasks, container, false);
        rootView.setTag(TAG);

        // 1. Get a reference to recyclerView & set the onClickListener
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewVT);

        // TODO: make up/dowm button clickable
        // TODO: make before/after images clickable

        // 2. Set layoutManager (defines how the elements are laid out)
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // test data
        List<VotingTasksListItem> data = VotingTasksListItem.fill_with_data();

        // 3. Create an adapter
        mAdapter = new Recycler_View_Adapter(data, getContext());

        // 4. set adapter
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }



    /**
     * ##################################################################################################################
     * ################################        RecyclerViewAdapter       ################################################
     * ##################################################################################################################
     *
     */
    private class Recycler_View_Adapter extends RecyclerView.Adapter<View_Holder> {

        List<VotingTasksListItem> listItem = Collections.emptyList();
        Context context;


        Recycler_View_Adapter(List<VotingTasksListItem> listItem, Context context) {
            this.listItem = listItem;
            this.context = context;
        }


        @Override
        public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Inflate the layout, initialize the View Holder
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fr_votingtasks_li, parent, false);
            return new View_Holder(view);
        }

        @Override
        public void onBindViewHolder(final View_Holder holder, int position) {
            //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
            holder.title.setText(listItem.get(position).title);
            holder.location.setText(listItem.get(position).location);
            holder.imageView1.setImageResource(listItem.get(position).imageID1);
            holder.imageView2.setImageResource(listItem.get(position).imageID2);

            // fixed icons/images
            holder.locationIcon.setImageResource(R.drawable.location);
            holder.up.setImageResource(R.drawable.up);
            holder.down.setImageResource(R.drawable.down);
        }

        @Override
        public int getItemCount() {
            //returns the number of elements the RecyclerView will display
            return listItem.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        // Insert a new item to the RecyclerView on a predefined position
        public void insert(int position, VotingTasksListItem listItem) {
            this.listItem.add(position, listItem);
            notifyItemInserted(position);
        }

        // Remove a RecyclerView item containing a specified Data object
        public void remove(VotingTasksListItem data) {
            int position = listItem.indexOf(data);
            listItem.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * ##################################################################################################################
     * ###################################        VIEW HOLDER         ###################################################
     * ##################################################################################################################
     *
     * The RecyclerView uses a ViewHolder to store the references to the relevant views for one entry in the RecyclerView.
     * This solution avoids all the findViewById() method calls in the adapter to find the views to be filled with data.
     *
     * ##################################################################################################################
     * ##################################################################################################################
     *
     */
    private class View_Holder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView title;
        TextView location;
        ImageView locationIcon;
        ImageView imageView1;
        ImageView imageView2;
        ImageView up;
        ImageView down;

        View_Holder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cvVT);
            title = (TextView) itemView.findViewById(R.id.titleVT);
            location = (TextView) itemView.findViewById(R.id.locationVT);
            locationIcon = (ImageView) itemView.findViewById(R.id.ivLocationIcon);
            imageView1 = (ImageView) itemView.findViewById(R.id.ivVotingPic1);
            imageView2 = (ImageView) itemView.findViewById(R.id.ivVotingPic2);
            up = (ImageView) itemView.findViewById(R.id.ivVotingUp);
            down = (ImageView) itemView.findViewById(R.id.ivVotingDown);
        }
    }



}


