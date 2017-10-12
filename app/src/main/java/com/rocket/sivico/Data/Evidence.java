package com.rocket.sivico.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JuanCamilo on 12/10/2017.
 */

public class Evidence implements Parcelable {
    private String key;
    private String image;

    public Evidence(String key, String image) {
        this.key = key;
        this.image = image;
    }

    public Evidence() {
    }

    protected Evidence(Parcel in) {
        key = in.readString();
        image = in.readString();
    }

    public static final Creator<Evidence> CREATOR = new Creator<Evidence>() {
        @Override
        public Evidence createFromParcel(Parcel in) {
            return new Evidence(in);
        }

        @Override
        public Evidence[] newArray(int size) {
            return new Evidence[size];
        }
    };

    public String getKey() {
        return key;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(image);
    }
}
