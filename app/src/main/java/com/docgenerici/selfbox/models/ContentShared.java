package com.docgenerici.selfbox.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2017.
 *         <p>......</p>
 */

public class ContentShared implements Parcelable {

    public String id;
    public String name;
    public String type;
    public String path;

    public ContentShared(String id, String name, String type, String path) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(path);
    }

    protected ContentShared(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        path = in.readString();

    }

    public static final Creator<ContentShared> CREATOR = new Creator<ContentShared>() {
        @Override
        public ContentShared createFromParcel(Parcel in) {
            return new ContentShared(in);
        }

        @Override
        public ContentShared[] newArray(int size) {
            return new ContentShared[size];
        }
    };
}
