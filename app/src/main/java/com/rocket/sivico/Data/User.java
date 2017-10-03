package com.rocket.sivico.Data;

/**
 * Created by JuanCamilo on 28/09/2017.
 */

public class User {
    private String id;
    private String name;
    private String idNumber;
    private boolean gender;
    private int age;
    private String phone;
    private String municipio;
    private String barrio;

    public User(String name, String idNumber, boolean gender, int age, String phone) {
        this.name = name;
        this.idNumber = idNumber;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
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

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }
}
