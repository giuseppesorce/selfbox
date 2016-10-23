package com.docgenerici.selfbox.models;

/**
 * @author Giuseppe Sorce #@copyright xx 22/10/16.
 */

public class SyncContent {

    private String title;
    private float percentage;

    public SyncContent(String title, float percentage) {
        this.title = title;
        this.percentage = percentage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}
