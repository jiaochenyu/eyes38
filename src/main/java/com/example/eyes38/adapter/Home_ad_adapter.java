package com.example.eyes38.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.eyes38.activity.SortMenuActivity;
import com.example.eyes38.beans.SortContentContent;

import java.util.List;

/**
 * Created by huangjiechun on 16/5/19.
 */
public class Home_ad_adapter  extends PagerAdapter {
    List<View> mViewList;
    Context mContext;

    public Home_ad_adapter(List<View> viewList,Context context) {
        mViewList = viewList;
        mContext=context;
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

        final int flag=position;

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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (flag%2==0){
                   Intent intent1 = new Intent(mContext, SortMenuActivity.class);
                   Bundle bundle = new Bundle();
                   SortContentContent scc = new SortContentContent(1, "水果", null);
                   bundle.putSerializable("values", scc);
                   intent1.putExtra("values", bundle);
                   mContext.startActivity(intent1);
               }else {
                   Intent intent1 = new Intent(mContext, SortMenuActivity.class);
                   Bundle bundle = new Bundle();
                   SortContentContent scc = new SortContentContent(1, "蔬菜", null);
                   bundle.putSerializable("values", scc);
                   intent1.putExtra("values", bundle);
                   mContext.startActivity(intent1);
               }
            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
