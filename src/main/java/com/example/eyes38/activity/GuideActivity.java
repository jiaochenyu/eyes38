package com.example.eyes38.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.adapter.ViewPageAdapter;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {
    private ViewPager guideViewPager;
    private List<View> mList;
    private PageIndicator indicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        //隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        initData();
        initAdapter();
    }

    private void initAdapter() {
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(mList, this);
        guideViewPager.setAdapter(viewPageAdapter);
        indicator.setViewPager(guideViewPager);
    }

    private void initData() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mList = new ArrayList<>();
        mList.add(inflater.inflate(R.layout.guide_one,null));
        mList.add(inflater.inflate(R.layout.guide_two,null));
        mList.add(inflater.inflate(R.layout.guide_three,null));
    }

    private void initView() {
        guideViewPager = (ViewPager) findViewById(R.id.guide_viewpage);
        indicator = (PageIndicator) findViewById(R.id.indicator);
    }

    public void start(View view) {
        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
