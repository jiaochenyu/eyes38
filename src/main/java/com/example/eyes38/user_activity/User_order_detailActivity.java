package com.example.eyes38.user_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.adapter.User_order_orderAdapter;
import com.example.eyes38.beans.UserOrderBean;
import com.example.eyes38.beans.UserOrderGoods;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User_order_detailActivity extends AppCompatActivity {
    private UserOrderBean mUserOrderBean;
    private UserOrderGoods mOrderGoods;
    private ImageView mImageView, user_order_detail_home;
    private TextView user_order_detail_state;//订单状态
    private TextView user_order_detail_number;//订单号
    private TextView user_order_detail_total;//总金额
    private TextView user_order_detail_person;//订单人
    private TextView user_order_detail_tel;//订单手机号
    private TextView user_order_detail_address;//订单详细地址
    private RecyclerView user_order_detail_recycle;//订单详情页面的recycle
    private TextView user_order_detail_total_goods;//商品总额
    private TextView user_order_detail_yunfei;//商品运费
    private static final int MFINISH = 266;
    private static final int MWHAT = 267;
    private int district_id;
    private String detail_address1;
    private Context mContext;
    private User_order_orderAdapter mAdapter = null;
    //偏好设置
    private String newHeader;
    private SharedPreferences sp;
    private String usernameValue, passwordValue;
    //Nohttp工具
    private RequestQueue mRequestQueue;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MFINISH:
                    //设置收货人详细地址
                    String address_detail = detail_address1 + " " + mUserOrderBean.getShipping_address();
                    user_order_detail_address.setText(address_detail + "");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_detail);
        initViews();//初始化控件
        initData();//接受传过来的数据
        getHttpMethod();
        setData();//把对应的数据传入对应控件
    }

    private void getHttpMethod() {
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customer-addresses";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        String addHeader = usernameValue + ":" + passwordValue;
        newHeader = new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));//加密后的header
        String header = "Basic " + newHeader;
        request.addHeader("Authorization", header);
        mRequestQueue.add(MWHAT, request, mOnResponseListener);
    }

    //请求http结果
    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == MWHAT) {
                //请求成功
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        district_id = Integer.parseInt(object1.getString("district_id"));
                        while (mUserOrderBean.getShipping_district_id() == district_id) {
                            detail_address1 = object1.getString("district");
                            break;
                        }
                    }
                    Message message = new Message();
                    message.what = MFINISH;
                    mHandler.sendMessage(message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

    private void initData() {
        usernameValue = sp.getString("USER_NAME", "");
        passwordValue = sp.getString("PASSWORD", "");
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("User_Order_Bean");
        mUserOrderBean = (UserOrderBean) bundle.get("User_Order_Bean");
    }

    private void initViews() {
        mImageView = (ImageView) findViewById(R.id.user_order_detail_back);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        user_order_detail_state = (TextView) findViewById(R.id.user_order_detail_state);
        user_order_detail_number = (TextView) findViewById(R.id.user_order_detail_number);
        user_order_detail_total = (TextView) findViewById(R.id.user_order_detail_total);
        user_order_detail_person = (TextView) findViewById(R.id.user_order_detail_person);
        user_order_detail_tel = (TextView) findViewById(R.id.user_order_detail_tel);
        user_order_detail_address = (TextView) findViewById(R.id.user_order_detail_address);
        user_order_detail_total_goods = (TextView) findViewById(R.id.user_order_detail_total_goods);
        user_order_detail_yunfei = (TextView) findViewById(R.id.user_order_detail_yunfei);
        user_order_detail_home = (ImageView) findViewById(R.id.user_order_detail_home);
        user_order_detail_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_order_detailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        user_order_detail_recycle = (RecyclerView) findViewById(R.id.user_order_detail_recycle);
        mUserOrderBean = new UserOrderBean();
        mOrderGoods = new UserOrderGoods();
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//偏好设置初始化
        //设置recycleview的布局管理
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
        user_order_detail_recycle.setLayoutManager(gridLayoutManager);
    }

    //传入正确的值
    private void setData() {
        //返回键
        //设置订单状态
        int order_state = mUserOrderBean.getOrder_status_id();
        if (order_state == 16) {
            user_order_detail_state.setText("待付款");
        } else if (order_state == 7) {
            user_order_detail_state.setText("订单取消");
        }
        //设置订单号
        user_order_detail_number.setText(mUserOrderBean.getOrder_no() + "");
        //设置支付金额
        user_order_detail_total.setText(mUserOrderBean.getTotal() + "");
        //设置收货人名
        user_order_detail_person.setText(mUserOrderBean.getShipping_name());
        //设置收货人手机号
        String mobile = mUserOrderBean.getShipping_mobile();
        String mobile_noe = mobile.substring(0, 3);
        String mobile_two = mobile.substring(mobile.length()-4, mobile.length());
        user_order_detail_tel.setText(mobile_noe + "****" + mobile_two);
        //recycleview
        if (mAdapter == null) {
            mAdapter = new User_order_orderAdapter(mUserOrderBean.getmList(), this);
            user_order_detail_recycle.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        user_order_detail_total_goods.setText(mUserOrderBean.getShipping_freight() + "");
        //运费
        user_order_detail_yunfei.setText(mUserOrderBean.getTotal() - mUserOrderBean.getShipping_freight() + "");

    }

}
