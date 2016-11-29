
package com.docgenerici.selfbox.models.farmacia;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Cluster extends RealmObject{
    @PrimaryKey
    public int id;
    public String code;
    public String descr;

}
