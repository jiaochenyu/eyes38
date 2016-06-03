package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by huangjiechun on 16/6/2.
 */
public class Home_district implements Serializable{
    String CityName;
    int district_id;

    public Home_district(String cityName, int district_id) {
        CityName = cityName;
        this.district_id = district_id;
    }

    public Home_district() {
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        this.CityName = cityName;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }
}
