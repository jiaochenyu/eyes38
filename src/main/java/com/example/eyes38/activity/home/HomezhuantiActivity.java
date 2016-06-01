package com.example.eyes38.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.eyes38.R;
import com.example.eyes38.fragment.home.HomeRecycleView;

public class HomezhuantiActivity extends AppCompatActivity {
    HomeRecycleView mHomeRecycleView;
   // RecyclerView mRecyclerView;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homezhuanti);
      //  mRecyclerView = (RecyclerView) findViewById(R.id.home_more_recycleview);
        Intent intent = getIntent();
        name=intent.getStringExtra("values");
//        mHomeRecycleView =new HomeRecycleView(this,mRecyclerView,name);
//        mHomeRecycleView.startItem();
    }

    public void zhuanti_back(View view) {
        finish();
    }
}
