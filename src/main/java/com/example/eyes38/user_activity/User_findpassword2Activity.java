package com.example.eyes38.user_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.eyes38.R;

public class User_findpassword2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_findpassword2);
    }

    public void find_password_backTwo(View view) {
        onBackPressed();
    }


    public void user_find_next2(View view) {
        Intent intent=new Intent(User_findpassword2Activity.this,User_find_password3Activity.class);
        startActivity(intent);
    }
}
