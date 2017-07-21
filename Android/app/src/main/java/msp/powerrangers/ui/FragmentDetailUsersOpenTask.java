package msp.powerrangers.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import msp.powerrangers.R;
import msp.powerrangers.logic.Global;
import msp.powerrangers.logic.Ranger;
import msp.powerrangers.logic.User;

import static android.app.Activity.RESULT_OK;

/**
 * Screen to show details of a user's open task and complete it
 */
public class FragmentDetailUsersOpenTask extends Fragment {

    private TextView tvTaskName;
    //private ImageView ivTaskImage;
    private TextView tvTaskDesc;
    private TextView tvActionToUpload;
    private Button buttonUploadImages;
    private ImageView ivUploadedImage;
    private Button buttonCompleteTask;

    private int position;
    private String taskTitle;
    private String taskDescription;
    private Bitmap taskImageBefore;
    private boolean isTaskAlreadyCompleted;
    List<Bitmap> pictureBitmapList;
    String caseImageUrl;
    String taskImageUrl;
    ViewPager viewPager;

    private String taskID;
    private String caseID;

    private Bitmap taskImageAfter;
    private Uri uriAfterImage;
    private static final int CHOOSE_IMAGE_REQUEST = 123;

    // current user
    SharedPreferences sharedPrefs;
    DatabaseReference refPathCurrentUser;
    String userDbID;

    public FragmentDetailUsersOpenTask() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs = getContext().getSharedPreferences(getResources().getString(R.string.sharedPrefs_userDbIdPrefname), 0);
        userDbID = sharedPrefs.getString(getResources().getString(R.string.sharedPrefs_userDbId), null);
        refPathCurrentUser = FirebaseDatabase.getInstance().getReference().child("users").child(userDbID);

        Bundle bundle = getArguments();
        position = bundle.getInt("PositionUsersOpenTask");
        taskTitle = bundle.getString("TitleUsersOpenTask");
        //TODO: key anpassen
        taskID = bundle.getString("OpenTaskID");
        caseID = bundle.getString("OpenTaskCaseID");
        taskDescription = bundle.getString("DescriptionUsersOpenTask");

        taskImageUrl = bundle.getString("taskImageUrl");
        caseImageUrl = bundle.getString("caseImageUrl");

        isTaskAlreadyCompleted = bundle.getBoolean("StatusUsersOpenTask");

        pictureBitmapList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fr_detail_users_open_task, container, false);

        tvTaskName = (TextView) view.findViewById(R.id.taskNameUOT);
        tvTaskName.setText(taskTitle);

        //ivTaskImage = (ImageView) view.findViewById(R.id.taskImageUOT);
        viewPager = (ViewPager) view.findViewById(R.id.taskImagesUOT);
        FragmentDetailUsersOpenTask.ImageAdapter adapter = new FragmentDetailUsersOpenTask.ImageAdapter(this.getContext());
        viewPager.setAdapter(adapter);


        if (taskImageUrl != null) {
            try {
                final File localFileTask = File.createTempFile("images", "jpg");
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference riversRefTask = storageRef.child(Global.getDisplayUrl(taskImageUrl));
                riversRefTask.getFile(localFileTask)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.v("FragmentStart", "download erfolgreich");
                                Bitmap bmp = BitmapFactory.decodeFile(localFileTask.getAbsolutePath());
                                pictureBitmapList.add(bmp);
                                updateImageViews();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("FragmentStart", exception.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.d("FragmentStart", "no image available or some other error occured");
            }
        }






        // neu, wie setze ich das bild
        /*
        if (taskImageUrl != null) {     // no bitmap from bundle

            try {   // download pic before
                final File localFile = File.createTempFile("images", "jpg");
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference riversRef = storageRef.child(Global.getThumbUrl(taskImageUrl));
                riversRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.i("Viki DetailVotingTask", "download erfolgreich, imageBefore");
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                pictureBitmapList.set(0, bitmap);
                                //imageBefore.setImageBitmap(bitmap);
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

*/


/*
        if (taskImageUrl != null) {
            try {
                final File localFileTask = File.createTempFile("images", "jpg");
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference riversRefTask = storageRef.child(Global.getDisplayUrl(taskImageUrl));
                riversRefTask.getFile(localFileTask)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.v("FragmentStart", "download erfolgreich");
                                Bitmap bmp = BitmapFactory.decodeFile(localFileTask.getAbsolutePath());
                                pictureBitmapList.add(bmp);
                                updateImageViews();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("FragmentStart", exception.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.d("FragmentStart", "no image available or some other error occured");
            }
            if (caseImageUrl != null) {
                try {
                    final File localFileCase = File.createTempFile("images", "jpg");
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference riversRefCase = storageRef.child(Global.getDisplayUrl(caseImageUrl));
                    riversRefCase.getFile(localFileCase)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.v("FragmentStart", "download erfolgreich");
                                    Bitmap bmp = BitmapFactory.decodeFile(localFileCase.getAbsolutePath());
                                    pictureBitmapList.add(bmp);
                                    updateImageViews();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("FragmentStart", exception.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Log.d("FragmentStart", "no image available or some other error occured");
                }
            }
        }
        */
        tvTaskDesc = (TextView) view.findViewById(R.id.taskDescUOT);
        tvTaskDesc.setText(taskDescription);

        // hint to upload an image
        tvActionToUpload = (TextView) view.findViewById(R.id.textImagesToUpload);
        ivUploadedImage = (ImageView) view.findViewById(R.id.taskUploadedImageRanger);

        buttonUploadImages = (Button) view.findViewById(R.id.buttonUploadImageUOT);
        buttonUploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        buttonCompleteTask = (Button) view.findViewById(R.id.buttonCompleteTask);
        buttonCompleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeTask();
            }
        });

        //give feedback, when task is already completed
        if (isTaskAlreadyCompleted) {
            buttonUploadImages.setEnabled(false);
            buttonCompleteTask.setEnabled(false);
            tvActionToUpload.setText(getString(R.string.textNoNeedToUpload));
            //TODO: show uploaded picture, if task was already completed
            ivUploadedImage.setVisibility(View.GONE);
        }

        return view;
    }


    public void updateImageViews() {
        viewPager.getAdapter().notifyDataSetChanged();
    }

    /**
     * method to show file chooser for images
     */
    private void showFileChooser() {
        Intent getimageintent = new Intent();
        getimageintent.setType("image/*");
        getimageintent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        getimageintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(getimageintent, getResources().getString(R.string.chooseProfilePicture)), CHOOSE_IMAGE_REQUEST);

    }

    /**
     * method to handle the image chooser activity result:
     * get the picture and upload it
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                uriAfterImage = data.getData();
                taskImageAfter = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriAfterImage);
                ivUploadedImage.setImageBitmap(taskImageAfter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * method to upload case pictures via firebase
     */
    private void completeTask() {
        Toast.makeText(getContext(), "Please wait, while picture is uploaded.", Toast.LENGTH_SHORT).show();

        //upload picture
        final String storageAndDBPath;
        //TODO: pictureName anpassen: Position?!
        storageAndDBPath = "images/cases/" + caseID + "/" + position + "_after.jpg";

        //upload to Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = storageRef.child(storageAndDBPath);
        riversRef.putFile(uriAfterImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //write picture into db and change taskstatus
                        DatabaseReference refTasks = FirebaseDatabase.getInstance().getReference("tasks").child(taskID);
                        refTasks.child(getString(R.string.tasks_taskCompleted)).setValue(true);
                        refTasks.child(getString(R.string.tasks_taskPictureAfter)).setValue(storageAndDBPath);

                        // update bubble user completed tasks
                        refPathCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String currentCount = String.valueOf(dataSnapshot.child("numberCompletedTasks").getValue());
                                int newCount = Integer.valueOf(currentCount) + 1;
                                refPathCurrentUser.child("numberCompletedTasks").setValue(String.valueOf(newCount));

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getContext(), R.string.errorUpload, Toast.LENGTH_LONG).show();
                        Log.d("DetailUsersOpenTask", exception.getMessage());
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });

        Toast.makeText(getContext(), "Task completed! " +
                "You will get your reward after task confirmation.", Toast.LENGTH_LONG).show();

        // move to Main Activity (FragmentStart)
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }


    public class ImageAdapter extends PagerAdapter {
        Context context;

        ImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getItemPosition(Object object) {
            if (pictureBitmapList.contains((View) object)) {
                return pictureBitmapList.indexOf((View) object);
            } else {
                return POSITION_NONE;
            }
        }

        @Override
        public int getCount() {
            return pictureBitmapList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            final Bitmap bmp = pictureBitmapList.get(position);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(bmp);

            imageView.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Dialog builder = new Dialog(getContext());
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                        }
                    });

                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageBitmap(bmp);
                    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    builder.show();
                }
            });

            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                return true;
            default:
                return false;
        }
    }

}