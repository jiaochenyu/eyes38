package com.example.eyes38.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.eyes38.R;
import com.example.eyes38.adapter.Sort_SortAdapter;
import com.example.eyes38.beans.Goods;

import java.util.ArrayList;
import java.util.List;

public class SortMenuActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Sort_SortAdapter sort_sortAdapter;
    List<Goods> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_menu);
        initView();
        initData();
        initAdapter();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.sort_sort_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sort_sort_swiprefresh);
        //显示两列
        GridLayoutManager grid = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(grid);
    }

    private void initData() {
        mList = new ArrayList<>();
        Goods g1 = new Goods(1,"苹果",null,"水果","100g","10/100g",null,null,11f,10f,0,0);
        Goods g2 = new Goods(2,"苹果",null,"水果","100g","10/100g",null,null,11f,10f,0,0);
        Goods g3 = new Goods(3,"苹果",null,"水果","100g","10/100g",null,null,11f,10f,0,0);
        Goods g4 = new Goods(4,"苹果",null,"水果","100g","10/100g",null,null,11f,10f,0,0);
        mList.add(g1);
        mList.add(g2);
        mList.add(g3);
        mList.add(g4);

    }

    private void initAdapter() {
        sort_sortAdapter = new Sort_SortAdapter(this,mList);
        mRecyclerView.setAdapter(sort_sortAdapter);
        sort_sortAdapter.setmOnItemClickListener(new Sort_SortAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Goods goods) {
                Log.e("jqchen",goods.getGoods_id()+"");
            }
        });
    }
}
