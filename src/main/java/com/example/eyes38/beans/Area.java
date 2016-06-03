package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by jqchen on 2016/6/3.
 */
public class Area implements Serializable{
    private int district_id;
    private int parent_id;
    private String name;

    public Area() {
    }

    public Area(int district_id, int parent_id, String name) {
        this.district_id = district_id;
        this.parent_id = parent_id;
        this.name = name;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
