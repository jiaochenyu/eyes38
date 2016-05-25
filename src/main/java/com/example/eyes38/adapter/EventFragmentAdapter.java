package com.example.eyes38.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.eyes38.EventActivity.EventActivity;

import java.util.List;

/**
 * Created by weixiao on 2016/5/24.
 */
public class EventFragmentAdapter extends FragmentPagerAdapter {
    /**
     * 活动日期 Viewpager绑定fragment
     */
    List<Fragment> mFragmentList;


    public EventFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
        super(fragmentManager);
        mFragmentList=fragmentList;
    }


    @Override
    public Fragment getItem(int position) {
        //传递参数
        sendArgs(position);
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    //传递参数
    private void sendArgs(int positon){
        Bundle args=new Bundle();
        args.putString("args", EventActivity.tabTitle[positon]);
        mFragmentList.get(positon).setArguments(args);
    }
}
