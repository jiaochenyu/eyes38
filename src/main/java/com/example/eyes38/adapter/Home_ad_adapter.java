package com.example.eyes38.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * Created by huangjiechun on 16/5/19.
 */
public class Home_ad_adapter  extends PagerAdapter {
    List<View> mViewList;

    public Home_ad_adapter(List<View> viewList) {
        mViewList = viewList;
    }

    @Override
    public int getCount() {
        //设定为最大值,确保能一直轮播
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //viewGroup是装在图片布局的容器
        position %= mViewList.size();
        if (position<0){
            position += mViewList.size();
        }
        View view = mViewList.get(position);
        ViewParent parent = view.getParent();
        if (parent!=null){
            ViewGroup vg = (ViewGroup) parent;
            vg.removeView(view);
        }
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
