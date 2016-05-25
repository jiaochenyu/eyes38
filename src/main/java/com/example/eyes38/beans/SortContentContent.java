package com.example.eyes38.beans;

import java.io.Serializable;


/**
 * Created by jqchen on 2016/5/17.
 * 此类是每一个详细布局的内容
 * 一张图片和文字
 */
public class SortContentContent implements Serializable{
    private int id;
    private String path;
    private String conten;

    public SortContentContent(int id, String conten, String path) {
        this.id = id;
        this.conten = conten;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getConten() {
        return conten;
    }

    public void setConten(String conten) {
        this.conten = conten;
    }
}
