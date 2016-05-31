package com.example.eyes38.beans;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by jqchen on 2016/5/19.
 */
public class CartGoods implements Serializable{
    private String path; //商品图片
    private String title; // 商品标题
    private float price; // 商品价格
    private int num; // 商品数量
    private boolean isSelected; // 是否被选中
<<<<<<< HEAD
=======
    private int shopping_cart_id ; // 购物车id
    //商品信息
    private Goods mGoods;

    public Goods getGoods() {
        return mGoods;
    }

    public void setGoods(Goods goods) {
        mGoods = goods;
    }
>>>>>>> c7cfbc72c6095a8db55b39ef93468236f5e10028

    public CartGoods() {
    }

<<<<<<< HEAD
    public CartGoods(String path, String title, float price,int num) {
=======
    public CartGoods(String path, String title, double price, int num, int productID, Goods goods) {
        DecimalFormat df = new DecimalFormat("0.00");
>>>>>>> c7cfbc72c6095a8db55b39ef93468236f5e10028
        this.path = path;
        this.title = title;
        this.price = price;
        this.num = num;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

<<<<<<< HEAD
=======
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getShopping_cart_id() {
        return shopping_cart_id;
    }

    public void setShopping_cart_id(int shopping_cart_id) {
        this.shopping_cart_id = shopping_cart_id;
    }

>>>>>>> c7cfbc72c6095a8db55b39ef93468236f5e10028
    @Override
    public String toString() {
        return "CartGoods{" +
                "path='" + path + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
<<<<<<< HEAD
=======
                ", num=" + num +
                ", productID=" + productID +
                ", isSelected=" + isSelected +
                ", shopping_cart_id=" + shopping_cart_id +
                ", mGoods=" + mGoods +
>>>>>>> c7cfbc72c6095a8db55b39ef93468236f5e10028
                '}';
    }
}
