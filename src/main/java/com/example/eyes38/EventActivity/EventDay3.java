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
import com.example.eyes38.utils.DividerItemDecoration;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

/**
 * Created by weixiao on 2016/5/24.
 */
public class EventDay3 extends Fragment{
    EventActivity mEventActivity;
    ImageView mImageView;
    View view;
    PullToRefreshRecyclerView mPtrrv;
    SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新
    //适配器

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.event_day3,null);
        return view;
    }


}
