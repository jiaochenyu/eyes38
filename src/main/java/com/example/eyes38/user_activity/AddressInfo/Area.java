package com.example.eyes38.user_activity.AddressInfo;

import java.io.Serializable;

/**
 * Created by weixiao on 2016/5/24.
 */
public class Area implements Serializable{
    private String province;
    private String city;
    private String county;
    public Area(){

    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    @Override
    public String toString() {
        return "Area{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                '}';
    }
}
