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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Collections;
import java.util.List;

import msp.powerrangers.R;
import msp.powerrangers.logic.Global;
import msp.powerrangers.ui.listitems.RangerTasksListItem;
import msp.powerrangers.ui.listitems.VotingTasksListItem;


/**
 * A fragment representing a voting of Items.
 */
public class FragmentVotingTasks extends Fragment {

    private static final String TAG = "VotingTasksFragment";
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected Recycler_View_Adapter mAdapter;
    private StorageReference storageRef;
    private VotingTasksListItem  votingTasksListItem;

    // Firebase db instance
    private DatabaseReference dbRefTasks = FirebaseDatabase.getInstance().getReference("tasks");

    public FragmentVotingTasks() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageRef = FirebaseStorage.getInstance().getReference();

        Bundle bund = getArguments();
        votingTasksListItem = (VotingTasksListItem) bund.getSerializable(getString(R.string.votingTasksSerializable));
        Log.i("KATJA", "VotingTasks onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_votingtasks, container, false);
        rootView.setTag(TAG);
        Log.i("KATJA", "*****************  new voting  **********************");

        // 1. Get a reference to recyclerView & set the onClickListener
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewVT);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //show FragmentDetailVotingTask
                        FragmentDetailVotingTask fragmentDetailVotingTask = new FragmentDetailVotingTask();
                        Bundle bundle = new Bundle();
                        bundle.putInt("PositionVotingTask", position);

                        // img before
                        try{
                            Bitmap imgBefore = mAdapter.getItem(position).getBitmapBefore();
                            ByteArrayOutputStream bs = new ByteArrayOutputStream();
                            imgBefore.compress(Bitmap.CompressFormat.JPEG, 50, bs);
                            bundle.putByteArray("imageBeforeByteArray", bs.toByteArray());
                        } catch (Exception e){
                            bundle.putString("imageBeforeUrl", mAdapter.getItem(position).getImageBeforeURL());
                        }

                        // img after
                        try{
                            Bitmap imgAfter = mAdapter.getItem(position).getBitmapAfter();
                            ByteArrayOutputStream bs = new ByteArrayOutputStream();
                            imgAfter.compress(Bitmap.CompressFormat.JPEG, 50, bs);
                            bundle.putByteArray("imageAfterByteArray", bs.toByteArray());
                        } catch (Exception e){
                            bundle.putString("imageAfterUrl", mAdapter.getItem(position).getImageAfterURL());
                        }

                        fragmentDetailVotingTask.setArguments(bundle);
                        ((BaseContainerFragment)getParentFragment()).replaceFragmentDetailVoting(fragmentDetailVotingTask);

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

        // 3. Create an adapter and fill
        mAdapter = new FragmentVotingTasks.Recycler_View_Adapter(votingTasksListItem.getData(), getContext());

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

        List<VotingTasksListItem> voting = Collections.emptyList();
        Context context;

       /* private DatabaseReference pathCurrentTask;
        String votingTitle;
        String votingLocation;
        String votingLikes;
        String votingDislikes;
        int votingThreshold = 5; */


        Recycler_View_Adapter(List<VotingTasksListItem> voting, Context context) {
            this.voting = voting;
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
        public void onBindViewHolder(final View_Holder holder, final int position) {

            // populate the current row on the RecyclerView
            holder.title.setText(voting.get(position).title);
            holder.location.setText(voting.get(position).location);


            String imageBeforeURL = voting.get(position).imageBeforeURL;
            Log.i("KATJA", "imageBeforeURL: " + imageBeforeURL);

            try {
                final File localFile = File.createTempFile("images", "jpg");
                StorageReference riversRef = storageRef.child(Global.getThumbUrl(imageBeforeURL));
                riversRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.v("FragmentRangerTasks", "download erfolgreich");
                                Bitmap taskImage = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                voting.get(position).setBitmapBefore(taskImage);
                                holder.imageView1.setImageBitmap(voting.get(position).getBitmapBefore());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("FragmentRangerTasks", "download nicht erfolgreich (1)");
                        holder.imageView1.setImageResource(R.drawable.placeholder_task);
                    }
                });
            } catch (Exception e) {
                Log.d("FragmentRangerTasks", "download nicht erfolgreich (2)");
                holder.imageView1.setImageResource(R.drawable.placeholder_task);
            }


            /*
                ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

             */

            // current task
            pathCurrentTask = dbRefTasks.child(voting.get(position).taskId);
            Log.i("Viki", "Voting get position taskid" + voting.get(position).taskId);

            Log.i("Viki", "Path to current Task " + pathCurrentTask.toString());

            // before image
            String imageBeforeUrl = voting.get(position).imageBeforeURL;
            try {
                final File localFile = File.createTempFile("images", "jpg");
                StorageReference riversRef = storageRef.child(Global.getThumbUrl(imageBeforeUrl));
                riversRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.v("FragmentVotingTask", "download erfolgreich");
                                Log.i("KATJA", "download erfolgreich");
                                Bitmap beforeImage = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                holder.imageView1.setImageBitmap(beforeImage);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("FragmentVotingTask", "download nicht erfolgreich");
                        Log.i("KATJA", "download nicht erfolgreich");
                        holder.imageView1.setImageResource(voting.get(position).imageID1);
                    }
                });
            } catch (Exception e) {
                Log.d("FragmentVotingTask", "download nicht erfolgreich");
                holder.imageView1.setImageResource(voting.get(position).imageID1);
            }

            // after image
            //TODO: get after image from storage
            String imageAfterUrl = voting.get(position).imageAfterURL;
            try {
                final File localFile = File.createTempFile("images", "jpg");
                StorageReference riversRef = storageRef.child(Global.getThumbUrl(imageAfterUrl));
                riversRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.v("FragmentVotingTask", "download erfolgreich");
                                Bitmap afterImage = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                holder.imageView2.setImageBitmap(afterImage);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("FragmentVotingTask", "download nicht erfolgreich");
                        holder.imageView2.setImageResource(voting.get(position).imageID2);
                    }
                });
            } catch (Exception e) {
                Log.d("FragmentVotingTask", "download nicht erfolgreich");
                holder.imageView2.setImageResource(voting.get(position).imageID2);
            }

            // thumbs up/down
            holder.up.setImageResource(R.drawable.up);
            holder.down.setImageResource(R.drawable.down);

            // get values for likes/dislikes
            votingLikes = voting.get(position).nLikes;
            Log.i("Viki", "Voting get poistion" + voting.get(position));
            Log.i("Viki", "Position" + position);
            votingDislikes = voting.get(position).nDislikes;
            Log.i("KATJA", "nLikes from  DB:" + votingLikes);
            Log.i("KATJA", "nDislikes from DB:" + votingDislikes);

            holder.nLikes.setText(votingLikes);
            holder.nDislikes.setText(votingDislikes);
/*
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
*/
            // set onClickListener for up votes
            holder.up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("KATJA", "------- ONCLICK --------");
                    // prevent downvoting if already upvoted
                    holder.down.setEnabled(false);
                    int positive = Integer.parseInt(votingLikes);
                    Log.i("Viki", "voting Likes: " +  positive);
                    // remove the item if the threshold was reached
                    if (positive + 1 >= votingThreshold) {
                        pathCurrentTask.child("taskVoted").setValue(true);
                        Log.i("Viki", pathCurrentTask.toString());
                        Log.i("KATJA", "position in onClick:"+position);
                        remove(voting.get(position));
                        Toast.makeText(getContext(), "Thanks! \nThe ranger will get his reward!", Toast.LENGTH_LONG).show();
                        // TODO: set the isConfirmed for the task in db to true. Ranger should get his reward.

                    } else {
                        // TODO: on data update write in db
                        //holder.nLikes.setText(String.valueOf(positive + 1));
                        pathCurrentTask.child("numberUpvotes").setValue(positive + 1);
                        Log.i("Viki", "Path to current task in Else" + pathCurrentTask.child("taskId").toString());

                        pathCurrentTask.child("taskVoted").setValue(true);
                        remove(voting.get(position));
                        //prevent clicking multiple times
                       // holder.up.setEnabled(false);

                    }
                }
            });

            // set onClickListener for down votes
            holder.down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // prevent upvoting if already downvoted
                    holder.up.setEnabled(false);
                    int negative = Integer.parseInt(votingDislikes);
                    // remove the item if the threshold was reached
                    if (negative + 1 >= votingThreshold) {
                        remove(voting.get(position));

                        Toast.makeText(getContext(), "The ranger won't get his reward...", Toast.LENGTH_LONG).show();
                        // TODO: set the isConfirmed to false. Ranger wont get his reward :)
                    }

                    else {
                        // TODO: on data update write in db
                        holder.nDislikes.setText(String.valueOf(negative + 1));
                        pathCurrentTask.child("numberDownvotes").setValue(negative + 1);
                        //prevent clicking multiple times
                        holder.down.setEnabled(false);
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            //returns the number of elements the RecyclerView will display
            return voting.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        // Insert a new item to the RecyclerView on a predefined position
        public void insert(int position, VotingTasksListItem listItem) {
            this.voting.add(position, listItem);
            notifyItemInserted(position);
        }

        // Remove a RecyclerView item containing a specified Data object
        public void remove(VotingTasksListItem data) {
            int position = voting.indexOf(data);
            Log.i("Viki", "Position in remove" + position);
            voting.remove(position);
            notifyItemRemoved(position);
        }

        public VotingTasksListItem getItem(int position) {
            return voting.get(position);
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

        // from layout
        CardView cv;
        TextView title;
        TextView location;
        ImageView imageView1;
        ImageView imageView2;
        //Button buttonOk;
        //Button buttonNotOk;


        View_Holder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cvVT);
            title = (TextView) itemView.findViewById(R.id.titleVT);
            location = (TextView) itemView.findViewById(R.id.locationVT);
            // before/after images
            imageView1 = (ImageView) itemView.findViewById(R.id.ivVotingPic1);
            imageView2 = (ImageView) itemView.findViewById(R.id.ivVotingPic2);
        }
    }

}


