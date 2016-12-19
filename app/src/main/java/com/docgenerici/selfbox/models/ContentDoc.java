package com.docgenerici.selfbox.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Giuseppe Sorce #@copyright xx 16/10/16.
 */

public class ContentDoc implements Parcelable {


    public int id;
    public int type;
    public int typeview;
    public String name;
    public int image;
    public boolean shared;
    public String content;
    public boolean  isnew= false;
    public String cover;
    public boolean isFolder;


    public ContentDoc(int type, String title, int image, String content, int typeview, String cover) {
        this.type= type;
        this.name = title;
        this.image= image;
        this.content= content;
        this.typeview= typeview;
        this.cover= cover;
    }

    public ContentDoc(int type, String title, int image, String content, int typeview, String cover, boolean isFolder) {
        this.type= type;
        this.name = title;
        this.image= image;
        this.content= content;
        this.typeview= typeview;
        this.cover= cover;
        this.isFolder= isFolder;
    }

    public ContentDoc() {

    }

    protected ContentDoc(Parcel in) {
        id = in.readInt();
        type = in.readInt();
        typeview = in.readInt();
        name = in.readString();
        image = in.readInt();
        shared = in.readByte() != 0;
        content = in.readString();
        isnew = in.readByte() != 0;
        cover = in.readString();
    }

    public static final Creator<ContentDoc> CREATOR = new Creator<ContentDoc>() {
        @Override
        public ContentDoc createFromParcel(Parcel in) {
            return new ContentDoc(in);
        }

        @Override
        public ContentDoc[] newArray(int size) {
            return new ContentDoc[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(type);
        dest.writeInt(typeview);
        dest.writeString(name);
        dest.writeInt(image);
        dest.writeByte((byte) (shared ? 1 : 0));
        dest.writeString(content);
        dest.writeByte((byte) (isnew ? 1 : 0));
        dest.writeString(cover);
    }
}
