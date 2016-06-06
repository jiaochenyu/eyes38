package com.example.eyes38.Application;

import android.content.SharedPreferences;

import com.yolanda.nohttp.NoHttp;

/**
 * Created by jqchen on 2016/5/21.
 */
public class Application extends android.app.Application {
<<<<<<< HEAD
    public static boolean isLogin;
=======
    public SharedPreferences mSharedPreferences;
    public static boolean isLogin = false;
>>>>>>> 19a785258421f7a7212f318bc1c0d7b65484a20c

    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.init(this);
<<<<<<< HEAD
        SharedPreferences mSharedPreferences = this.getSharedPreferences("userInfo", MODE_PRIVATE);
        isLogin = mSharedPreferences.getBoolean("STATE", false);
=======
        mSharedPreferences = this.getSharedPreferences("userInfo",MODE_PRIVATE);
        boolean states = mSharedPreferences.getBoolean("STATE",false);
        isLogin = states;
>>>>>>> 19a785258421f7a7212f318bc1c0d7b65484a20c
    }
}
