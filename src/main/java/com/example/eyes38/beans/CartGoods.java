package com.example.eyes38.beans;

/**
 * Created by jqchen on 2016/5/19.
 */
public class CartGoods {
    private String path; //商品图片
    private String title; // 商品标题 名称
    private double price; // 商品价格
    private int num; // 商品数量
    private int productID;  // 商品id
    private boolean isSelected; // 是否被选中
    //商品信息
    private Goods mGoods;

    public Goods getGoods() {
        return mGoods;
    }

    public void setGoods(Goods goods) {
        mGoods = goods;
    }

    public CartGoods() {
    }

    public CartGoods(String path, String title, double price, int num, int productID, Goods goods) {
        this.path = path;
        this.title = title;
        this.price = price;
        this.num = num;
        this.productID = productID;
        mGoods = goods;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    @Override
    public String toString() {
        return "CartGoods{" +
                "path='" + path + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                '}';
    }
}
