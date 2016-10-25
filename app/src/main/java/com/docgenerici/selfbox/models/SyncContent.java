package com.docgenerici.selfbox.models;

/**
 * @author Giuseppe Sorce #@copyright xx 22/10/16.
 */

public class SyncContent {

    private String title;
    private int percentage;

    public SyncContent(String title, int percentage) {
        this.title = title;
        this.percentage = percentage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
