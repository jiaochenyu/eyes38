package com.example.eyes38.utils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by jqchen on 2016/6/6.
 * 此服务是动态监听网络的变化
 */
public class NetworkStateService extends Service {
    //网络状态
    public static boolean NetState;

    //广播实例
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 当当前接受到的广播的标识为网络状态的标识时做相应判断
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                // 获取网络连接管理器
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                //获取当前网络的状态
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    //当NetworkInfo不为空且是可用的情况下，获取当前网络的Type状态
                    NetState = true;
//                    Toast.makeText(context, "太好了，已经连接网络了！", Toast.LENGTH_LONG).show();
                } else {
                    NetState = false;
                    //提示用户无网络连接
                    Toast.makeText(context, "亲，网络不好哦！", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //注册网络状态的广播，绑定到mReceiver
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
