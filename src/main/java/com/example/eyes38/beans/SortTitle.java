package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by jqchen on 2016/5/16.
 * 此类用来描述分类一级标题
 */
public class SortTitle implements Serializable {
    /**
     * id
     * category_id 分类的id
     * content 标题内容
     * isseltected 是否选中
     * path 图片地址
      */
    private int id;
    private int category_id;
    private String content;
    private boolean isSeltcted;
    private String path;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public SortTitle(int id, int category_id, String content, boolean isSeltcted, String path) {
        this.id = id;
        this.category_id = category_id;
        this.content = content;
        this.isSeltcted = isSeltcted;

        this.path = path;
    }

    public boolean isSeltcted() {
        return isSeltcted;
    }

    public void setSeltcted(boolean seltcted) {
        isSeltcted = seltcted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
