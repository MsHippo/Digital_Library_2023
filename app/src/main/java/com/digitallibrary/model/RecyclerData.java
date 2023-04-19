package com.digitallibrary.model;

import android.graphics.Bitmap;


public class RecyclerData {

    private String title;
    private Bitmap imgid;
    private String link;


    public RecyclerData(String title, Bitmap imgid, String link) {
        this.title = title;
        this.imgid = imgid;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImgid() {
        return imgid;
    }

    public void setImgid(Bitmap imgid) {
        this.imgid = imgid;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link){
        this.link = link;
    }
}