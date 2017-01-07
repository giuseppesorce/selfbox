package com.docgenerici.selfbox.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2017.
 *         <p>......</p>
 */

public class ContentShare implements Parcelable {

    public String typeShare;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
