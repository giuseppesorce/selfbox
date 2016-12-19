package com.docgenerici.selfbox.models.shares;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2016.
 *         <p>......</p>
 */

public class ShareData extends RealmObject{

    @PrimaryKey
    public long id;
    public String isfCode;
    public String doctorCode;
    public String doctorEmail;
    public String emailCustomText;
    public String requestDate;
    public String contentIds;


}
