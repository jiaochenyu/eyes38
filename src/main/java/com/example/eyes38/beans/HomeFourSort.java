package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by huangjiechun on 16/5/25.
 */
public class HomeFourSort implements Serializable{
    String category_name ;
    String category_image;

    public HomeFourSort() {
    }

    public HomeFourSort(String category_name, String category_image) {
        this.category_name = category_name;
        this.category_image = category_image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }
}
