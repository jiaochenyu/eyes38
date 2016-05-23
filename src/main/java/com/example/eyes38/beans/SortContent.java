package com.example.eyes38.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jqchen on 2016/5/17.
 */
public class SortContent implements Serializable {
    private int id;
    private String content;
    private List<SortContentContent> mList;

    public SortContent(int id, String content, List<SortContentContent> mList) {
        this.id = id;
        this.content = content;
        this.mList = mList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<SortContentContent> getmList() {
        return mList;
    }

    public void setmList(List<SortContentContent> mList) {
        this.mList = mList;
    }
}
