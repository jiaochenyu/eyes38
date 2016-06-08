package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by jqchen on 2016/5/18.
 */
public class Goods implements Serializable {
    private int goods_id;//商品id
    private String goods_name;//商品名称
    private String path;//商品图片地址
    private String goods_unit;//商品规格
    private float goods_market_price;//商品市场价
    private float goods_platform_price;//商品平台价
    private int goods_comment_count;//评论数量
    private int goods_stock;//商品库存
    private String goods_description;//商品图文详情
    private String extension = "false" ; //是否是一周菜谱
    public Goods() {
    }

    public Goods(int goods_id, String goods_name, String path, String goods_unit, float goods_market_price, float goods_platform_price, int goods_comment_count, int goods_stock, String goods_description) {
        this.goods_id = goods_id;
        this.goods_name = goods_name;
        this.path = path;
        this.goods_unit = goods_unit;
        this.goods_market_price = goods_market_price;
        this.goods_platform_price = goods_platform_price;
        this.goods_comment_count = goods_comment_count;
        this.goods_stock = goods_stock;
        this.goods_description = goods_description;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGoods_unit() {
        return goods_unit;
    }

    public void setGoods_unit(String goods_unit) {
        this.goods_unit = goods_unit;
    }

    public float getGoods_market_price() {
        return goods_market_price;
    }

    public void setGoods_market_price(float goods_market_price) {
        this.goods_market_price = goods_market_price;
    }

    public float getGoods_platform_price() {
        return goods_platform_price;
    }

    public void setGoods_platform_price(float goods_platform_price) {
        this.goods_platform_price = goods_platform_price;
    }

    public int getGoods_comment_count() {
        return goods_comment_count;
    }

    public void setGoods_comment_count(int goods_comment_count) {
        this.goods_comment_count = goods_comment_count;
    }

    public int getGoods_stock() {
        return goods_stock;
    }

    public void setGoods_stock(int goods_stock) {
        this.goods_stock = goods_stock;
    }

    public String getGoods_description() {
        return goods_description;
    }

    public void setGoods_description(String goods_description) {
        this.goods_description = goods_description;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
