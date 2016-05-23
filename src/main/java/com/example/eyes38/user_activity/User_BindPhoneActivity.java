package com.example.eyes38.user_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;

public class User_BindPhoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__bind_phone);
    }
    //绑定手机返回键
    public void user_bindphone_back(View view) {
        onBackPressed();
    }
    //返回到主页面
    public void user_bindphone_home(View view) {
        Intent intent=new Intent(User_BindPhoneActivity.this, MainActivity.class);
        startActivity(intent);
    }
    //保存
    public void user_bindphone_save(View view) {
        onBackPressed();
    }
}
