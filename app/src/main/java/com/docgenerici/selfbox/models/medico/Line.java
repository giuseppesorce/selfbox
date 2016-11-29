
package com.docgenerici.selfbox.models.medico;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Line extends RealmObject{
    @PrimaryKey
    public int id;
    public String code;
    public String shortCode;
    public String name;


}
