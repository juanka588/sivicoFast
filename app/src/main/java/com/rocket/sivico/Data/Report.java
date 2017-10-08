package com.rocket.sivico.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JuanCamilo on 28/09/2017.
 */

public class Report implements Parcelable {
    private String id;
    private long date;
    private String description;
    private List<String> evidences;
    private long lat;
    private long lon;
    private Category category;

    public Report() {
    }

    public Report(String id, String description, Category category) {
        this(id, new Date().getTime(), description, new ArrayList<String>(1), 0, 0, category);
    }

    public Report(String id, long date, String description, List<String> evidences, long lat, long lon, Category category) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.evidences = evidences;
        this.lat = lat;
        this.lon = lon;
        this.category = category;
    }

    protected Report(Parcel in) {
        id = in.readString();
        date = in.readLong();
        description = in.readString();
        evidences = in.createStringArrayList();
        lat = in.readLong();
        lon = in.readLong();
        category = in.readParcelable(Category.class.getClassLoader());
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };

    public String getId() {
        return id;
    }

    public long getTime() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getEvidences() {
        return evidences;
    }

    public long getLat() {
        return lat;
    }

    public long getLon() {
        return lon;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeLong(date);
        parcel.writeString(description);
        parcel.writeStringList(evidences);
        parcel.writeLong(lat);
        parcel.writeLong(lon);
        parcel.writeParcelable(category, i);
    }
}
