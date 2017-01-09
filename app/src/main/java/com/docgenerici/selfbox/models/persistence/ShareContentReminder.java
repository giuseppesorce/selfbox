package com.docgenerici.selfbox.models.persistence;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2017.
 *         <p>......</p>
 */

public class ShareContentReminder extends RealmObject{

    @PrimaryKey
    private long id;
    private String reminderShare;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReminderShare() {
        return reminderShare;
    }

    public void setReminderShare(String reminderShare) {
        this.reminderShare = reminderShare;
    }
}
