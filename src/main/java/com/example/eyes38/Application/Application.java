package com.example.eyes38.Application;

import com.yolanda.nohttp.NoHttp;

/**
 * Created by jqchen on 2016/5/21.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.init(this);
    }
}
