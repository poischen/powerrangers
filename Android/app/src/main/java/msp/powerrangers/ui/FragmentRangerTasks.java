package msp.powerrangers.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Collections;
import java.util.List;

import msp.powerrangers.R;
import msp.powerrangers.logic.Global;
import msp.powerrangers.ui.listitems.ConfirmerCasesListItem;
import msp.powerrangers.ui.listitems.RangerTasksListItem;


/**
 * A fragment representing a voting of Items.
 */
public class FragmentRangerTasks extends Fragment {

    private static final String TAG = "RangerTaskFragment";
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected Recycler_View_Adapter mAdapter;
    private RangerTasksListItem tasksListItem;
    private StorageReference storageRef;

    public FragmentRangerTasks() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("KATJA","onCreate FrRangerTasks");
        storageRef = FirebaseStorage.getInstance().getReference();

        Bundle bundle = getArguments();
        tasksListItem = (RangerTasksListItem) bundle.getSerializable(getString(R.string.rangerTasksSerializable));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_rangertasks, container, false);
        rootView.setTag(TAG);
        Log.i("KATJA","onCreateView FrRangerTasks");

        // 1. Get a reference to recyclerView & set the onClickListener
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewRT);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //show FragmentDetailRangerTask
                        FragmentDetailRangerTask fragmentDetailRangerTask = new FragmentDetailRangerTask();
                        Bundle bundle = new Bundle();
                        bundle.putInt("PositionRanger", position);
                        bundle.putString("taskImageUrl", mAdapter.getItem(position).getTaskUrl());
                        bundle.putString("caseImageUrl", mAdapter.getItem(position).getCaseUrl());


                        fragmentDetailRangerTask.setArguments(bundle);
                        ((BaseContainerFragment)getParentFragment()).replaceFragmentDetailRanger(fragmentDetailRangerTask);

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

        // 3. Create an adapter
        mAdapter = new Recycler_View_Adapter(tasksListItem.getData(), getContext());

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

        List<RangerTasksListItem> listItem = Collections.emptyList();
        Context context;

        Recycler_View_Adapter(List<RangerTasksListItem> listItem, Context context) {
            this.listItem = listItem;
            this.context = context;
        }

        @Override
        public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Inflate the layout, initialize the View Holder
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fr_rangertasks_li, parent, false);
            return new View_Holder(view);

        }

        @Override
        public void onBindViewHolder(final View_Holder holder, final int position) {

            // populate the current row on the RecyclerView
            holder.title.setText(listItem.get(position).title);
            holder.location.setText(listItem.get(position).city + ", " + listItem.get(position).country);
            holder.comment.setText(listItem.get(position).comment);

            String taskImageUrlDB = listItem.get(position).taskImageUrlDB;
            Log.i("KATJA FrRangerTasks", "taskImageUrlDB: " + taskImageUrlDB);

            // set image
            if (taskImageUrlDB != null) {     //  url from bundle is set

                try { // download image
                    final File localFile = File.createTempFile("images", "jpg");
                    StorageReference riversRef = storageRef.child(Global.getThumbUrl(taskImageUrlDB));
                    riversRef.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.i(" KATJA FrRangerTasks", "download erfolgreich");
                                    Bitmap taskImage = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    // set blurred task image
                                    holder.image.setImageBitmap(blur(taskImage));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.i("KATJA FrRangerTasks", "download nicht erfolgreich (1)");
                            holder.image.setImageResource(R.drawable.placeholder_task);
                        }
                    });
                } catch (Exception e) {
                    Log.i("KATJA FrRangerTasks", "download nicht erfolgreich (2)");
                    holder.image.setImageResource(R.drawable.placeholder_task);
                }
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
        public void insert(int position, RangerTasksListItem listItem) {
            this.listItem.add(position, listItem);
            notifyItemInserted(position);
        }

        // Remove a RecyclerView item containing a specified Data object
        public void remove(RangerTasksListItem data) {
            int position = listItem.indexOf(data);
            listItem.remove(position);
            notifyItemRemoved(position);
        }

        public RangerTasksListItem getItem(int position) {
            return listItem.get(position);
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
            cv = (CardView) itemView.findViewById(R.id.cvRT);
            title = (TextView) itemView.findViewById(R.id.titleRT);
            location = (TextView) itemView.findViewById(R.id.locationRT);
            comment = (TextView) itemView.findViewById(R.id.descriptionRT);
            image = (ImageView) itemView.findViewById(R.id.ivRT);
        }
    }

    //Set the radius of the Blur. Supported range 0 < radius <= 25
    private static final float BLUR_RADIUS = 2;

    public Bitmap blur(Bitmap image) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(getActivity());
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

}