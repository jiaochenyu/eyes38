package com.example.eyes38.utils;

/**
 * Created by jqchen on 2016/6/2.
 * 此类是为了截取图文详解的网址
 */
public class Substring {
    public static String getString(String string){
        if (!string.equals("")){
            int start = string.indexOf("http");
            int end = string.indexOf("jpg")+3;
            if (end > start && start >= 0 && end > 0){
                return string.substring(start, end);
            }
        }
        return "";
    }
    //返回加密后的电话号码
    public static String getPhoneString(String string){
        String start = string.substring(0,3);
        String end = string.substring(7,11);
        return start+"****"+end;
    }
}
