package com.example.eyes38.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.fragment.home.HomeLunboView;
import com.example.eyes38.fragment.home.HomeRecycleView;


/**
 * Created by jcy on 2016/5/8.
 */
public class HomeFragment extends Fragment {
    MainActivity mMainActivity;
    View view;
    //初始化轮播图的viewpager
    ViewPager mViewPager;
    //初始化recycleView
    RecyclerView mRecyclerView;
    //封装的recycleView实现类
    HomeRecycleView mHomeRecycleView;
    //封装的轮播图的实现类
    HomeLunboView mHomeLunboView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, null);
        mMainActivity = (MainActivity) getActivity();
        initView();
        mHomeLunboView = new HomeLunboView(mMainActivity,mViewPager);
        mHomeLunboView.startLubo();
        mHomeRecycleView = new HomeRecycleView(mMainActivity,mRecyclerView);
        mHomeRecycleView.startItem();
        return view;
    }

    private void initView() {
        mViewPager = (ViewPager) view.findViewById(R.id.main_ad_show);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
