package com.example.eyes38.fragment.home;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.eyes38.R;
import com.example.eyes38.adapter.Home_item_adapter;
import com.example.eyes38.beans.HomeGrid;

import java.util.ArrayList;
import java.util.List;

/**此类写的是recycleView的实现
 * Created by huangjiechun on 16/5/23.
 */
public class HomeRecycleView {

    private RecyclerView mRecyclerView;
    private List<HomeGrid> mData;
    private Home_item_adapter mAdapter;
    private Context mContext;

    public HomeRecycleView(Context context,RecyclerView recycler) {
        this.mContext=context;
        this.mRecyclerView=recycler;
    }
    public void startItem() {
        initData();
        mAdapter = new Home_item_adapter(mData);
        mRecyclerView.setAdapter(mAdapter);
        //设置recycleview的布局管理
        //listview风格
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        //grid
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }


    private void initData() {
       mData = new ArrayList<HomeGrid>();
        HomeGrid hg1 = new HomeGrid(R.drawable.home_c1);
        HomeGrid hg2 = new HomeGrid(R.drawable.home_c2);
        HomeGrid hg3 = new HomeGrid(R.drawable.home_c3);
        HomeGrid hg4 = new HomeGrid(R.drawable.home_c4);
        mData.add(hg1);
        mData.add(hg2);
        mData.add(hg3);
        mData.add(hg4);
    }
}
