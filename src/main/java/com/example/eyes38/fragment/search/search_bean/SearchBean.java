package com.example.eyes38.fragment.search.search_bean;

/**
 * Created by weixiao on 2016/5/26.
 */
public class SearchBean {
    private String pic;
    private String name;
    private float price;
    private String size;

    public SearchBean() {
    }

    public SearchBean(String pic, String name, float price, String size) {
        this.pic = pic;
        this.name = name;
        this.price = price;
        this.size = size;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
