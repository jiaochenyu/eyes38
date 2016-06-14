package com.example.eyes38.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;

/**
 * 此类是每次进入应用的加载页
 * create by jqchen
 * update by 2016.6.6
 */

public class LoadActivity extends AppCompatActivity {

    public static final int END = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        //隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler.sendEmptyMessageDelayed(END,3000);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(LoadActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

}
