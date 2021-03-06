package com.docgenerici.selfbox.models.persistence;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by giuseppesorce on 24/11/16.
 */

public class InfoApp extends RealmObject {
    @PrimaryKey
    public String repCode;
    public String name;
    public String surname;
    public String sessionId;
    public String selfBoxContentDownloadUrl;
    public String selfBoxProductDownloadUrl;
    public String selfBoxIsfDrugstoreDownloadUrl;
    public String line;
    public String lineShortCode;
    public String lineCode;
    public long lastUpdate;
    public boolean result;

}
