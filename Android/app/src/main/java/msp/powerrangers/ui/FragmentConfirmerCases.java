package msp.powerrangers.ui;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
<<<<<<< HEAD
import android.support.v7.widget.GridLayoutManager;
=======
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
>>>>>>> ui
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import msp.powerrangers.R;
import msp.powerrangers.ui.listitems.FragmentConfirmerCasesListItem;
import msp.powerrangers.ui.listitems.FragmentConfirmerCasesListItem.DummyItem;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FragmentConfirmerCases extends Fragment {

<<<<<<< HEAD
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
=======
    private static final String TAG = "ConfirmerCasesFragment";
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected Recycler_View_Adapter mAdapter;
    private ConfirmerCasesListItem  casesListItem;

>>>>>>> ui

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentConfirmerCases() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FragmentConfirmerCases newInstance(int columnCount) {
        FragmentConfirmerCases fragment = new FragmentConfirmerCases();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
<<<<<<< HEAD
        View view = inflater.inflate(R.layout.fragment_fragmentconfirmercaseslistitem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyFragmentConfirmerCaseListItemRecyclerViewAdapter(FragmentConfirmerCasesListItem.ITEMS, mListener));
        }
        return view;
    }
=======
        View rootView = inflater.inflate(R.layout.fr_confirmercases, container, false);
        rootView.setTag(TAG);


        // 2. Set layoutManager (defines how the elements are laid out)
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // test data

        casesListItem = new ConfirmerCasesListItem();
       // casesListItem.fill_with_data();

       // List<ConfirmerCasesListItem> data = casesListItem.fill_with_data();


      //  Log.i("CONFIRMER CASES" , casesListItem.toString());

        // 3. Create an adapter
        mAdapter = new Recycler_View_Adapter(casesListItem.fill_with_data(), getContext());

        // 4. set adapter
        mRecyclerView.setAdapter(mAdapter);


        // 1. Get a reference to recyclerView & set the onClickListener
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewCC);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // switch to FragmentDetailConfirmerCase
                        FragmentDetailConfirmerCase confirmCaseFragment = new FragmentDetailConfirmerCase();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        Bundle bundles = new Bundle();
                       // bundles.putInt("position");

                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.replace(R.id.activity_main_fragment_container, confirmCaseFragment);
                        ft.addToBackStack(null);
                        ft.commit();


                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // TODO: do whatever
                    }
                })
        );

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


    public class MyFragmentConfirmerCaseListItemRecyclerViewAdapter extends RecyclerView.Adapter<MyFragmentConfirmerCaseListItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyItem> mValues;
        private final OnListFragmentInteractionListener mListener;

        public MyFragmentConfirmerCaseListItemRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
            mValues = items;
            mListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_fragmentconfirmercaseslistitem, parent, false);
            return new ViewHolder(view);
        }

        @Override
<<<<<<< HEAD
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
=======
        public void onBindViewHolder(final View_Holder holder, int position) {
            //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
            holder.title.setText(listItem.get(position).title);
            holder.description.setText(listItem.get(position).desc);
            //holder.imageView.setImageResource(listItem.get(position).imageID);
>>>>>>> ui
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
}
