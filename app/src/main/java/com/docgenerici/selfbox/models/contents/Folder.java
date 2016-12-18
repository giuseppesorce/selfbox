package com.docgenerici.selfbox.models.contents;

import java.util.ArrayList;
import java.util.List;

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
    public long creationDate;
    public RealmList<ContentBox> contents;


}
