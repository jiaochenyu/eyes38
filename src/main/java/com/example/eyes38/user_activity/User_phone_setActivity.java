package com.example.eyes38.user_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.eyes38.Application.Application;
import com.example.eyes38.R;

public class User_phone_setActivity extends AppCompatActivity {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    LinearLayout user_go_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_phone_set);
        initViews();
        initListener();
    }
    //跳转到修改密码页面
    private void initListener() {
        user_go_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(User_phone_setActivity.this,Update_user_passwordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
       user_go_update= (LinearLayout) findViewById(R.id.user_go_update);
        sp=this.getSharedPreferences("userInfo",MODE_PRIVATE);
    }

    //返回键
    public void user_set_back(View view) {
        onBackPressed();
    }
    //前往手机绑定
    public void user_bindphone_set(View view) {
        Intent intent=new Intent(User_phone_setActivity.this,User_BindPhoneActivity.class);
        startActivity(intent);
    }
    //退出登录
    public void user_back_login(View view) {
        editor=sp.edit();
        editor.putBoolean("STATE",false);
        Application.isLogin = false;
        editor.commit();
        Intent intent=new Intent(User_phone_setActivity.this,User_loginActivity.class);
        startActivity(intent);
    }
}

