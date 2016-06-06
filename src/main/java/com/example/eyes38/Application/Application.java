package com.example.eyes38.Application;

import android.content.SharedPreferences;

import com.yolanda.nohttp.NoHttp;

/**
 * Created by jqchen on 2016/5/21.
 */
public class Application extends android.app.Application {
    public SharedPreferences mSharedPreferences;
    public static boolean isLogin = false;

    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.init(this);
        mSharedPreferences = this.getSharedPreferences("userInfo",MODE_PRIVATE);
        boolean states = mSharedPreferences.getBoolean("STATE",false);
        isLogin = states;
    }
}
