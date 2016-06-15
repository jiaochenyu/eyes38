package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by jqchen on 2016/5/20.
 */
public class Comments implements Serializable{
    /**
     * comment_id 后台编号
     * item_id 商品编号
     * path 头像地址
     * author_name 评论作者名称
     * rating 评论星级
     * comment 内容
     * create_date 评论时间
     * store_id 商品的存储id
     * author_id 评论人id
     */
    private int comment_id;
    private int item_id;
    private String path;
    private String author_name;
    private int rating;
    private String comment;
    private String create_date;
    private int store_id;
    private int author_id;

    public Comments(int comment_id, int item_id, String path, String author_name, int rating, String comment, String create_date, int store_id,int author_id) {
        this.comment_id = comment_id;
        this.item_id = item_id;
        this.path = path;
        this.author_name = author_name;
        this.rating = rating;
        this.comment = comment;
        this.create_date = create_date;
        this.store_id = store_id;
        this.author_id = author_id;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }
}
