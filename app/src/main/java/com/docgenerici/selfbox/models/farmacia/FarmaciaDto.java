package com.docgenerici.selfbox.models.farmacia;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @uthor giuseppesorce
 */

public class FarmaciaDto implements Parcelable {
    public int id;
    public String fullname;
    public String type;
    public boolean selected;
    public String email;
    public String ente;

    public FarmaciaDto() {
    }


    protected FarmaciaDto(Parcel in) {
        id = in.readInt();
        fullname = in.readString();
        type = in.readString();
        selected = in.readByte() != 0;
        email = in.readString();
        ente = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fullname);
        dest.writeString(type);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeString(email);
        dest.writeString(ente);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FarmaciaDto> CREATOR = new Creator<FarmaciaDto>() {
        @Override
        public FarmaciaDto createFromParcel(Parcel in) {
            return new FarmaciaDto(in);
        }

        @Override
        public FarmaciaDto[] newArray(int size) {
            return new FarmaciaDto[size];
        }
    };
}
