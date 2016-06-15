package com.example.eyes38.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.eyes38.beans.Consult;
import com.example.eyes38.beans.ConsultTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jqchen on 2016/6/15.
 */
public class ConsultDataBase {
    private Context mContext;
    private static ConsultHelper helper;
    private SQLiteDatabase mDatabase;
    public ConsultDataBase(Context mContext) {
        this.mContext = mContext;
        helper = new ConsultHelper(mContext);
    }
    public boolean InsertDataBase(int customerid,String content,String reply,String image){
        mDatabase = helper.getWritableDatabase();
        /*String sql = "insert into " + ConsultTable.Field.TABLE_NAME+
                "(" +ConsultTable.Field.CUSOTMER_ID+
                "," +ConsultTable.Field.CONSULT_CONTENT+
                "," +ConsultTable.Field.CONSULT_REPLY+
                ")values(?,?,?)";*/
        ContentValues values = new ContentValues();
        values.put(ConsultTable.Field.CUSOTMER_ID,customerid);
        values.put(ConsultTable.Field.CONSULT_CONTENT,content);
        values.put(ConsultTable.Field.CONSULT_REPLY,reply);
        values.put(ConsultTable.Field.IMAGE,image);
        mDatabase.insert(ConsultTable.Field.TABLE_NAME,null,values);
        return true;
    }
    public List<Consult> selectAllConsult(int customerid){
        List<Consult> mConsults = new ArrayList<>();
        mDatabase = helper.getReadableDatabase();
        String sql = "select " +ConsultTable.Field.CONSULT_CONTENT+
                "," +ConsultTable.Field.CONSULT_REPLY+
                "," +ConsultTable.Field.IMAGE+
                " from " +ConsultTable.Field.TABLE_NAME+
                " where " +ConsultTable.Field.CUSOTMER_ID+
                " == " +customerid+
                " group by " +ConsultTable.Field.CONSULT_ID+
                "";
        Cursor cursor = mDatabase.rawQuery(sql,null);
        while (cursor.moveToNext()){
            String content = cursor.getString(cursor.getColumnIndex(ConsultTable.Field.CONSULT_CONTENT));
            String reply = cursor.getString(cursor.getColumnIndex(ConsultTable.Field.CONSULT_REPLY));
            String image = cursor.getString(cursor.getColumnIndex(ConsultTable.Field.IMAGE));
            Consult customer = new Consult(image,content);
            Consult service = new Consult(image,reply);
            mConsults.add(customer);
            mConsults.add(service);
        }
        return mConsults;
    }

}
