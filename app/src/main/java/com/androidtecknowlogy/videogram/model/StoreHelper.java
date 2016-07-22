package com.androidtecknowlogy.videogram.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nezspencer on 7/18/16.
 */
public class StoreHelper {

    private String videoUrl;
    private String imageUrl;

    public StoreHelper() {
    }

    public StoreHelper(String videoUrl, String imageUrl) {
        this.videoUrl = videoUrl;
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result=new HashMap<>();

        result.put("videoUrl",videoUrl);
        result.put("imageUrl",imageUrl);

        return result;
    }
}
