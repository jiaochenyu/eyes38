package com.example.eyes38.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.eyes38.beans.ConsultTable;

/**
 * Created by jqchen on 2016/6/15.
 */
public class ConsultHelper extends SQLiteOpenHelper {
    public static final String NAME = "consult.db";
    public static final int VERSION = 1;
    public static final String CREATE_TABLE = "create table " + ConsultTable.Field.TABLE_NAME +
            "(" + ConsultTable.Field.CONSULT_ID +
            " integer primary key autoincrement," + ConsultTable.Field.CUSOTMER_ID +
            " int," + ConsultTable.Field.CONSULT_CONTENT +
            " text," + ConsultTable.Field.CONSULT_REPLY +
            " text," + ConsultTable.Field.IMAGE +
            " text)";

    public ConsultHelper(Context context) {
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
