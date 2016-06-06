package com.example.eyes38.Application;

import android.content.SharedPreferences;

import com.yolanda.nohttp.NoHttp;

/**
 * Created by jqchen on 2016/5/21.
 */
public class Application extends android.app.Application {
    public static boolean isLogin;

    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.init(this);
        SharedPreferences mSharedPreferences = this.getSharedPreferences("userInfo", MODE_PRIVATE);
        isLogin = mSharedPreferences.getBoolean("STATE", false);
    }
}
