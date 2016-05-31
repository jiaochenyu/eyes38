package com.example.eyes38.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.fragment.home.HomeRecycleView;

public class HomexptjActivity extends AppCompatActivity {
    //初始化recycleView
    RecyclerView mRecyclerView;
    //封装的recycleView实现类
    HomeRecycleView mHomeRecycleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homexptj);
        mRecyclerView = (RecyclerView) findViewById(R.id.home_xptj_view);
        //初始化recycleview并实现
        mHomeRecycleView = new HomeRecycleView(this,mRecyclerView);
        mHomeRecycleView.startItem();
    }

    public void xptj_back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
