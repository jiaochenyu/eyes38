package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by weixiao on 2016/6/4.
 */
public class UserOrderGoods implements Serializable{
    private String image;//物品图片
    private String name;//物品名字
    private double price;//物品单价
    private int quantity;//物品数量
    private String order_id;//标识哪种物品

    public UserOrderGoods() {
    }

    public UserOrderGoods(String image, String name, double price, int quantity, String order_id) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.order_id = order_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
