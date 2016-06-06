package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by huangjiechun on 16/6/6.
 */
public class Receipt implements Serializable {
    private String Receipt_person;
    private String Receipt_phone;
    private String Receipt_pro;
    private String Receipt_city;
    private String Receipt_area;
    private String Receipt_plot;
    private String Receipt_detail;

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
}
