package com.docgenerici.selfbox.models.contents;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by giuseppesorce on 24/11/16.
 */

public class Target extends RealmObject{

    @PrimaryKey
    public int id;
    public String name;
    public String code;
}
