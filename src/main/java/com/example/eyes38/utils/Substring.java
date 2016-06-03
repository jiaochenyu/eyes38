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
            if (end > start){
                return string.substring(start, end);
            }
        }
        return "";
    }
}
