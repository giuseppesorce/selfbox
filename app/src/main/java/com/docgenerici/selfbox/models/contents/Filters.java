package com.docgenerici.selfbox.models.contents;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2016.
 *         <p>......</p>
 */

public class Filters implements Parcelable {

    public boolean video;
    public boolean videoIntervista;
    public boolean eVisual;
    public boolean documents;
    public boolean alertHighlight;
    public boolean all;


    public Filters() {

    }
    public Filters(boolean all) {
        video= true;
        videoIntervista= true;
        eVisual= true;
        documents= true;
        alertHighlight= true;
        this.all= true;
    }

    protected Filters(Parcel in) {
        video = in.readByte() != 0;
        videoIntervista = in.readByte() != 0;
        eVisual = in.readByte() != 0;
        documents = in.readByte() != 0;
        alertHighlight = in.readByte() != 0;
        all = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeByte((byte) (videoIntervista ? 1 : 0));
        dest.writeByte((byte) (eVisual ? 1 : 0));
        dest.writeByte((byte) (documents ? 1 : 0));
        dest.writeByte((byte) (alertHighlight ? 1 : 0));
        dest.writeByte((byte) (all ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Filters> CREATOR = new Creator<Filters>() {
        @Override
        public Filters createFromParcel(Parcel in) {
            return new Filters(in);
        }

        @Override
        public Filters[] newArray(int size) {
            return new Filters[size];
        }
    };
}
