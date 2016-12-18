package com.docgenerici.selfbox.models.persistence;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2016.
 *         <p>......</p>
 */

public class ConfigurationApp extends RealmObject {

    @PrimaryKey
    private int id;
    private boolean syncronized;

    public boolean isSyncronized() {
        return syncronized;
    }

    public void setSyncronized(boolean syncronized) {
        this.syncronized = syncronized;
    }
}
