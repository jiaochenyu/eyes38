package com.example.eyes38.user_activity.AddressInfo;

/**
 * Created by weixiao on 2016/5/30.
 */
public class AreaBean {
    private int district_id;
    private int parent_id;
    private String name;

    public AreaBean() {
    }

    public AreaBean(int district_id, int parent_id, String name) {
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
