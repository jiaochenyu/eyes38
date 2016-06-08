package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by huangjiechun on 16/5/26.
 */
public class HomeContentContent implements Serializable {
    private int product_id;
    String image;
    String name;
    Double price;
    String extension4;

    @Override
    public String toString() {
        return "HomeContentContent{" +
                "image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", extension4='" + extension4 + '\'' +
                '}';
    }

    public HomeContentContent(String image, String name, Double price, String extension4) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.extension4 = extension4;
    }

    public HomeContentContent() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getExtension4() {
        return extension4;
    }

    public void setExtension4(String extension4) {
        this.extension4 = extension4;
    }
}
