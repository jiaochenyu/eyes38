package com.example.eyes38.user_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.eyes38.R;
import com.example.eyes38.fragment.UserFragment;

public class Update_user_passwordActivity extends AppCompatActivity {
    ImageView password_set_back, user_go_home;
    Button user_password_bao;//保存

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_password);
        initViews();
        toUserPassword();
        toUserHome();
        toUserSave();
    }

    //保存并返回
    private void toUserSave() {
        onBackPressed();
    }

    //返回user主界面
    private void toUserHome() {
        Intent intent = new Intent(Update_user_passwordActivity.this, UserFragment.class);
        startActivity(intent);
    }

    //返回键
    private void toUserPassword() {
        password_set_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        password_set_back = (ImageView) findViewById(R.id.password_set_back);
        user_go_home = (ImageView) findViewById(R.id.user_go_home);
        user_password_bao = (Button) findViewById(R.id.user_password_bao);
    }
}
