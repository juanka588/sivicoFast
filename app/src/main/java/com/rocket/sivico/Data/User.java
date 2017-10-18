package com.rocket.sivico.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JuanCamilo on 28/09/2017.
 */

public class User {
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
}
