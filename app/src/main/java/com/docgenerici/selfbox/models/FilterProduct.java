package com.docgenerici.selfbox.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Giuseppe Sorce #@copyright xx 07/11/16.
 */

public class FilterProduct implements Parcelable {

    public int color;
    public String name;
    public boolean select;

    public FilterProduct(int color, String name){
        this.color= color;
        this.name= name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.color);
        dest.writeString(this.name);
        dest.writeByte(this.select ? (byte) 1 : (byte) 0);
    }

    public FilterProduct() {
    }

    protected FilterProduct(Parcel in) {
        this.color = in.readInt();
        this.name = in.readString();
        this.select = in.readByte() != 0;
    }

    public static final Parcelable.Creator<FilterProduct> CREATOR = new Parcelable.Creator<FilterProduct>() {
        @Override
        public FilterProduct createFromParcel(Parcel source) {
            return new FilterProduct(source);
        }

        @Override
        public FilterProduct[] newArray(int size) {
            return new FilterProduct[size];
        }
    };
}
