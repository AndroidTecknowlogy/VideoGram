package com.androidtecknowlogy.videogram.model;

/**
 * Created by nezspencer on 7/18/16.
 */
public class MyStore {

    private String itemID;
    private StoreHelper detail;

    public MyStore(String itemID, String videoUrl, String imageUrl) {
        this.itemID = itemID;
        this.detail = new StoreHelper(videoUrl,imageUrl);
    }

    public MyStore() {
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public StoreHelper getDetail() {
        return detail;
    }

    public void setDetail(String videoUrl, String imageUrl) {
        this.detail = new StoreHelper(videoUrl,imageUrl);
    }
}
