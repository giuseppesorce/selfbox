package com.docgenerici.selfbox.models;

import android.graphics.drawable.Drawable;

/**
 * @author Giuseppe Sorce #@copyright xx 16/10/16.
 */

public class ContentDoc {

  //this model is temporaney
    private int type;
    private String title;
    private Drawable image;
    private boolean shared;


    public ContentDoc(int type, String title, Drawable image) {
        this.type= type;
        this.title= title;
        this.image= image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}
