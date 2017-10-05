package com.rocket.sivico.Data;

/**
 * Created by JuanCamilo on 28/09/2017.
 */

public class User {
    private String id;
    private String name;
    private String lastName;
    private String idNumber;
    private boolean gender;
    private int age;
    private String phone;
    private String region;
    private String neighborhood;
    private String email;


    public User(String id, String name, String lastName,
                String idNumber, boolean gender, int age,
                String phone, String region, String neighborhood,
                String email) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.idNumber = idNumber;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.region = region;
        this.neighborhood = neighborhood;
        this.email = email;
    }

    public String getLastName() {
        return lastName;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
}
