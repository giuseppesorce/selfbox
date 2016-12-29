package com.docgenerici.selfbox.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2016.
 *         <p>......</p>
 */

public class SyncDataCheck extends RealmObject {


    @PrimaryKey
    public int id;
    public int contents;
    public int products;
    public int anagraphic;
    public boolean started;
    public boolean fromhome;


}
