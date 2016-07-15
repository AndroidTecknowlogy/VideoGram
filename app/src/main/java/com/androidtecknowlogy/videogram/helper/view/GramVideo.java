package com.androidtecknowlogy.videogram.helper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.VideoView;

/**
 * Created by AGBOMA franklyn on 7/14/16.
 */

public class GramVideo extends VideoView{

    private final String LOG_TAG = GramVideo.class.getSimpleName();

    private Context context;
    private int vHeight = 0;
    private int vWidth = 0;


    public GramVideo(Context context) {
        super(context);
    }

    public GramVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GramVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setScreenSize(int height, int width) {
        vHeight = height;
        vWidth = width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getResources().getConfiguration().screenHeightDp;
        int width = getResources().getConfiguration().screenWidthDp;
        Log.e(LOG_TAG, "mobile height = " + height+ " mobile width = " + width);

        this.vHeight = (height / 2) + 100;
        vWidth = height;


        setMeasuredDimension(vWidth, vHeight);
        Log.e(LOG_TAG, "h = " + vHeight+ " w = " + height);
    }


    /*@Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        getHolder().setSizeFromLayout();
    }*/

    /*public ViewGroup.LayoutParams setViewParams(int vWidth, int vHeight,
                                                 int screeWidth, int screeHeight, VideoGram vGram) {

        float videoProportion = (float) vWidth / (float) vHeight;
        float screeProportion = (float) screeWidth / (float) screeHeight;
        android.view.ViewGroup.LayoutParams lp = vGram.getLayoutParams();

        if(videoProportion > screeProportion) {
            lp.width = screeWidth;
            lp.height = (int) ((float) screeWidth / videoProportion);
        }
        else {
            lp.width = (int) ( videoProportion * (float) screeWidth);
            lp.height = screeHeight;
        }

        return lp;
    }*/
}
