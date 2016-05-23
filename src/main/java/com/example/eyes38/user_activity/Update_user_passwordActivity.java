package com.example.eyes38.user_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.fragment.HomeFragment;
import com.example.eyes38.fragment.UserFragment;

public class Update_user_passwordActivity extends AppCompatActivity {
    Button user_password_save;
    ImageView user_go_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_password);
        initViews();//初始化控件
        initListener();
    }

    private void initListener() {
        //保存
        user_password_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //返回主页面
        user_go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Update_user_passwordActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        user_password_save= (Button) findViewById(R.id.user_password_save);
        user_go_home= (ImageView) findViewById(R.id.user_go_home);
    }

    //修改密码返回键
    public void update_password_back(View view) {
        onBackPressed();
    }

}
