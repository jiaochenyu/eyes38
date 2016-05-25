package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by huangjiechun on 16/5/23.
 */
public class HomeGrid implements Serializable {
    private String pic;

    public HomeGrid() {
    }

    public HomeGrid(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
