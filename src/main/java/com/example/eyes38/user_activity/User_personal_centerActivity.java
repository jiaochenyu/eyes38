package com.example.eyes38.user_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.eyes38.R;

public class User_personal_centerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_personal_center);
    }
    //返回键
    public void user_personal_center_back(View view) {
        onBackPressed();
    }
    //保存
    public void user_person_center_save(View view) {
        onBackPressed();
    }
}
