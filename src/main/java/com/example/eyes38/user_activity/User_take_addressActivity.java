package com.example.eyes38.user_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.eyes38.R;
import com.example.eyes38.user_activity.AddressInfo.User_addAddressActivity;

public class User_take_addressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_take_address);
    }
    //尼玛还是返回键
    public void user_take_address(View view) {
        onBackPressed();
    }
    //go go去新建收货地址
    public void user_toaddAddress(View view) {
        Intent intent=new Intent(User_take_addressActivity.this, User_addAddressActivity.class);
        startActivity(intent);
    }
}
