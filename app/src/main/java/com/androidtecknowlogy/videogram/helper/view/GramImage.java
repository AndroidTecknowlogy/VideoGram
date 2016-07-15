package com.androidtecknowlogy.videogram.helper.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by AGBOMA franklyn on 7/15/16.
 */

public class GramImage extends ImageView {

    private final String LOG_TAG = GramImage.class.getSimpleName();

    private int iHeight = 0;
    private int iWidth = 0;

    public GramImage(Context context) {
        super(context);
    }

    public GramImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GramImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(ScaleType.CENTER_CROP);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = getResources().getConfiguration().screenHeightDp;
        int width = getResources().getConfiguration().screenWidthDp;
        Log.e(LOG_TAG, "mobile height = " + height+ " mobile width = " + width);

        this.iHeight = (height / 2) + 100;
        iWidth = height;


        setMeasuredDimension(iWidth, iHeight);
        Log.e(LOG_TAG, "h = " + iHeight+ " w = " + height);
    }
}
