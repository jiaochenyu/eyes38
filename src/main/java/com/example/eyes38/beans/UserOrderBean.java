package com.example.eyes38.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by weixiao on 2016/6/3.
 */
public class UserOrderBean implements Serializable{
    //我的订单界面主要数据
    private String create_date;//下单日期
    private int order_status_id;//订单状态
    private int total_count;//总共数量（对product循环取值）
    private double total;//总共价格(支付金额)
    private List<UserOrderGoods> mList;
    private String order_no;//订单号
    private String shipping_name;//收货人姓名
    private String shipping_mobile;//收货人手机号
    private String shipping_address;//收货人地址
    private double shipping_freight;//总金额
    private int shipping_district_id;//小区id
    private String order_id;//订单id

    public UserOrderBean(String create_date, int order_status_id, int total_count, double total, List<UserOrderGoods> list, String order_no, String shipping_name, String shipping_mobile, String shipping_address, double shipping_freight, int shipping_district_id, String order_id) {
        this.create_date = create_date;
        this.order_status_id = order_status_id;
        this.total_count = total_count;
        this.total = total;
        mList = list;
        this.order_no = order_no;
        this.shipping_name = shipping_name;
        this.shipping_mobile = shipping_mobile;
        this.shipping_address = shipping_address;
        this.shipping_freight = shipping_freight;
        this.shipping_district_id = shipping_district_id;
        this.order_id = order_id;
    }

    public UserOrderBean() {
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public int getOrder_status_id() {
        return order_status_id;
    }

    public void setOrder_status_id(int order_status_id) {
        this.order_status_id = order_status_id;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<UserOrderGoods> getmList() {
        return mList;
    }

    public void setList(List<UserOrderGoods> list) {
        mList = list;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getShipping_name() {
        return shipping_name;
    }

    public void setShipping_name(String shipping_name) {
        this.shipping_name = shipping_name;
    }

    public String getShipping_mobile() {
        return shipping_mobile;
    }

    public void setShipping_mobile(String shipping_mobile) {
        this.shipping_mobile = shipping_mobile;
    }

    public String getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    public double getShipping_freight() {
        return shipping_freight;
    }

    public void setShipping_freight(double shipping_freight) {
        this.shipping_freight = shipping_freight;
    }

    public int getShipping_district_id() {
        return shipping_district_id;
    }

    public void setShipping_district_id(int shipping_district_id) {
        this.shipping_district_id = shipping_district_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
