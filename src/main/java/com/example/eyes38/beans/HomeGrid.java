package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by huangjiechun on 16/5/23.
 */
public class HomeGrid implements Serializable {
    private int pic;

    public HomeGrid(int pic) {
        this.pic = pic;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}
