package com.example.eyes38.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.fragment.home.HomeRecycleView;

import in.srain.cube.views.ptr.PtrFrameLayout;

public class HomezhuantiActivity extends AppCompatActivity {
    HomeRecycleView mHomeRecycleView;
     RecyclerView mRecyclerView;
    private String name;
    private PtrFrameLayout ptrFrame;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homezhuanti);
        mRecyclerView = (RecyclerView) findViewById(R.id.home_zhuanti_detail_content);
        Intent intent = getIntent();
        name = intent.getStringExtra("values");
        mTextView = (TextView) findViewById(R.id.home_zhuanti_detail_name);
        mTextView.setText(name);
        mHomeRecycleView =new HomeRecycleView(this,mRecyclerView,name);
        mHomeRecycleView.startItem();
    }

    public void zhuanti_back(View view) {
        finish();
    }
}
