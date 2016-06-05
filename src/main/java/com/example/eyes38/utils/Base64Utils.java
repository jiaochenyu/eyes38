package com.example.eyes38.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

/**
 * Created by JCY on 2016/6/2.
 */
public class Base64Utils {
    SharedPreferences mSharedPreferences; //偏好设置

    public Base64Utils(Context context) {
        this.mSharedPreferences = context.getSharedPreferences("",2); ///??????
    }
    private String authorization() {
        String username = "13091617887";  // 应该从偏好设置中获取账号密码
        String password = "123456";
        //Basic 账号+':'+密码  BASE64加密
        String addHeader = username + ":" + password;
        String authorization = "Basic " + new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));
        return authorization;
    }
}
