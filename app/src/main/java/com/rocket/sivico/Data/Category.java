package com.rocket.sivico.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JuanCamilo on 28/09/2017.
 */

public class Category implements Parcelable {
    private String id;
    private String name;
    private String color;
    private String icon;
    private String description;

    public Category() {
    }

    protected Category(Parcel in) {
        id = in.readString();
        name = in.readString();
        color = in.readString();
        icon = in.readString();
        description = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(color);
        parcel.writeString(icon);
        parcel.writeString(description);
    }
}
