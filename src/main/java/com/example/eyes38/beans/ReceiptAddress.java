package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by huangjiechun on 16/6/3.
 */
public class ReceiptAddress implements Serializable{
    /**收货地址的javabean
     * address_id : 36
     * customer_id : 19
     * firstname : 黄捷春
     * lastname : null
     * company : null
     * mobile : 11111111111
     * phone :
     * address_1 : 文萃211号
     * address_2 : null
     * district_id : 86
     * city_id : 0
     * postcode :
     * country_id : 44
     * zone_id : 0
     * create_date : 2016-06-03 13:44:30
     * update_date : 2016-06-03 13:44:30
     * version : 1
     * create_by : 13091617887
     * update_by : null
     * district : 江苏省 苏州市 姑苏区
     */

    private String address_id;
    private String customer_id;
    private String firstname;
    private String mobile;
    private String address_1; //自己填的详细地址
    private String district_id; // 地区编号
    private String country_id; //
    private String version; //
    private String create_by; // 地址电话
    private Object update_by; //
    private String district; // 三级联动
        private int zone_id;

    public int getZone_id() {
        return zone_id;
    }

    public void setZone_id(int zone_id) {
        this.zone_id = zone_id;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public Object getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(Object update_by) {
        this.update_by = update_by;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

}
