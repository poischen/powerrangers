package msp.powerrangers.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import msp.powerrangers.R;
import msp.powerrangers.ui.listitems.FragmentVotingTasksListItem;
import msp.powerrangers.ui.listitems.FragmentVotingTasksListItem.DummyItem;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FragmentVotingTasks extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    TextView taskDescription;
    TextView taskLocation;

    ImageButton confirmButton;
    ImageButton cancelButton;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentVotingTasks() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FragmentVotingTasks newInstance(int columnCount) {
        FragmentVotingTasks fragment = new FragmentVotingTasks();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
<<<<<<< HEAD
        View view = inflater.inflate(R.layout.fragment_fragmentitemvotingtasks_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyFragmentItemVotingTaskRecyclerViewAdapter(FragmentVotingTasksListItem.ITEMS, mListener));
        }
        return view;
    }
=======
        View rootView = inflater.inflate(R.layout.fr_votingtasks, container, false);
        rootView.setTag(TAG);

        // 1. Get a reference to recyclerView & set the onClickListener
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewVT);



        // 2. Set layoutManager (defines how the elements are laid out)
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // test data
        List<VotingTasksListItem> data = VotingTasksListItem.fill_with_data();

        // 3. Create an adapter
        mAdapter = new Recycler_View_Adapter(data, getContext());
>>>>>>> ui


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

<<<<<<< HEAD

=======
        List<VotingTasksListItem> listItem = Collections.emptyList();
        Context context;
        int positive;
        int negative;
        int votingThreshold;
>>>>>>> ui

    public class MyFragmentItemVotingTaskRecyclerViewAdapter extends RecyclerView.Adapter<MyFragmentItemVotingTaskRecyclerViewAdapter.ViewHolder> {

        private final List<DummyItem> mValues;
        private final OnListFragmentInteractionListener mListener;

        public MyFragmentItemVotingTaskRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
            mValues = items;
            mListener = listener;
        }

        @Override
<<<<<<< HEAD
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_fragmentitemvotingtasks, parent, false);
            return new ViewHolder(view);
=======
        public void onBindViewHolder(final View_Holder holder, final int position) {

            // TODO: set real values from db tasks
            // title and location
            holder.title.setText(listItem.get(position).title);
            holder.location.setText(listItem.get(position).location);
            holder.locationIcon.setImageResource(R.drawable.location);

            // before/after images
            holder.imageView1.setImageResource(listItem.get(position).imageID1);
            holder.imageView2.setImageResource(listItem.get(position).imageID2);

            //  thumbs up/down
            holder.up.setImageResource(R.drawable.up);
            holder.down.setImageResource(R.drawable.down);
            holder.nLikes.setText("4");           // dummy values
            holder.nDislikes.setText("0");

            positive = Integer.parseInt(holder.nLikes.getText().toString());
            negative = Integer.parseInt(holder.nDislikes.getText().toString());
            votingThreshold = 5;

            // set onClickListener for before image
            holder.imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: should open the image in full size
                    Toast.makeText(getContext(), "Here opens the disgusting before image..", Toast.LENGTH_SHORT).show();
                }
            });

            // set onClickListener for after image
            holder.imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: should open the image in full size
                    Toast.makeText(getContext(), "Here opens the most beautiful after image..", Toast.LENGTH_SHORT).show();
                }
            });

            // set onClickListener for up votes
            holder.up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // prevent downvoting if already upvoted
                    holder.down.setEnabled(false);

                    // remove the item if the threshold was reached
                    if (positive + 1 >= votingThreshold) {
                        remove(listItem.get(position));
                        Toast.makeText(getContext(), "Thanks! \nThe ranger will get his reward!", Toast.LENGTH_LONG).show();
                        // TODO: set the isConfirmed for the task in db to true. Ranger should get his reward.
                    } else {
                        // TODO: on data update write in db
                        holder.nLikes.setText(String.valueOf(positive + 1));
                        //prevent clicking multiple times
                        holder.up.setEnabled(false);
                    }
                }
            });

            // set onClickListener for down votes
            holder.down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // prevent upvoting if already downvoted
                    holder.up.setEnabled(false);

                    // remove the item if the threshold was reached
                    if (negative + 1 >= votingThreshold) {
                        remove(listItem.get(position));
                        Toast.makeText(getContext(), "The ranger won't get his reward...", Toast.LENGTH_LONG).show();
                        // TODO: set the isConfirmed to false. Ranger fucked up, he wont get his reward :)
                    }

                    else {
                        // TODO: on data update write in db
                        holder.nDislikes.setText(String.valueOf(negative + 1));
                        //prevent clicking multiple times
                        holder.down.setEnabled(false);
                    }
                }
            });


>>>>>>> ui
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(holder.mItem);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

<<<<<<< HEAD
=======
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
        TextView nLikes;
        TextView nDislikes;

        View_Holder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cvVT);
            title = (TextView) itemView.findViewById(R.id.titleVT);
            location = (TextView) itemView.findViewById(R.id.locationVT);
            locationIcon = (ImageView) itemView.findViewById(R.id.ivLocationIcon);

            // before/after images
            imageView1 = (ImageView) itemView.findViewById(R.id.ivVotingPic1);
            imageView2 = (ImageView) itemView.findViewById(R.id.ivVotingPic2);

            // vote thumbs up/down
            up = (ImageView) itemView.findViewById(R.id.ivVotingUp);
            down = (ImageView) itemView.findViewById(R.id.ivVotingDown);
            nLikes = (TextView) itemView.findViewById(R.id.tvVotingUp);
            nDislikes = (TextView) itemView.findViewById(R.id.tvVotingDown);
        }
    }



>>>>>>> ui
}



