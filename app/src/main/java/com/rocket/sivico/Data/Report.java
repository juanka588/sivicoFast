package com.rocket.sivico.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.maps.android.clustering.ClusterItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JuanCamilo on 28/09/2017.
 */

public class Report implements Parcelable, ClusterItem {
    private String date;
    private String description;
    private String lat;
    private String lon;
    private String category;
    private String owner;
    private String color;
    private long score;
    private List<Evidence> evidencesList;
    private Map<String, Object> evidence;

    public Report() {
    }

    public Report(DataSnapshot dataSnapshot) {
        this.date = (String) dataSnapshot.child("date").getValue();
        this.description = (String) dataSnapshot.child("description").getValue();
        this.lat = (String) dataSnapshot.child("lat").getValue();
        this.lon = (String) dataSnapshot.child("lon").getValue();
        this.category = (String) dataSnapshot.child("category").getValue();
        this.owner = (String) dataSnapshot.child("owner").getValue();
        this.color = (String) dataSnapshot.child("color").getValue();
        if (dataSnapshot.child("score").getValue() == null) {
            this.score = 0;
        } else {
            this.score = (Long) dataSnapshot.child("score").getValue();
        }
        this.evidence = (Map<String, Object>) dataSnapshot.child("evidence").getValue();
    }

    public Report(String date, String description, String lat, String lon
            , String category, String owner, String color) {
        this.date = date;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.category = category;
        this.owner = owner;
        this.color = color;
    }

    public Report(Parcel in) {
        date = in.readString();
        description = in.readString();
        lat = in.readString();
        lon = in.readString();
        category = in.readString();
        owner = in.readString();
        color = in.readString();
        score = in.readLong();
        evidencesList = in.createTypedArrayList(Evidence.CREATOR);
        if (getEvidence() == null) {
            this.evidence = new HashMap<>();
            for (Evidence e : evidencesList) {
                this.evidence.put(e.getKey(), e.getImage());
            }
        }
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
        parcel.writeLong(score);
        if (getEvidence() != null) {
            evidencesList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : getEvidence().entrySet()) {
                evidencesList.add(new Evidence(entry.getKey(), entry.getValue().toString()));
            }
        }
        parcel.writeTypedList(evidencesList);
    }

    public void addEvidence(String newEvidence) {
        if (evidence == null) {
            this.evidence = new HashMap<>();
            this.evidence.put("img1", newEvidence);
        } else {
            int size = evidence.size();
            this.evidence.put("img" + size, newEvidence);
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("date", date);
        mMap.put("description", description);
        mMap.put("lat", lat);
        mMap.put("lon", lon);
        mMap.put("category", category);
        mMap.put("owner", owner);
        mMap.put("color", color);
        mMap.put("score", score);
        mMap.put("evidence", evidence);
        return mMap;
    }

    public long getScore() {
        return score;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(Double.parseDouble(this.getLat()), Double.parseDouble(this.getLon()));
    }
}