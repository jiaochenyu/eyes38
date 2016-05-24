package com.example.eyes38.fragment.home;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.adapter.Home_ad_adapter;

import java.util.ArrayList;

/**
 * Created by huangjiechun on 16/5/23.
 */
public class HomeLunboView {

    MainActivity mMainActivity;
    View view;
    public static final int IMAGE_UPDATE = 1;
    public static final int REFRESHTIME = 5 * 1000;
    public static final int IMAGE_CHANGED = 2;
    ViewPager mViewPager;
    ArrayList<View> mViewList;
    int mCurrentItem = Integer.MAX_VALUE / 2;

    public HomeLunboView(MainActivity activity, ViewPager viewPager) {
        this.mMainActivity=activity;
        this.mViewPager=viewPager;
    }
    public  void startLubo(){
        view =  View.inflate(mMainActivity,R.layout.home,null);
        initData();
        setLinstener();
        Home_ad_adapter myAdapter = new Home_ad_adapter(mViewList);
        mViewPager.setAdapter(myAdapter);
        mViewPager.setCurrentItem(mCurrentItem);
        mHandler.sendEmptyMessageDelayed(IMAGE_UPDATE,REFRESHTIME);
    }
    private void setLinstener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                //手动滑到这个广告的时候,发送改位置的值
                mHandler.sendMessage(Message.obtain(mHandler,IMAGE_CHANGED,position,0));
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        mViewList = new ArrayList<View>();
        int[] imageId = {R.drawable.banner01, R.drawable.banner1,R.drawable.banner01,R.drawable.banner1};
        for (int i = 0; i < imageId.length; i++) {
            View view = View.inflate(mMainActivity,R.layout.home_ad_item, null);
            ImageView mItemIvContent = (ImageView) view.findViewById(R.id.item_iv_content);
            mItemIvContent.setImageResource(imageId[i]);
            mViewList.add(view);
        }
    }




    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int action = msg.what;
            if (mHandler.hasMessages(IMAGE_UPDATE)){
                mHandler.removeMessages(IMAGE_UPDATE);
            }
            switch (action){
                case IMAGE_UPDATE:
                    //轮播图经行更新
                    mCurrentItem +=1;
                    mViewPager.setCurrentItem(mCurrentItem);
                    mHandler.sendEmptyMessageDelayed(IMAGE_UPDATE, REFRESHTIME);
                    break;
                case IMAGE_CHANGED:
                    //手动滑了广告
                    mCurrentItem = msg.arg1;
                    mViewPager.setCurrentItem(mCurrentItem);
                    mHandler.sendEmptyMessageDelayed(IMAGE_UPDATE,REFRESHTIME);
                    break;
            }
        }
    };
}
