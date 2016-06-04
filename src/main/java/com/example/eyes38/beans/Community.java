package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by jqchen on 2016/6/3.
 */
public class Community implements Serializable {
    private int id;
    private String name;
    private int district_id;

    public Community() {
    }

    public Community(int id, String name, int district_id) {
        this.id = id;
        this.name = name;
        this.district_id = district_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }
}
