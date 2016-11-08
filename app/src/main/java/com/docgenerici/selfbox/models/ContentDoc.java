package com.docgenerici.selfbox.models;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Giuseppe Sorce #@copyright xx 16/10/16.
 */

public class ContentDoc implements Parcelable {

  //this model is temporaney
    public int type;
    public int typeview;
    public String title;
    public int image;
    public boolean shared;
    public String content;
    public boolean  isnew= false;


    public ContentDoc(int type, String title, int image, String content, int typeview) {
        this.type= type;
        this.title= title;
        this.image= image;
        this.content= content;
        this.typeview= typeview;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.title);
        dest.writeInt(this.image);
        dest.writeByte(this.shared ? (byte) 1 : (byte) 0);
        dest.writeString(this.content);
    }

    protected ContentDoc(Parcel in) {
        this.type = in.readInt();
        this.title = in.readString();
        this.image = in.readInt();
        this.shared = in.readByte() != 0;
        this.content = in.readString();
    }

    public static final Parcelable.Creator<ContentDoc> CREATOR = new Parcelable.Creator<ContentDoc>() {
        @Override
        public ContentDoc createFromParcel(Parcel source) {
            return new ContentDoc(source);
        }

        @Override
        public ContentDoc[] newArray(int size) {
            return new ContentDoc[size];
        }
    };
}
