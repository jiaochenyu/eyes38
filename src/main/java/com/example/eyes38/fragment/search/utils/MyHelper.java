package com.example.eyes38.fragment.search.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by weixiao on 2016/5/27.
 */
public class MyHelper extends SQLiteOpenHelper {
    private static final String NAME = "history_list.db";
    private static final int VERSION = 1;
    public static final String CREATE_TABLE = "create table " + HistoryTable.Field.TABLE_NAME +
            "(" + HistoryTable.Field._ID +
            " integer primary key autoincrement,"  + HistoryTable.Field.HISTORY_NAME +
            " text)";

    public MyHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
                db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
