package com.example.eyes38.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eyes38.EventActivity.EventActivity;
import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.activity.home.HomexptjActivity;
import com.example.eyes38.fragment.home.HomeLunboView;
import com.example.eyes38.fragment.home.HomeRecycleView;
import com.example.eyes38.fragment.home.HomeSpinnerView;
import com.yolanda.nohttp.RequestQueue;


/**
 * Created by jcy on 2016/5/8.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
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
    //初始化spinner
    Spinner mSpinner;
    //封装的spinner的实现类
    HomeSpinnerView mHomeSpinnerView;

    ImageView home_xptjgengduo,home_yzcpgengduo;

    ImageView mImageView;
    int height;

    private RequestQueue mRequestQueue;
<<<<<<< HEAD
=======


>>>>>>> 60a4f3967656157dfb4f23b349000cbcab04a36c

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, null);
        mMainActivity = (MainActivity) getActivity();
        initView();


        //初始化轮播图并实现
        /*mHomeLunboView = new HomeLunboView(mMainActivity, mViewPager);
        mHomeLunboView.startLubo();*/


        //初始化recycleview并实现
        mHomeRecycleView = new HomeRecycleView(mMainActivity, mRecyclerView);
        mHomeRecycleView.startItem();
        //计算屏幕的尺寸
        caculate();
        //初始化spinner并实现
        mHomeSpinnerView = new HomeSpinnerView(mMainActivity, mSpinner, height);
        mHomeSpinnerView.startspinner();
        setonClick();
        return view;
    }

    private void setonClick() {
        home_xptjgengduo.setOnClickListener(this);
        home_yzcpgengduo.setOnClickListener(this);
    }

    private void initView() {
        mViewPager = (ViewPager) view.findViewById(R.id.main_ad_show);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        mSpinner = (Spinner) view.findViewById(R.id.home_spinner);
        mImageView = (ImageView) view.findViewById(R.id.home_jisuan);
        home_xptjgengduo = (ImageView) view.findViewById(R.id.home_xptjgengduo);
        home_yzcpgengduo= (ImageView) view.findViewById(R.id.home_yzcpgengduo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void caculate() {
        WindowManager manager = (WindowManager) mMainActivity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        int width2 = dm.widthPixels;
        height = dm.heightPixels;
        Toast.makeText(mMainActivity, "height:" + height, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int buttonid = v.getId();
        switch (buttonid) {
            case R.id.home_xptjgengduo:
                Intent intent = new Intent(mMainActivity, HomexptjActivity.class);
                startActivity(intent);
                break;
            case R.id.home_yzcpgengduo:
                Intent intent1 = new Intent(mMainActivity, EventActivity.class);
                startActivity(intent1);
                break;

        }
    }
}