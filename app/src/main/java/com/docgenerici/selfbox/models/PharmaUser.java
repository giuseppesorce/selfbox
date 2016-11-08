package com.docgenerici.selfbox.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Giuseppe Sorce #@copyright xx 06/11/16.
 */

public class PharmaUser implements Parcelable {

    public String name;
    public boolean selected;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    public PharmaUser() {
    }

    protected PharmaUser(Parcel in) {
        this.name = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<PharmaUser> CREATOR = new Parcelable.Creator<PharmaUser>() {
        @Override
        public PharmaUser createFromParcel(Parcel source) {
            return new PharmaUser(source);
        }

        @Override
        public PharmaUser[] newArray(int size) {
            return new PharmaUser[size];
        }
    };
}
