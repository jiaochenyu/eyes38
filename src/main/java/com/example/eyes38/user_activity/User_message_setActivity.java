package com.example.eyes38.user_activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.eyes38.Application.Application;
import com.example.eyes38.R;
import com.example.eyes38.adapter.MessageAdapter;
import com.example.eyes38.beans.MessageBean;
import com.example.eyes38.utils.MessageSQLiteHelp;

import java.util.ArrayList;
import java.util.List;

public class User_message_setActivity extends AppCompatActivity {
    private LinearLayout mEmptyLinearLayout;
    private LinearLayout mNotEmptyLinearLayout;
    private RecyclerView mRecyclerView;
    private MessageSQLiteHelp myHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    List<MessageBean> mList;
    private MessageAdapter mMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message_set);
        initViews();
        initData();
        initStates();
        setAdapter();
        initListener();
    }

    private void initViews() {
        mEmptyLinearLayout = (LinearLayout) findViewById(R.id.message_empty);
        mNotEmptyLinearLayout = (LinearLayout) findViewById(R.id.message_notempty);
        mRecyclerView = (RecyclerView) findViewById(R.id.message_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void initData() {
        //先遍历数据库
        mList = new ArrayList<>();
        myHelper = new MessageSQLiteHelp(this);
        selectAllStudent();
    }

    //判断显示那个界面
    private void initStates() {
        if (Application.isLogin){
            if (mList.size() == 0) {
                mEmptyLinearLayout.setVisibility(View.VISIBLE);
                mNotEmptyLinearLayout.setVisibility(View.GONE);
            } else {
                mEmptyLinearLayout.setVisibility(View.GONE);
                mNotEmptyLinearLayout.setVisibility(View.VISIBLE);
            }
        }else {
            mEmptyLinearLayout.setVisibility(View.VISIBLE);
            mNotEmptyLinearLayout.setVisibility(View.GONE);
        }

    }

    private void setAdapter() {
        mMessageAdapter = new MessageAdapter(mList, this);
        mRecyclerView.setAdapter(mMessageAdapter);
    }

    private void initListener() {
        mMessageAdapter.setLongClickListener(new MessageAdapter.MyItemLongClickListener() {
            @Override
            public void OnItemLongClick(View view, int position) {
                deleteMethod(mList.get(position).getMessageID(),position);
            }
        });

    }


    //*****先遍历数据库
    public void selectAllStudent() {

        mDatabase = myHelper.getReadableDatabase();
        //String sql = "select messageID,customerID,imgPath,title,content,createtime from message";
        //Cursor cursor = db.query(DBOpneHelper.STUDENT_TABLE, null, null, null, null, null, null);
        mCursor = mDatabase.query("message", null, null, null, null, null, null);
        // mCursor = mDatabase.rawQuery(sql,null);
        while (mCursor.moveToNext()) {
            int messageID = mCursor.getInt(mCursor.getColumnIndex("messageID"));
            int customerID = mCursor.getInt(mCursor.getColumnIndex("customerID"));
            String imgPath = mCursor.getString(mCursor.getColumnIndex("imgPath"));
            String title = mCursor.getString(mCursor.getColumnIndex("title"));
            String content = mCursor.getString(mCursor.getColumnIndex("content"));
            String time = mCursor.getString(mCursor.getColumnIndex("createtime"));
            MessageBean message = new MessageBean(messageID, customerID, imgPath, title, content, time);
            mList.add(message);
        }
        ;
        mDatabase.close();
    }

    private void deleteMethod(int messageID,int position) {
        mDatabase = myHelper.getWritableDatabase();
        mDatabase.delete("message","messageID = ?",new String[]{String.valueOf(messageID)});
        mList.remove(position);
        initStates();
        mMessageAdapter.notifyDataSetChanged();
    }

    //返回键
    public void user_message_back(View view) {
        finish();
    }
}
