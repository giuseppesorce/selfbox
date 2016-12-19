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
    public String doctorEmail;
    public String emailCustomText;
    public String requestDate;
    public ArrayList<Integer> contentIds;


}
