package com.example.eyes38.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.eyes38.R;
import com.example.eyes38.fragment.home.HomeRecycleView;

import in.srain.cube.views.ptr.PtrFrameLayout;

public class HomezhuantiActivity extends AppCompatActivity {
    HomeRecycleView mHomeRecycleView;
     RecyclerView mRecyclerView;
    private String name;
    private PtrFrameLayout ptrFrame;
    Context mContext;
    private LayoutInflater inflater;
//    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homezhuanti);
        mRecyclerView = (RecyclerView) findViewById(R.id.home_zhuanti_detail_content);
        Intent intent = getIntent();
        name = intent.getStringExtra("values");
        mHomeRecycleView =new HomeRecycleView(this,mRecyclerView,name);
        mHomeRecycleView.startItem();
        //listener();
    }

    /*private void listener() {
        LoadMoreFooterView header = new LoadMoreFooterView(this);
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);
        //刷新
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrame.refreshComplete();
                        mHomeRecycleView =new HomeRecycleView(mContext,mRecyclerView,name);
                        mHomeRecycleView.startItem();

                    }
                },1800);

            }
        });
    }*/
    public void zhuanti_back(View view) {
        finish();
    }
}
