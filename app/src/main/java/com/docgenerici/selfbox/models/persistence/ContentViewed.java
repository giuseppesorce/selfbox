package com.docgenerici.selfbox.models.persistence;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2016.
 *         <p>......</p>
 */

public class ContentViewed extends RealmObject {

    @PrimaryKey
    public long id;
    public int idcontent;
    public long lastupdate;
}
