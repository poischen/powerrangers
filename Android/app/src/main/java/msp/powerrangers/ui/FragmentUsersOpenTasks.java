package msp.powerrangers.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import msp.powerrangers.ui.listitems.RangerTasksListItem;
import msp.powerrangers.ui.listitems.UsersOpenTasksListItem;


/**
 * A fragment representing a listItem of Items.
 */
public class FragmentUsersOpenTasks extends Fragment {

    private static final String TAG = "UsersOpenTasksFragment";
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected Recycler_View_Adapter mAdapter;

    private UsersOpenTasksListItem usersOpenTasksListItem;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentUsersOpenTasks() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_usersopentasks, container, false);
        rootView.setTag(TAG);

        // 1. Get a reference to recyclerView & set the onClickListener
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewUOT);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        // show FragmentDetailUsersOpenTask
                        Intent intent = new Intent(getActivity(), ActivityDetailContainer.class);
                        intent.putExtra(String.valueOf(R.string.activityDetailContainer_targetFr), "FragmentDetailUsersOpenTask");
                        startActivity(intent);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // TODO: do whatever
                    }
                })
        );

        // 2. Set layoutManager (defines how the elements are laid out)
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // will be filled with data
        usersOpenTasksListItem = new UsersOpenTasksListItem();

        // 3. Create an adapter
        mAdapter = new Recycler_View_Adapter(usersOpenTasksListItem.fill_with_data(), getContext());

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

        List<UsersOpenTasksListItem> listItem = Collections.emptyList();
        Context context;


        Recycler_View_Adapter(List<UsersOpenTasksListItem> listItem, Context context) {
            this.listItem = listItem;
            this.context = context;
        }


        @Override
        public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Inflate the layout, initialize the View Holder
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fr_usersopentasks_li, parent, false);
            return new View_Holder(view);

        }

        @Override
        public void onBindViewHolder(final View_Holder holder, int position) {
            //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
            holder.title.setText(listItem.get(position).title);
            holder.description.setText(listItem.get(position).desc);
            holder.imageView.setImageResource(listItem.get(position).imageID);
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
        public void insert(int position, UsersOpenTasksListItem listItem) {
            this.listItem.add(position, listItem);
            notifyItemInserted(position);
        }

        // Remove a RecyclerView item containing a specified Data object
        public void remove(UsersOpenTasksListItem data) {
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
        TextView description;
        ImageView imageView;

        View_Holder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cvUOT);
            title = (TextView) itemView.findViewById(R.id.titleUOT);
            description = (TextView) itemView.findViewById(R.id.descriptionUOT);
            imageView = (ImageView) itemView.findViewById(R.id.ivUOT);
        }
    }



}


