package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by jqchen on 2016/5/16.
 * 此类用来描述分类一级标题
 */
public class SortTitle implements Serializable {
    /**
     * id
     * content 标题内容
     * isseltected 是否选中
      */
    private int id;
    private String content;
    private boolean isSeltcted;

    public SortTitle(int id, String content, boolean isSeltcted) {
        this.id = id;
        this.content = content;
        this.isSeltcted = isSeltcted;
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
}
