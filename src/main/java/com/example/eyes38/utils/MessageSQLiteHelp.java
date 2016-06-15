package com.example.eyes38.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JCY on 2016/6/14.
 */
public class MessageSQLiteHelp extends SQLiteOpenHelper {
    static String messageName = "message.db";
    static int dbVersion = 1;


    public MessageSQLiteHelp(Context context) {
      super(context,messageName,null,dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        String sql ="create table " + "message " +"("
                + "messageID "+"INTEGER PRIMARY KEY autoincrement,"
                + "customerID " + "int,"
                + "imgPath " + "text,"
                + "title " + "text,"
                + "content "+"text,"
                + "createtime "+"text"
                +")";
        db.execSQL(sql);
    }

    /**
     * 升级数据库的时候使用，当发现两次数据库的VERSION值不一致，则认为数据库会升级
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
