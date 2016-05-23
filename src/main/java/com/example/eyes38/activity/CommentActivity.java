package com.example.eyes38.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
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
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();
        initData();
        initAdapter();
        initRefresh();
        initLoading();
    }

    private void initLoading() {
        swipeRefreshLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //在这里进行监听，滑动布局滑到了底部，然后又进行加载数据
                        int scrollY = v.getScrollY();
                        int height = v.getHeight();
                        int scrollViewMeasureHeight = swipeRefreshLayout.getChildAt(0).getMeasuredHeight();
                        if ((scrollY + height) >= scrollViewMeasureHeight) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    List<Comments> list = new ArrayList<Comments>();
                                    Comments c1 = new Comments(5, "eeeee", "555");
                                    list.add(c1);
                                    comment_adapter.addMoreItem(list);
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }, 3000);
                        }

                }
                return false;
            }
        });
    }

    private void initRefresh() {
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<Comments> list = new ArrayList<Comments>();
                        Comments c1 = new Comments(5, "eeeee", "555");
                        list.add(c1);
                        comment_adapter.addItem(list);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    private void initAdapter() {
        comment_adapter = new Comment_Adapter(mList);
        mRecyclerView.setAdapter(comment_adapter);
    }

    private void initData() {
        mList = new ArrayList<>();
        Comments c1 = new Comments(1, "aaaaa", "111");
        Comments c2 = new Comments(2, "bbbbb", "222");
        Comments c3 = new Comments(3, "ccccc", "333");
        Comments c4 = new Comments(4, "ddddd", "444");
        mList.add(c1);
        mList.add(c2);
        mList.add(c3);
        mList.add(c4);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.goods_comment_recycler);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.comment_swiperefresh);
    }

    public void back(View view) {
        finish();
    }
}
