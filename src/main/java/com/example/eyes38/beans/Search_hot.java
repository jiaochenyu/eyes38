package com.example.eyes38.beans;

/**
 * Created by weixiao on 2016/6/10.
 */
public class Search_hot {
    private String name;//热门名称
    private int num;//热门数量
    private String image;//热门前三图片

    public Search_hot() {
    }

    public Search_hot(String name, int num, String image) {
        this.name = name;
        this.num = num;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
