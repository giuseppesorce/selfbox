package com.docgenerici.selfbox.models.shares;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2016.
 *         <p>......</p>
 */

public class ShareDataSend {



    public String isfCode;
    public String doctorCode;
    public String drugstoreCode;
    public String email;
    public String emailCustomText;
    public long requestDate;
    public ArrayList<Integer> contentIds;


}
