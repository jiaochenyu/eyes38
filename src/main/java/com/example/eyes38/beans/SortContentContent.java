package com.example.eyes38.beans;

import android.net.Uri;

import java.io.Serializable;


/**
 * Created by jqchen on 2016/5/17.
 * 此类是每一个详细布局的内容
 * 一张图片和文字
 */
public class SortContentContent implements Serializable{
    private int id;
    private Uri uri;
    private String conten;

    public SortContentContent(int id, Uri uri, String conten) {
        this.id = id;
        this.uri = uri;
        this.conten = conten;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getConten() {
        return conten;
    }

    public void setConten(String conten) {
        this.conten = conten;
    }
}
