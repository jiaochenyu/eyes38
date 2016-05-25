package com.example.eyes38.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
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
    public static final int REFRESH = 0;
    public static final int LOADING = 1;
    private RecyclerView mRecyclerView;
    private List<Comments> mList;
    private Comment_Adapter comment_adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH:
                    //下拉刷新
                    Refresh();
                    break;
                case LOADING:
                    //上拉加载
                    Loading();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();
        initData();
        initAdapter();
        initListener();
    }

    private void initListener() {
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessageDelayed(REFRESH,1000);
            }
        });
        //上拉加载
        /*mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (newState == mRecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1 == comment_adapter.getItemCount());{
                    swipeRefreshLayout.setRefreshing(true);
                    handler.sendEmptyMessageDelayed(LOADING,1000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });*/
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition+1 == comment_adapter.getItemCount() && dy > 0){
                    boolean isRefreshing=swipeRefreshLayout.isRefreshing();
                    if (isRefreshing){
                        comment_adapter.notifyItemRemoved(comment_adapter.getItemCount());
                        return;
                    }
                    if (!isLoading){
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessageDelayed(LOADING,1000);
                                isLoading = false;
                            }
                        },3000);
                    }
                }
            }
        });
    }

    private void Loading() {
        //加更多数据，现在是写死的
        swipeRefreshLayout.setRefreshing(false);
        List<Comments> list = new ArrayList<Comments>();
        Comments c1 = new Comments(LOADING, "loading", "new");
        list.add(c1);
        list.add(c1);
        list.add(c1);
        comment_adapter.addMoreItem(list);
    }

    private void Refresh() {
        swipeRefreshLayout.setRefreshing(false);
        List<Comments> list = new ArrayList<Comments>();
        Comments c1 = new Comments(REFRESH, "refresh", "new");
        list.add(c1);
        comment_adapter.addItem(list);

    }

    private void initAdapter() {
        comment_adapter = new Comment_Adapter(mList);
        mRecyclerView.setAdapter(comment_adapter);
    }

    private void initData() {
        mList = new ArrayList<>();
        /*for (int i = 0; i < 20; i++) {
            Comments c1 = new Comments(i, "aaaaa", i+"");
            mList.add(c1);
        }*/
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
