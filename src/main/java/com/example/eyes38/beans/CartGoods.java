package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by jqchen on 2016/5/19.
 */
public class CartGoods implements Serializable {

    private int shopping_cart_id;
    private int customer_id;
    private String customer_name;
    private int product_id;
    private String product_name;
    private String store_name;
    private int quantity = 0;  // 数量
    private double price; //商品价格
    private String extension1=null; // 是否是一周菜谱
    private boolean isSelected; // 是否被选中
    private int viewType;
    //商品信息
    private Goods mGoods;


    /**
     * group : NORMAL
     * data : [{"shopping_cart_id":"258","customer_id":"19","customer_name":"13091617887","product_id":"4","product_name":"shz＋全国-蜜瓜","store_id":"0","store_name":"平台店铺","quantity":"2","price":"0.0100","distribution_key":null,"main_promotion_id":null,"main_promotion_name":null,"other_promotion_ids":null,"product_option_ids":"","product_option_values":"","product_option_value_ids":"","product_option_value_names":"","extension1":"false","create_date":"2016-06-02 19:16:08","update_date":"2016-06-02 19:16:10","create_by":"19","update_by":"19","version_num":null,"type":null,"points":null,"checked":null,"product":{"product_id":"4","model":null,"sku":"1","location":null,"belongs_to_store":"1","stock_num":"194","price":"0.010","stock_status_id":null,"image":"http://hz-ifs.ilexnet.com/eyes38/599334_1_pic500_120.jpg","brand_id":null,"shipping":"0","points":null,"tax_class_id":null,"date_available":null,"weight":null,"weight_class_id":null,"length":null,"width":null,"height":null,"length_class_id":null,"subtract_stock":null,"minimum":"0","sort_order":"0","status":"onSell","tag":null,"featured":null,"page_view":"0","cross_border":null,"shipping_type":null,"send_country":null,"unit":null,"rough_weight":null,"create_date":"2016-04-25 17:06:23","update_date":"2016-06-02 19:28:47","create_by":"3","update_by":"1","version_num":null,"market_price":"0.020","extension1":"0.02","extension2":"y","extension3":"all","extension4":"个","extension5":"84","extension6":null,"flag":null,"type":"NORMAL","option_status":"DISABLED","name":"shz＋全国-蜜瓜","subtitle":null,"description":"<p><img src=\"http://pic.womai.com/upload/2016/4/16/20160416084759193.jpg\" /><\/p>\n","meta_title":"","meta_description":"","meta_keyword":"","brand":false,"product_option_combines":[],"store":false,"product_search":{"product_id":"4","comment_num":"0","sales":"6","stock_num":"194"}}}]
     */

    public CartGoods() {
    }

    public CartGoods(int shopping_cart_id, int customer_id, String customer_name, int product_id, String product_name, String store_name, int quantity, double price, String extension1, Goods goods,int viewType) {
        this.shopping_cart_id = shopping_cart_id;
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.product_id = product_id;
        this.product_name = product_name;
        this.store_name = store_name;
        this.quantity = quantity;
        this.price = price;
        this.extension1 = extension1;
        mGoods = goods;
        this.viewType = viewType;
    }

    public Goods getGoods() {
        return mGoods;
    }

    public void setGoods(Goods goods) {
        mGoods = goods;
    }






    public int getShopping_cart_id() {
        return shopping_cart_id;
    }

    public void setShopping_cart_id(int shopping_cart_id) {
        this.shopping_cart_id = shopping_cart_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

   /* public boolean isExtension1() {
        return extension1;
    }

    public void setExtension1(boolean extension1) {
        this.extension1 = extension1;
    }*/

    public String getExtension1() {
        return extension1;
    }

    public void setExtension1(String extension1) {
        this.extension1 = extension1;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}

