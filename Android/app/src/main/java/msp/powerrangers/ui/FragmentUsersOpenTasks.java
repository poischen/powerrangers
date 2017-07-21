package msp.powerrangers.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Collections;
import java.util.List;

import msp.powerrangers.R;
import msp.powerrangers.logic.Global;
import msp.powerrangers.logic.User;
import msp.powerrangers.ui.listitems.UsersOpenTasksListItem;


/**
 * A fragment representing a voting of Items.
 */
public class FragmentUsersOpenTasks extends Fragment {

    private static final String TAG = "UsersOpenTasksFragment";
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected Recycler_View_Adapter mAdapter;


    private UsersOpenTasksListItem usersOpenTasksListItem;

    //private FragmentTabs tabHost;

    private String currentUserId;
    private StorageReference storageRef;


    String taskTitle;
    String taskDescription;
    //  boolean isTaskAlreadyCompleted = mAdapter.getItem(position).getTaskCompleted();
    String taskId;
    String caseId;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentUsersOpenTasks() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        //tabHost = (FragmentTabs) bundle.getSerializable(getString(R.string.tabHostSerializable));
        usersOpenTasksListItem = (UsersOpenTasksListItem) bundle.getSerializable(getString(R.string.openTasksSerializable));
        Log.v("FragmentUsersOpenTasks", "usersOpenTasksListItem: " + usersOpenTasksListItem);

        User userTest = ((FragmentTabs)getActivity()).getUser();
        Log.v("FragmentUsersOpenTasks", "User: " + userTest);
        storageRef = FirebaseStorage.getInstance().getReference();

//***********************************************************************************************************************
        //@Viki from tabHost you can call getUser() to get the user and from this his id
//***********************************************************************************************************************

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_usersopentasks, container, false);
        rootView.setTag(TAG);

        // 1. Get a reference to recyclerView & set the onClickListener
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewUOT);



        // 2. Set layoutManager (defines how the elements are laid out)
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // will be filled with data
        //usersOpenTasksListItem = new UsersOpenTasksListItem();

        // 3. Create an adapter
        mAdapter = new Recycler_View_Adapter(usersOpenTasksListItem.getData(), getContext());


        // 4. set adapter
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        // show FragmentDetailUsersOpenTask

                        //  boolean isTaskAlreadyCompleted = mAdapter.getItem(position).getTaskCompleted();
                        taskId = mAdapter.getItem(position).getTaskid();
                        caseId = mAdapter.getItem(position).getCaseId();

                        FragmentDetailUsersOpenTask fragmentDetailUsersOpenTask = new FragmentDetailUsersOpenTask();
                        Bundle bundle = new Bundle();
                        bundle.putInt("PositionUsersOpenTask", position);

                        bundle.putString("TitleUsersOpenTask", mAdapter.getItem(position).title);
                        bundle.putString("LocationUsersOpenTask", mAdapter.getItem(position).location);
                        bundle.putString("DescriptionUsersOpenTask", mAdapter.getItem(position).comment);
                        bundle.putBoolean("StatusUsersOpenTask", mAdapter.getItem(position).getTaskCompleted());

                        bundle.putString("OpenTaskID", taskId);
                        bundle.putString("OpenTaskCaseID", caseId);
                        bundle.putString("taskImageUrl", mAdapter.getItem(position).taskImageUrlDB);
                        bundle.putString("caseImageUrl", mAdapter.getItem(position).caseImageUrlDB);
                        Log.i("BUNDLE IN UOT", bundle.toString());

                        fragmentDetailUsersOpenTask.setArguments(bundle);
                        ((BaseContainerFragment)getParentFragment()).replaceFragmentDetailOpenTask(fragmentDetailUsersOpenTask);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // TODO: do whatever
                    }
                })
        );


        return rootView;
    }

    public String getCurrentUserId(){
        return ((MainActivity)getActivity()).getUser().getId();
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
        public void onBindViewHolder(final View_Holder holder, final int position) {
            //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
            Log.i("Viki", "In onBindViewHolder");

            holder.title.setText(listItem.get(position).title);
            holder.location.setText(listItem.get(position).location);
            holder.description.setText(listItem.get(position).comment);
           // holder.imageView.setImageResource(listItem.get(position).imageID);

            String taskImageUrlDB = listItem.get(position).taskImageUrlDB;
            Log.i("Viki: TaskImageURLDB", taskImageUrlDB);

            // set image
            if (taskImageUrlDB != null) {     //  url from bundle is set

                try {   // download pic
                    final File localFile = File.createTempFile("images", "jpg");
                    StorageReference riversRef = storageRef.child(Global.getThumbUrl(taskImageUrlDB));
                    riversRef.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.i("Viki DetailVotingTask", "download erfolgreich, imageAfter");
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    holder.imageView.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.i("Viki DetailVotingTask", exception.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Log.i("Viki DetailVotingTask", e.getMessage());
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

        public UsersOpenTasksListItem getItem(int position) {
            return listItem.get(position);
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
        TextView description;
        ImageView imageView;

        View_Holder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cvUOT);
            title = (TextView) itemView.findViewById(R.id.titleUOT);
            location = (TextView) itemView.findViewById(R.id.locationOUT);
            description = (TextView) itemView.findViewById(R.id.descriptionUOT);
            imageView = (ImageView) itemView.findViewById(R.id.ivUOT);
        }
    }



}


