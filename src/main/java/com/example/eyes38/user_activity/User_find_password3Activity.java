package com.example.eyes38.user_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.eyes38.R;

public class User_find_password3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_find_password3);
    }

    public void update_password_back3(View view) {
        onBackPressed();
    }

    public void user_findpassword_reback(View view) {
        Intent intent=new Intent(User_find_password3Activity.this,User_loginActivity.class);
       startActivity(intent);
    }
}
