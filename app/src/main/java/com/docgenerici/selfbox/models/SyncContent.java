package com.docgenerici.selfbox.models;

/**
 * @author Giuseppe Sorce #@copyright xx 22/10/16.
 */

public class SyncContent {

    private String title;
    private int percentage;
    private int type;

    public SyncContent(String title, int percentage, int type) {
        this.title = title;
        this.percentage = percentage;
        this.type= type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
