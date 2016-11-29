
package com.docgenerici.selfbox.models.medico;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Microbrick extends RealmObject{
    @PrimaryKey
    public int id;
    public String code;
    public String descr;
}
