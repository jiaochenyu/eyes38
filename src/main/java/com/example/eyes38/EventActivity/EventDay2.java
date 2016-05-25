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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.event_day2,null);
        return view;
    }
}
