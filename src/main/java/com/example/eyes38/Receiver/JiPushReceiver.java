package com.example.eyes38.Receiver;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.example.eyes38.user_activity.User_message_setActivity;
import com.example.eyes38.utils.MessageSQLiteHelp;

import java.util.Calendar;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 JiPushReceiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JiPushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private MessageSQLiteHelp myHelper;
	private SQLiteDatabase mDatabase;
	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		//Log.d(TAG, "[JiPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[JiPushReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[JiPushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));



		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[JiPushReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_MESSAGE);
            Log.d(TAG, "[JiPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			//*****************插入数据到数据库
			initdata(context,bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE),bundle.getString(JPushInterface.EXTRA_ALERT));



		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[JiPushReceiver] 用户点击打开了通知");

        	//打开自定义的Activity
        	Intent i = new Intent(context, User_message_setActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	context.startActivity(i);
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[JiPushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[JiPushReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[JiPushReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	private void initdata(Context context,String title,String content) {
		Calendar c = Calendar.getInstance();
		String createtime = c.get(Calendar.YEAR)+"/"
				+(c.get(Calendar.MONTH)+1)+"/"
				+c.get(Calendar.DAY_OF_MONTH)+"  "
				+c.get(Calendar.HOUR_OF_DAY)+":"
				+c.get(Calendar.MINUTE);
		Log.e("createtime",createtime+"");
		myHelper = new MessageSQLiteHelp(context);
		/*mDatabase = myHelper.getWritableDatabase();
		String sql = "insert into message (customerID,imgPath,title,content,createtime) values(?,?,?,?,?)";
		mDatabase.execSQL(sql,new String[]{null,null,title,content,createtime});*/
		mDatabase = myHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("customerID",1);
		values.put("imgPath", "http://o8oqvjhsv.bkt.clouddn.com/message_logos.png");
		values.put("title","38慧眼");
		values.put("content",content);
		values.put("createtime",createtime);
		mDatabase.insert("message", null, values);
	}

}
