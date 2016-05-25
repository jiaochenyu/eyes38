package com.example.eyes38.beans;

/**
 * Created by jqchen on 2016/5/19.
 */
public class CartGoods {
    private String path; //商品图片
    private String title; // 商品标题
    private float price; // 商品价格
    private int num; // 商品数量
    private boolean isChecked; // 是否被选中

    public CartGoods() {
    }

    public CartGoods(String path, String title, float price,int num) {
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
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
