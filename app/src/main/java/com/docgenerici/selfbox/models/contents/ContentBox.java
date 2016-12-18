package com.docgenerici.selfbox.models.contents;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by giuseppesorce on 24/11/16.
 */

public class ContentBox extends RealmObject{


    public ContentBox(){

    }
    @PrimaryKey
    public int id;
    public String name;
  //  public String descrFull;
//    public String descrShort;
//    public String textEmail;
//    public String filename;
//    public String checksum;
//    public String thumbnailCover;
//    public String keywords;
//    public long creationDate;
//    public long lastUpdate;
//    public long expirationDate;
//    public boolean alertHighlight;
//    public boolean approved;
//    public boolean visible;
//    public boolean active;
//    public Editor editor;
    public TypeContent type;
//    //public RealmList<Target> targets;


//    public String getType(){
//        return  type.name;
//    }


}
