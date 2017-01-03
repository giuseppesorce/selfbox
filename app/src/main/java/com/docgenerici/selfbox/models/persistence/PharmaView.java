package com.docgenerici.selfbox.models.persistence;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2017.
 *         <p>......</p>
 */

public class PharmaView extends RealmObject {



    @PrimaryKey
    private int idadd;
    private String code;
    private Date selectionDate;

    public int getIdadd() {
        return idadd;
    }

    public void setIdadd(int idadd) {
        this.idadd = idadd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getSelectionDate() {
        return selectionDate;
    }

    public void setSelectionDate(Date selectionDate) {
        this.selectionDate = selectionDate;
    }
}
