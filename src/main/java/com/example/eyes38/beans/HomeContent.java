package com.example.eyes38.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huangjiechun on 16/5/26.
 */
public class HomeContent implements Serializable{

    private String name;
    private List<HomeContentContent> mList;

    public HomeContent(String name, List<HomeContentContent> list) {

        this.name = name;
        mList = list;
    }

    public HomeContent() {
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HomeContentContent> getList() {
        return mList;
    }

    public void setList(List<HomeContentContent> list) {
        mList = list;
    }
}
