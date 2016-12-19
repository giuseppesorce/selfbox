package com.docgenerici.selfbox.models.contents;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by giuseppesorce on 24/11/16.
 */

public class ContentBox extends RealmObject {


    public ContentBox() {

    }

    @PrimaryKey
    public int id;
    public String name;
    public String descrFull;
    public String descrShort;
    //    public String textEmail;
    public String filename;
    public String resourcePath;
    public String checksum;
    public String thumbnailCover;
    public String thumbnailPath;
    public String keywords;
    public long creationDate;
    public long lastUpdate;
    public long expirationDate;
    public boolean alertHighlight;
    //    public boolean approved;
    public boolean visible;
      public boolean active;
//    public Editor editor;
    public TypeContent type;
    public RealmList<Target> targets;
    private String localfilePath;
    private String localthumbnailPath;

    public String getLocalfilePath() {
        return localfilePath;
    }

    public void setLocalfilePath(String localfilePath) {
        this.localfilePath = localfilePath;
    }

    public String getLocalthumbnailPath() {
        return localthumbnailPath;
    }

    public void setLocalthumbnailPath(String localthumbnailPath) {
        this.localthumbnailPath = localthumbnailPath;
    }


//    public String getType(){
//        return  type.name;
//    }


}
