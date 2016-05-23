package com.example.eyes38.user_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.eyes38.R;

public class User_findpasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_findpassword);
    }
    //下一步
    public void user_find_next(View view) {
        Intent intent=new Intent(User_findpasswordActivity.this,User_findpassword2Activity.class);
        startActivity(intent);
    }
    //返回键
    public void find_password_back(View view) {
        onBackPressed();
    }
}
