package msp.powerrangers.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import msp.powerrangers.R;
import msp.powerrangers.ui.listitems.ConfirmerCasesListItem;


/**
 * A fragment representing a listItem of Items.
 */
public class FragmentConfirmerCases extends Fragment {

    private static final String TAG = "ConfirmerCasesFragment";
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected Recycler_View_Adapter mAdapter;
    private ConfirmerCasesListItem casesListItem;
    private StorageReference storageRef;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentConfirmerCases() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageRef = FirebaseStorage.getInstance().getReference();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_confirmercases, container, false);
        rootView.setTag(TAG);

        // 1. Get a reference to recyclerView & set the onClickListener
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewCC);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // switch to FragmentDetailConfirmerCase
                        FragmentDetailConfirmerCase confirmCaseFragment = new FragmentDetailConfirmerCase();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        Bundle bundles = new Bundle();
                        Log.i("KATJA", "position in CC: "+ position);
                        bundles.putInt("Position", position);
                        confirmCaseFragment.setArguments(bundles);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.replace(R.id.activity_main_fragment_container, confirmCaseFragment);
                        ft.addToBackStack(null);

                        ft.commit();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // TODO: do whatever
                    }
                })
        );


        // 2. Set layoutManager (defines how the elements are laid out)
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // will be filled with data
        casesListItem = new ConfirmerCasesListItem();

        // 3. Create an adapter and fill
        mAdapter = new Recycler_View_Adapter(casesListItem.fill_with_data(), getContext());

        // 4. set adapter
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }


    /**
     * ##################################################################################################################
     * ################################        RecyclerViewAdapter       ################################################
     * ##################################################################################################################
     */
    private class Recycler_View_Adapter extends RecyclerView.Adapter<View_Holder> {

        List<ConfirmerCasesListItem> listItem = Collections.emptyList();
        Context context;


        Recycler_View_Adapter(List<ConfirmerCasesListItem> listItem, Context context) {
            this.listItem = listItem;
            this.context = context;
        }


        @Override
        public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Inflate the layout, initialize the View Holder
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fr_confirmercases_li, parent, false);
            return new View_Holder(view);
        }

        @Override
        public void onBindViewHolder(final View_Holder holder, int position) {
            //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
            holder.title.setText(listItem.get(position).title);
            holder.location.setText(listItem.get(position).city + ", " + listItem.get(position).country);
            holder.comment.setText(listItem.get(position).comment);

            String imageURL = listItem.get(position).imageUrlDB;
            Log.v("FragmentConfirmerCases", "imageURL: " + imageURL);

            try {
                final File localFile = File.createTempFile("images", "jpg");
                StorageReference riversRef = storageRef.child(imageURL);
                riversRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.v("FragmentConfirmerCases", "download erfolgreich");
                                Bitmap caseImage = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                holder.image.setImageBitmap(caseImage);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("FragmentConfirmerCases", "download nicht erfolgreich");
                        holder.image.setImageResource(R.drawable.placeholder_case);
                    }
                });
            } catch (Exception e) {
                Log.d("FragmentConfirmerCases", "download nicht erfolgreich");
                holder.image.setImageResource(R.drawable.placeholder_case);

            }
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
        public void insert(int position, ConfirmerCasesListItem listItem) {
            this.listItem.add(position, listItem);
            notifyItemInserted(position);
        }

        // Remove a RecyclerView item containing a specified Data object
        public void remove(ConfirmerCasesListItem data) {
            int position = listItem.indexOf(data);
            listItem.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * ##################################################################################################################
     * ###################################        VIEW HOLDER         ###################################################
     * ##################################################################################################################
     * <p>
     * The RecyclerView uses a ViewHolder to store the references to the relevant views for one entry in the RecyclerView.
     * This solution avoids all the findViewById() method calls in the adapter to find the views to be filled with data.
     * <p>
     * ##################################################################################################################
     * ##################################################################################################################
     */
    private class View_Holder extends RecyclerView.ViewHolder {

        // from layout
        CardView cv;
        TextView title;
        TextView location;
        TextView comment;
        ImageView image;

        View_Holder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cvCC);
            title = (TextView) itemView.findViewById(R.id.titleCC);
            location = (TextView) itemView.findViewById(R.id.locationCC);
            comment = (TextView) itemView.findViewById(R.id.descriptionCC);
            image = (ImageView) itemView.findViewById(R.id.ivCC);
        }
    }

}


