package com.androidtecknowlogy.videogram.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtecknowlogy.videogram.MainActivity;
import com.androidtecknowlogy.videogram.R;
import com.androidtecknowlogy.videogram.fragment.MainActivityFragment;
import com.androidtecknowlogy.videogram.helper.ConnectionReceiver;
import com.androidtecknowlogy.videogram.helper.view.GramImage;
import com.androidtecknowlogy.videogram.helper.view.GramVideo;
import com.androidtecknowlogy.videogram.model.VideoObject;
import com.dd.CircularProgressButton;

import java.util.ArrayList;

/**
 * Created by nezspencer on 7/11/16.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private final String LOG_TAG = VideoHolder.class.getSimpleName();

    private Context context;
    private ArrayList<VideoObject> videoUris;
    private final int FADE_DURATION = 1000;
    private MediaController mController;

    private  ImageButton getUpBtn;
    private  CircularProgressButton getUpCir;

    public VideoAdapter(Context context, ArrayList<VideoObject> videoUris) {
        this.videoUris=videoUris;
        this.context=context;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.video_item,parent,false);

        new ConnectionReceiver().onReceive(context, new Intent());
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(final VideoHolder holder, final int position) {


        MediaController mController = new MediaController(context);
        mController.setAnchorView(holder.recordedVideo);

        holder.recordedVideo.setVisibility(View.INVISIBLE);
        holder.recordedVideo.setMediaController(mController);
        holder.recordedVideo.setVideoURI(videoUris.get(position).getVideoUri());

        /*File file=new File(videoUris.get(position).getVideoUri().getPath());
        Bitmap bitmap=ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(),
                MediaStore.Video.Thumbnails.MINI_KIND);*/

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 1;
        Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(),
                getUriId(videoUris.get(position).getVideoUri()), MediaStore.Video.Thumbnails.MINI_KIND, opt);

        if(bitmap != null){
            Log.e(LOG_TAG, "Bitmap: " + bitmap.toString());
            holder.recordThumbnail.setImageBitmap(bitmap);
        }
        MainActivity.videoThumbnails.add(position,bitmap);

        /**error listener for videoView to catch all errors
         * this prevents the users from being notified of unnecessary errors*/
        holder.recordedVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return true;
            }
        });


        holder.recordThumbnail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                /*if (holder.recordedVideo.isPlaying())
                {
                    holder.recordedVideo.pause();
                }
                else if (holder.recordedVideo.getCurrentPosition()==0){

                }
                else {holder.recordedVideo.resume();}*/

                /*Intent playIntent=new Intent(Intent.ACTION_VIEW,videoUris.get(position).getVideoUri());
                playIntent.setDataAndType(videoUris.get(position).getVideoUri(),"video*//*");
                context.startActivity(Intent.createChooser(playIntent,"select player"))*/;

                /*holder.recordedVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        Log.e(LOG_TAG, "Video duration: "
                                +holder.recordedVideo.getDuration());

                        holder.recordThumbnail.setVisibility(View.INVISIBLE);
                        holder.recordedVideo.setVisibility(View.VISIBLE);
                        holder.recordedVideo.start();

                        if(!holder.recordedVideo.isPlaying()) {
                            holder.recordedVideo.setVisibility(View.INVISIBLE);
                            holder.recordThumbnail.setVisibility(View.VISIBLE);
                        }
                    }

                });*/
                holder.recordThumbnail.setVisibility(View.INVISIBLE);
                holder.recordedVideo.setVisibility(View.VISIBLE);
                holder.recordedVideo.start();

                return true;
            }
        });


        if(!holder.recordedVideo.isPlaying()) {
            holder.recordedVideo.setVisibility(View.INVISIBLE);
            holder.recordThumbnail.setVisibility(View.VISIBLE);
        }

        holder.recordedBy.setText(videoUris.get(position).getUploadedBy());
        holder.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*add upload function*/

                if(!ConnectionReceiver.isConnected()) {
                    Toast.makeText(context, "Not connected, check network settings",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                holder.uploadBtn.setVisibility(View.INVISIBLE);
                holder.upLoadProgress.setVisibility(View.VISIBLE);
                /*holder.upLoadProgress.setIndeterminateProgressMode(true);
                holder.upLoadProgress.setProgress(20);*/

                MainActivityFragment.uploadVideoFile(position, holder.upLoadProgress);

                getUpBtn = holder.uploadBtn;
                getUpCir = holder.upLoadProgress;
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
        //VideoView recordedVideo;
        GramVideo recordedVideo;
        GramImage recordThumbnail;
        TextView recordedBy;
        CircularProgressButton upLoadProgress;
        ImageButton uploadBtn;
        ImageButton shareVideoBtn;


        public VideoHolder(View itemView) {
            super(itemView);

            //recordedVideo=(VideoView)itemView.findViewById(R.id.item_video);
            recordedVideo=(GramVideo) itemView.findViewById(R.id.item_video);
            recordThumbnail = (GramImage) itemView.findViewById(R.id.video_thumbnail);
            upLoadProgress =(CircularProgressButton) itemView.findViewById(R.id.upload_progress);
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

    private long getUriId(Uri uri) {

        if(uri != null){
            String getUri = uri.getPath().trim();

            String[] separator = getUri.split("/");
            int getSeparatorLast = 0;
            for(int count =0; count <separator.length; count ++) {
                getSeparatorLast = count;
            }
            return Long.valueOf(separator[getSeparatorLast]);
        }
        return 0;
    }

    public void stopProgressOnFailed() {

        getUpCir.setProgress(0);
        getUpCir.setVisibility(View.INVISIBLE);
        getUpBtn.setVisibility(View.VISIBLE);

    }

    public void stopProgressOnSuccess() {

        getUpCir.setProgress(100);
        getUpCir.setVisibility(View.INVISIBLE);
        getUpBtn.setVisibility(View.INVISIBLE);

    }
}
