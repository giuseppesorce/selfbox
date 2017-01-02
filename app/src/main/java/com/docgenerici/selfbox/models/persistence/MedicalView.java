package com.docgenerici.selfbox.models.persistence;



import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2017.
 *         <p>......</p>
 */

public class MedicalView extends RealmObject {



    @PrimaryKey
    private int idadd;
    private String id;
    private Date d;

    public int getIdadd() {
        return idadd;
    }

    public void setIdadd(int idadd) {
        this.idadd = idadd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getD() {
        return d;
    }

    public void setD(Date d) {
        this.d = d;
    }
}
