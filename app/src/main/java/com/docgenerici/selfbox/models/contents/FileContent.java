package com.docgenerici.selfbox.models.contents;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @uthor giuseppesorce
 */

public class FileContent extends RealmObject {
    @PrimaryKey
    public int id;
}
