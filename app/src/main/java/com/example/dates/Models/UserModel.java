package com.example.dates.Models;

import java.io.Serializable;

public class UserModel implements Serializable {

    private String first_name;
    private String last_name;
    private String phone;
    private String password;
    private String address;
    private String gender;

    public UserModel(String first_name, String last_name, String phone, String password, String address, String gender) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.gender = gender;
    }

    public UserModel() {
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
