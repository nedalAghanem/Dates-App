package com.example.dates.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CompanyModel  extends RealmObject {

    private String name;
    private String phone;
    private String start_time;
    private String end_time;
    private String lat;
    private String lng;

    public CompanyModel(String name, String phone, String start_time, String end_time, String lat, String lng) {
        this.name = name;
        this.phone = phone;
        this.start_time = start_time;
        this.end_time = end_time;
        this.lat = lat;
        this.lng = lng;
    }

    public CompanyModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
