package com.example.eyes38.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.eyes38.R;

/**
 * Created by jqchen on 2016/6/6.
 * 此类是监听网络的变化
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    private static final String TAG = ConnectionChangeReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "网络状态改变");
        boolean success = false;
        //获取网络连接服务
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取是否正在使用WiFi网络
        NetworkInfo.State state = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (NetworkInfo.State.CONNECTED == state) {
            success = true;
        }
        //获取GPRS网络连接状态
        state = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (NetworkInfo.State.CONNECTED == state) {
            success = true;
        }
        if (!success) {
            Toast.makeText(context,context.getString(R.string.NetWorkChange),Toast.LENGTH_LONG).show();
            /*//新建通知
            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.logos)
                    .setContentTitle("38慧眼")
                    .setContentText("网络断开连接");
            //通知管理器
            int mNotificationId = 001;
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //发布通知
            manager.notify(mNotificationId,mBuilder.build());*/
        }
    }
}
