package com.example.eyes38.user_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.eyes38.R;
import com.example.eyes38.user_activity.AddressInfo.User_addAddressActivity;

public class User_take_addressActivity extends AppCompatActivity {
    private ListView add_address_list;
    private LinearLayout add_address_header,add_address_footer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        //判断当前该显示哪个页面
        //adjustViews();
        setContentView(R.layout.activity_user_take_address);
    }

 /*   private void adjustViews() {
        int count=add_address_list.getCount();//count表示listview当前数据的个数
        if (count==0){
            //表示当前没有地址，则显示空页面
            add_address_header.setVisibility(View.VISIBLE);
        }else {
            //表示当前显示的有地址，则显示listview
            add_address_footer.setVisibility(View.VISIBLE);
        }
    }*/

    private void initViews() {
        add_address_list= (ListView) findViewById(R.id.add_address_list);
        add_address_footer= (LinearLayout) findViewById(R.id.add_address_header);
        add_address_header= (LinearLayout) findViewById(R.id.add_address_footer);

    }

    //尼玛还是返回键
    public void user_take_address(View view) {
        finish();
    }

    //go go去新建收货地址
    public void user_toaddAddress(View view) {
        Intent intent = new Intent(User_take_addressActivity.this, User_addAddressActivity.class);
        startActivity(intent);
    }
}
