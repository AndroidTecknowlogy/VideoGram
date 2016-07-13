package com.androidtecknowlogy.videogram.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidtecknowlogy.videogram.R;
import com.androidtecknowlogy.videogram.model.VideoObject;

import java.util.ArrayList;

/**
 * Created by nezspencer on 7/11/16.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private Context context;
    private ArrayList<VideoObject> videoUris;
    private final int FADE_DURATION = 1000;

    public VideoAdapter(Context context, ArrayList<VideoObject> videoUris) {
        this.videoUris=videoUris;
        this.context=context;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.video_item,parent,false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(final VideoHolder holder, final int position) {

        holder.recordedVideo.setVideoURI(videoUris.get(position).getVideoUri());

        /**error listener for videoView to catch all errors
         * this prevents the users from being notified of unnecessary errors*/
        holder.recordedVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return true;
            }
        });

        holder.recordedVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                /*if (holder.recordedVideo.isPlaying())
                {
                    holder.recordedVideo.pause();
                }
                else if (holder.recordedVideo.getCurrentPosition()==0){

                }
                else {holder.recordedVideo.resume();}*/

                Intent playIntent=new Intent(Intent.ACTION_VIEW,videoUris.get(position).getVideoUri());
                playIntent.setDataAndType(videoUris.get(position).getVideoUri(),"video/*");
                context.startActivity(Intent.createChooser(playIntent,"select player"));
                return true;
            }
        });

        holder.recordedBy.setText(videoUris.get(position).getUploadedBy());
        holder.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*add upload function*/
                Toast.makeText(context,"Yet to implement upload function",Toast.LENGTH_LONG).show();
            }
        });

        holder.shareVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent videoShareIntent=new Intent(Intent.ACTION_SEND);
                videoShareIntent.setType("video/*");
                videoShareIntent.putExtra(Intent.EXTRA_STREAM,videoUris
                        .get(position).getVideoUri());
                context.startActivity(Intent.createChooser(videoShareIntent,"select medium"));
            }
        });

        setAnimatedView(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return videoUris.size();
    }

    class VideoHolder extends RecyclerView.ViewHolder{
        VideoView recordedVideo;
        TextView recordedBy;
        ImageButton uploadBtn;
        ImageButton shareVideoBtn;


        public VideoHolder(View itemView) {
            super(itemView);

            recordedVideo=(VideoView)itemView.findViewById(R.id.item_video);
            uploadBtn=(ImageButton)itemView.findViewById(R.id.video_upload_btn);
            recordedBy=(TextView)itemView.findViewById(R.id.text_uploaded_by);
            shareVideoBtn=(ImageButton)itemView.findViewById(R.id.video_share);
        }
    }

    private void setAnimatedView(View view) {
        AlphaAnimation animate = new AlphaAnimation(0.0f, 1.0f);
        animate.setDuration(FADE_DURATION);
        view.startAnimation(animate);
    }
}
