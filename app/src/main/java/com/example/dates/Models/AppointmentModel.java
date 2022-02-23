package com.example.dates.Models;

import java.io.Serializable;
import java.lang.reflect.Array;

import io.realm.RealmObject;

public class AppointmentModel extends RealmObject implements Serializable {

  //  int[] array = new int[5];


    private String company;
    private String date;
    private String time;
    private String details;

    public AppointmentModel(String company, String date, String time, String details) {
        this.company = company;
        this.date = date;
        this.time = time;
        this.details = details;
    }

    public AppointmentModel() {}

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
