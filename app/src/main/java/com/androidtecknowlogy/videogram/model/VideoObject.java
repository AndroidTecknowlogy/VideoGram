package com.androidtecknowlogy.videogram.model;

import android.net.Uri;

/**
 * Created by nezspencer on 7/11/16.
 */
public class VideoObject {

    private Uri videoUri;
    private String uploadedBy;

    public VideoObject(Uri videoUri, String uploadedBy) {
        this.videoUri = videoUri;
        this.uploadedBy = uploadedBy;
    }

    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
