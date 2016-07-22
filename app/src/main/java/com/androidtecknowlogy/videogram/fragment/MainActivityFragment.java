package com.androidtecknowlogy.videogram.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidtecknowlogy.videogram.MainActivity;
import com.androidtecknowlogy.videogram.R;
import com.androidtecknowlogy.videogram.adapter.VideoAdapter;
import com.androidtecknowlogy.videogram.model.StoreHelper;
import com.androidtecknowlogy.videogram.model.VideoObject;
import com.androidtecknowlogy.videogram.util.Constants;
import com.dd.CircularProgressButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final int VIDEO_CAPTURE_INTENT=2123;
    private static final int VIDEO_WATCH=2124;
    private RecyclerView videoRecycler;
    private static VideoAdapter videoAdapter;
    private static String USER;

    private static StorageReference mStorageReference;
    private static StorageReference mVideoStorage;
    private static StorageReference mThumbnailRef;

    //firebaseDatabase
    private static FirebaseDatabase mFirebaseDatabase;
    private static DatabaseReference mDatabaseReference;

    private static FragmentActivity mActivity;
    private static MainActivityFragment instance;
    private static String username;


    /*all intents declared*/
    private Intent videoIntent;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main, container, false);

        if (getActivity()!=null &&
                ((AppCompatActivity)getActivity()).getSupportActionBar()!=null &&
                !((AppCompatActivity) getActivity()).getSupportActionBar().isShowing())
        {
            ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        }

        FloatingActionButton videoFab = (FloatingActionButton)view.findViewById(R.id.video_fab);
        mStorageReference=MainActivity.mFirebaseStorage.getReferenceFromUrl(Constants.STORAGE_URL);
        mVideoStorage=mStorageReference.child("videos");
        mThumbnailRef=mStorageReference.child("images");
        instance=this;

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mFirebaseDatabase.getReference().child("storage data");


        //setupAndroidMpermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        username= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString
                (Constants.USERNAME,"Anonymous");

        USER="Uploaded by: "+username;

        videoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeVideo();
            }
        });
        videoRecycler=(RecyclerView)view.findViewById(R.id.video_list);

        videoRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL,false));

        videoAdapter=new VideoAdapter(getActivity(), MainActivity.videoUris);
        videoRecycler.setAdapter(videoAdapter);

        videoIntent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        return view;
    }


    /**starts an intent to record video
     * this is called by the FloatingActionButton videoFab */
    public void takeVideo()
    {
        if (videoIntent.resolveActivity(getActivity().getPackageManager())!=null)
            startActivityForResult(videoIntent,VIDEO_CAPTURE_INTENT);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(FragmentActivity)activity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("TAG"," start video");

        if (requestCode==VIDEO_CAPTURE_INTENT && resultCode==getActivity().RESULT_OK)
        {
            Log.e("TAG"," ok video");
            Uri videoUri=data.getData();
            MainActivity.videoUris.add(new VideoObject(videoUri, USER));
            videoAdapter.notifyDataSetChanged();

        }
        Log.e("TAG"," after video");
    }

    public static void chooseVideoPlayer(Intent intent){
        if (intent.resolveActivity(mActivity.getPackageManager())!=null)
            mActivity.startActivityForResult(intent,VIDEO_WATCH);
    }

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getActivity().getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);

            *//*new VideoGram(getActivity()).setDimension(700,700)
                    .getHolder().setFixedSize(700, 700);*//*
        }
        else {
            getActivity().getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

            *//*new VideoGram(getActivity()).setDimension(700,700)
                    .getHolder().setFixedSize(700, 700);*//*
        }
    }*/

    @TargetApi(23)
    public void setupAndroidMpermissions(String permissionName){
        if (getActivity().checkSelfPermission(permissionName)!= PackageManager.PERMISSION_GRANTED)
        {
            switch (permissionName)
            {
                case Manifest.permission.CAMERA:
                    getActivity().requestPermissions(new String[]{permissionName},
                            Constants.RUNTIME_CAMERA_PERMISSION);
                    break;
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    getActivity().requestPermissions(new String[]{permissionName},
                            Constants.RUNTIME_SDCARD_PERMISSION);
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==Constants.RUNTIME_CAMERA_PERMISSION)
        {
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                startActivityForResult(videoIntent,VIDEO_CAPTURE_INTENT);
            }
            else {
                Toast.makeText(getActivity(),"You need to accept the camera permission request",
                        Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode==Constants.RUNTIME_SDCARD_PERMISSION)
        {
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getActivity(),"Granted!",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(),"You need to accept the camera permission request",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**this uploads a video file and corresponding thumbnail to server
     * this is called from the adapter class.  It takes 2 parameters
     * @param position the position on the list of the video item to be uploaded
     * @param progressView the progressBar that shows the upload progress*/
    public static synchronized void uploadVideoFile(final int position, final CircularProgressButton
            progressView){

        Bitmap bitmap=MainActivity.videoThumbnails.get(position);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] data=baos.toByteArray();

        final String pathName=username+"_"+MainActivity.videoUris
                .get(position)
                .getVideoUri().getLastPathSegment();

        progressView.setIndeterminateProgressMode(true);
        progressView.setProgress(40);

        final StorageReference imageRef=mThumbnailRef.child(pathName);

        UploadTask imageUploadTask=imageRef.putBytes(data);
        imageUploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                progressView.setProgress(0);
                progressView.setIndeterminateProgressMode(false);

                StorageMetadata videoData=new StorageMetadata.Builder()
                        .setContentType("video/*")
                        .setCustomMetadata(Constants.METADATA_UPLOADED_BY,username)
                        .build();

                final StorageReference videoFile=mVideoStorage.child(pathName);
                final UploadTask videoUploadTask=videoFile.putFile(MainActivity.videoUris.get(position)
                        .getVideoUri(),videoData);
                videoUploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                         int progress = (int)(100* taskSnapshot.getBytesTransferred()
                                 /taskSnapshot.getTotalByteCount());

                        progressView.setProgress(progress);
                        progressView.setVisibility(progress >2 ? View.VISIBLE : View.INVISIBLE);
                    }
                });

                videoUploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        instance.storeFileMetaData(imageRef.getDownloadUrl().toString(),videoFile
                                .getDownloadUrl().toString());


                    }
                });

                videoUploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        videoAdapter.stopProgressOnFailed();
                        Log.e("failedMain","here");
                        Toast.makeText(mActivity,"Upload failed\nRetry",Toast.LENGTH_LONG).show();
                    }
                });



            }
        });

        imageUploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mActivity,"Upload failed\nRetry",Toast.LENGTH_LONG).show();
                videoAdapter.stopProgressOnFailed();
                Log.e("failedMain","here2");
            }
        });
    }

    public static synchronized void downloadVideoThumbnails(int position)
    {
        /*get list of all uploaded files*/

        /*download files one by one*/
    }

    public File createImageFolder(String folderName)
    {
        File file=new File(getActivity().getApplicationContext().getFilesDir(),folderName);

        if (!file.exists())
        {
            file.mkdir();
        }

        return file;
    }

    public void saveDownloadedImage(String directory,String imageName,Bitmap imageToSave)
    {
        File file=new File(directory,imageName);
        if (file.exists())
            file.delete();

        try {
            FileOutputStream outputStream=new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        }

    public void storeFileMetaData(String imageUrl,String videoUrl)
    {
        String key=mDatabaseReference.push().getKey();
        StoreHelper helper=new StoreHelper(imageUrl,videoUrl);
        Map<String,Object> item=helper.toMap();
        Map<String,Object> childUpdate=new HashMap<>();
        childUpdate.put("/"+key+"/",item);

        mDatabaseReference.updateChildren(childUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(),"success",Toast.LENGTH_SHORT).show();
                videoAdapter.stopProgressOnSuccess();
            }
        });
    }

    }



