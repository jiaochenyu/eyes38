package com.example.eyes38.user_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.eyes38.R;

public class User_message_setActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message_set);
    }
    //返回键
    public void user_message_back(View view) {
        onBackPressed();
    }
}
