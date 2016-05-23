package com.example.eyes38.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.eyes38.R;
import com.example.eyes38.adapter.Comment_Adapter;
import com.example.eyes38.beans.Comments;
import com.example.eyes38.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    List<Comments> mList;
    Comment_Adapter comment_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();
        initData();
        initAdapter();

    }

    private void initListener() {

    }

    private void initAdapter() {
        comment_adapter = new Comment_Adapter(mList);
        mRecyclerView.setAdapter(comment_adapter);
    }

    private void initData() {
        mList = new ArrayList<>();
        Comments c1 = new Comments(1,"aaaaa","111");
        Comments c2 = new Comments(2,"bbbbb","222");
        Comments c3 = new Comments(3,"ccccc","333");
        Comments c4 = new Comments(4,"ddddd","444");
        mList.add(c1);
        mList.add(c2);
        mList.add(c3);
        mList.add(c4);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.goods_comment_recycler);
        LinearLayoutManager linear = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linear);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    }

    public void back(View view) {
        finish();
    }
}
