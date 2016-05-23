package com.example.eyes38.user_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.eyes38.R;

public class User_registerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
    }

    public void user_register_back(View view) {
        onBackPressed();
    }

    public void user_register_login(View view) {
        Intent intent=new Intent(User_registerActivity.this,User_loginActivity.class);
        startActivity(intent);
    }
    //进入详情页面
    public void user_register_next(View view) {
    }
}
