package com.rocket.sivico.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by JuanCamilo on 28/09/2017.
 */

public class Report implements Parcelable {
    private String date;
    private String description;
    private String lat;
    private String lon;
    private String category;
    private String owner;
    private String color;
    private List<Evidence> evidencesList;
    private Map<String, Object> evidence;

    public Report() {
    }

    protected Report(Parcel in) {
        date = in.readString();
        description = in.readString();
        lat = in.readString();
        lon = in.readString();
        category = in.readString();
        owner = in.readString();
        color = in.readString();
        evidencesList = in.createTypedArrayList(Evidence.CREATOR);
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

    public List<Evidence> getEvidencesList() {
        return evidencesList;
    }

    public String getColor() {
        return color;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getCategory() {
        return category;
    }

    public String getOwner() {
        return owner;
    }

    public Map<String, Object> getEvidence() {
        return evidence;
    }


    public LatLng getLatLng() {
        return new LatLng(Double.parseDouble(this.getLat()), Double.parseDouble(this.getLon()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(description);
        parcel.writeString(lat);
        parcel.writeString(lon);
        parcel.writeString(category);
        parcel.writeString(owner);
        parcel.writeString(color);
        if (getEvidence() != null) {
            evidencesList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : getEvidence().entrySet()) {
                evidencesList.add(new Evidence(entry.getKey(), entry.getValue().toString()));
            }
        }
        parcel.writeTypedList(evidencesList);
    }
}