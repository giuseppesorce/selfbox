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
    public String drugStore;
    public String doctorEmail;
    public String emailCustomText;
    public String requestDate;
    public String contentIds;



    /*
    {
  "isfCode": "666",
  "doctorCode": "666",
  "drugstoreCode": null,
  "email": "ruggero.c@selflearning.it",
  "emailCustomText": null,
  "requestDate": 1483792042921,
  "contentIds": [
    1,
    2,
    3,
    4,
    5,
    6
  ],
  "products": [
    {
      "productName": "Prodotto A PDF 1",
      "productUrl": "http://prodottA"
    },
    {
      "productName": "Prodotto A PDF 2",
      "productUrl": "http://prodottB"
    }
  ]
}
     */

}
