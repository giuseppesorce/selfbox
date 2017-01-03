package com.docgenerici.selfbox.models;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2017.
 *         <p>......</p>
 */

public class CounterView {

    private String code;
    private long selectionDate;

    public CounterView(String code, long selectionDate) {
        this.code = code;
        this.selectionDate = selectionDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getSelectionDate() {
        return selectionDate;
    }

    public void setSelectionDate(long selectionDate) {
        this.selectionDate = selectionDate;
    }
}
