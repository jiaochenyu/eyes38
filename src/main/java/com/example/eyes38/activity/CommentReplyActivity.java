package com.example.eyes38.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.eyes38.R;
import com.example.eyes38.adapter.CommentReplyAdapter;
import com.example.eyes38.beans.CommentReply;
import com.example.eyes38.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CommentReplyActivity extends AppCompatActivity {
//    评论回复界面
private List<CommentReply> mList;
    private CommentReplyAdapter commentReplyAdapter;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_reply);
        initView();
        getData();
        setAdapter();
    }

    private void setAdapter() {
        //如果没有评论，显示评论为空的界面
        if (mList.size() == 0){
            setContentView(R.layout.comment_reply_none);
        }else {
            commentReplyAdapter = new CommentReplyAdapter(mList);
            mRecyclerView.setAdapter(commentReplyAdapter);
        }

    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.comment_reply_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void getData() {
        mList = new ArrayList<>();
        Intent intent = getIntent();
        mList = (List<CommentReply>) intent.getSerializableExtra("values");
    }

    public void back(View view) {
        //返回上一层
        finish();
    }


}
