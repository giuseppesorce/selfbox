package com.docgenerici.selfbox.models.contents;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by giuseppesorce on 24/11/16.
 */

public class Folder extends RealmObject{

    public Folder(){

    }
    @PrimaryKey
    public int id;
    public String name;
    public String path;
    public String cover;
    public String coverPath;
    public long creationDate;
    private String coverUri;
    public RealmList<ContentBox> contents;


    public String getCoverUri() {
        return coverUri;
    }

    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }
}


