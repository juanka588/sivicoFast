package com.rocket.sivico.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JuanCamilo on 11/10/2017.
 */

public class SubCategory implements Parcelable {
    private String color;
    private String name;
    private String parent;

    public SubCategory() {
    }

    protected SubCategory(Parcel in) {
        color = in.readString();
        name = in.readString();
        parent = in.readString();
    }

    public static final Creator<SubCategory> CREATOR = new Creator<SubCategory>() {
        @Override
        public SubCategory createFromParcel(Parcel in) {
            return new SubCategory(in);
        }

        @Override
        public SubCategory[] newArray(int size) {
            return new SubCategory[size];
        }
    };

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(color);
        parcel.writeString(name);
        parcel.writeString(parent);
    }
}
