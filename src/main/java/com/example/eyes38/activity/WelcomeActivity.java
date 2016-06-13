package com.example.eyes38.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.eyes38.activity.home.LoadActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo",MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime",false);
        if (isFirstTime){
            Intent intent = new Intent(WelcomeActivity.this, LoadActivity.class);
            startActivity(intent);
            finish();
        }else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTime",true);
            editor.apply();
            Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
