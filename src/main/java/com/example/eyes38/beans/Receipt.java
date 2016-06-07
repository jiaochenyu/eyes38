package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by huangjiechun on 16/6/6.
 */
public class Receipt implements Serializable {
    private String Receipt_person; //收货姓名
    private String Receipt_phone; //电话
    private String Receipt_pro; // 省
    private String Receipt_city; //市
    private String Receipt_area; //区
    private String Receipt_plot; //小区
    private String Receipt_detail; //自己填的
    private String district ;
    private boolean defaultAddress ;//是否是默认收货地址

    @Override
    public String toString() {
        return "Receipt{" +
                "Receipt_person='" + Receipt_person + '\'' +
                ", Receipt_phone='" + Receipt_phone + '\'' +
                ", Receipt_pro='" + Receipt_pro + '\'' +
                ", Receipt_city='" + Receipt_city + '\'' +
                ", Receipt_area='" + Receipt_area + '\'' +
                ", Receipt_plot='" + Receipt_plot + '\'' +
                ", Receipt_detail='" + Receipt_detail + '\'' +
                '}';
    }

    public Receipt() {
    }

    public Receipt(String receipt_person, String receipt_phone, String receipt_pro, String receipt_city, String receipt_area, String receipt_plot, String receipt_detail) {
        Receipt_person = receipt_person;
        Receipt_phone = receipt_phone;
        Receipt_pro = receipt_pro;
        Receipt_city = receipt_city;
        Receipt_area = receipt_area;
        Receipt_plot = receipt_plot;
        Receipt_detail = receipt_detail;
    }

    public String getReceipt_person() {
        return Receipt_person;
    }

    public void setReceipt_person(String receipt_person) {
        this.Receipt_person = receipt_person;
    }

    public String getReceipt_phone() {
        return Receipt_phone;
    }

    public void setReceipt_phone(String receipt_phone) {
        this.Receipt_phone = receipt_phone;
    }

    public String getReceipt_pro() {
        return Receipt_pro;
    }

    public void setReceipt_pro(String receipt_pro) {
        this.Receipt_pro = receipt_pro;
    }

    public String getReceipt_city() {
        return Receipt_city;
    }

    public void setReceipt_city(String receipt_city) {
        this.Receipt_city = receipt_city;
    }

    public String getReceipt_area() {
        return Receipt_area;
    }

    public void setReceipt_area(String receipt_area) {
        this.Receipt_area = receipt_area;
    }

    public String getReceipt_plot() {
        return Receipt_plot;
    }

    public void setReceipt_plot(String receipt_plot) {
        this.Receipt_plot = receipt_plot;
    }

    public String getReceipt_detail() {
        return Receipt_detail;
    }

    public void setReceipt_detail(String receipt_detail) {
        this.Receipt_detail = receipt_detail;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public boolean isDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    //省、市、区、收货箱、详细地址
    @Override
    public String toString() {
        return "Receipt{" +
                "Receipt_pro='" + Receipt_pro + '\'' +
                ", Receipt_city='" + Receipt_city + '\'' +
                ", Receipt_area='" + Receipt_area + '\'' +
                ", Receipt_detail='" + Receipt_detail + '\'' +
                ", Receipt_plot='" + Receipt_plot + '\'' +
                '}';
    }
}
