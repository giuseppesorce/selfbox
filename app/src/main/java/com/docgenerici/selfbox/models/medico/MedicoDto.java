package com.docgenerici.selfbox.models.medico;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.annotations.PrimaryKey;

/**
 * @uthor giuseppesorce
 */

public class MedicoDto implements Parcelable{

    public int id;
    public String fullname;
    public String name;
    public String surname;
    public boolean selected;
    public String email;
    public String code;

    public MedicoDto(){

    }

    protected MedicoDto(Parcel in) {
        id = in.readInt();
        fullname = in.readString();
        name = in.readString();
        surname = in.readString();
        selected = in.readByte() != 0;
        email = in.readString();
        code = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fullname);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeString(email);
        dest.writeString(code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MedicoDto> CREATOR = new Creator<MedicoDto>() {
        @Override
        public MedicoDto createFromParcel(Parcel in) {
            return new MedicoDto(in);
        }

        @Override
        public MedicoDto[] newArray(int size) {
            return new MedicoDto[size];
        }
    };
}
