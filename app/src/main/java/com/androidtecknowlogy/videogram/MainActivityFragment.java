package com.androidtecknowlogy.videogram;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidtecknowlogy.videogram.adapter.VideoAdapter;
import com.androidtecknowlogy.videogram.model.VideoObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final int VIDEO_CAPTURE_INTENT=2123;
    private RecyclerView videoRecycler;
    private static VideoAdapter videoAdapter;
    private final String USER = "User: "
            + Build.MANUFACTURER.toUpperCase()+" "+ Build.MODEL;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main, container, false);
        FloatingActionButton videoFab = (FloatingActionButton)view.findViewById(R.id.video_fab);

        videoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeVideo();
            }
        });
        videoRecycler=(RecyclerView)view.findViewById(R.id.video_list);

        videoRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL,false));

        videoAdapter=new VideoAdapter(getActivity(),MainActivity.videoUris);
        videoRecycler.setAdapter(videoAdapter);

        return view;
    }

    /**starts an intent to record video
     * this is called by the FloatingActionButton videoFab */
    public void takeVideo()
    {
        Intent videoIntent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (videoIntent.resolveActivity(getActivity().getPackageManager())!=null)
            startActivityForResult(videoIntent,VIDEO_CAPTURE_INTENT);
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
}
