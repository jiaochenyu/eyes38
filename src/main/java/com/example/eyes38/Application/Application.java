package com.example.eyes38.Application;

import android.content.SharedPreferences;

import com.yolanda.nohttp.NoHttp;

import c.b.BP;

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
        BP.init(this,"640698dda5dab6cb17f864264b35ae91");

    }
}
