package com.example.eyes38.EventActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.eyes38.R;
import com.example.eyes38.beans.EventContentGood;
import com.yolanda.nohttp.RequestQueue;

import java.util.List;

/**
 * Created by weixiao on 2016/5/24.
 */
public class EventDay2 extends Fragment{
    EventActivity mEventActivity;
    ImageView mImageView;
    View view;
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新
    //适配器
    List<EventContentGood> mList;
    //采用NoHttp
    private RequestQueue mRequestQueue;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.event_day2,null);
        initViews();
        return view;
    }

    private void initViews() {
        mRecyclerView= (RecyclerView) view.findViewById(R.id.event_day2_recycle);
        //设置recycleview布局


    }
}
