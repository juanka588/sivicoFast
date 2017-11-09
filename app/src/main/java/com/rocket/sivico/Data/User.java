package com.rocket.sivico.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JuanCamilo on 28/09/2017.
 */

public class User implements Parcelable {
    private String id;
    private String name;
    private String idNumber;
    private boolean gender;
    private String birthday;
    private String phone;
    private String region;
    private String neighborhood;
    private String email;
    private String photo;
    private int score;

    public User(String id, String name, String idNumber, boolean gender, String birthday,
                String phone, String region, String neighborhood,
                String email, String photo, int score) {
        this.id = id;
        this.name = name;
        this.idNumber = idNumber;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.region = region;
        this.neighborhood = neighborhood;
        this.email = email;
        this.photo = photo;
        this.score = score;
    }


    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        idNumber = in.readString();
        gender = in.readByte() != 0;
        birthday = in.readString();
        phone = in.readString();
        region = in.readString();
        neighborhood = in.readString();
        email = in.readString();
        photo = in.readString();
        score = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public void setScore(int score) {
        this.score = score;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getScore() {
        return score;
    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getEmail() {
        return email;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("name", getName());
        mMap.put("phone", getPhone());
        mMap.put("birthday", getBirthday());
        mMap.put("gender", isGender());
        mMap.put("region", getRegion());
        mMap.put("neighborhood", getNeighborhood());
        mMap.put("email", getEmail());
        mMap.put("photo", getPhoto());
        mMap.put("id_number", getIdNumber());
        mMap.put("points", getScore());
        return mMap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(idNumber);
        parcel.writeByte((byte) (gender ? 1 : 0));
        parcel.writeString(birthday);
        parcel.writeString(phone);
        parcel.writeString(region);
        parcel.writeString(neighborhood);
        parcel.writeString(email);
        parcel.writeString(photo);
        parcel.writeInt(score);
    }

    public void setAnonymous(boolean anonymous) {
        this.name = "Anonymous";
        this.idNumber = "000000";
        this.birthday = "000000";
        this.phone = "000000";
        this.region = "000000";
        this.neighborhood = "000000";
        this.email = "000000";
        this.photo = "no-pic";
    }
}
