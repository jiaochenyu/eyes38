package com.example.eyes38.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.adapter.PaySelectAdapter;
import com.example.eyes38.beans.Receipt;
import com.example.eyes38.user_activity.AddressInfo.User_addAddressActivity;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaySelectActivity extends AppCompatActivity {
    public final static int mAdressIDWhat = 1; //获取默认地址id
    public final static int mAdressWhat = 2; //获取所有地址集合

    public final static int mWhat = 1;
    private RecyclerView mRecyclerView;
    private TextView newAdressTV;
    private ImageView mBackImageView;
    private SharedPreferences sp;
    private List<Receipt> mReceiptList; // 收货地址
    private RequestQueue mRequestQueue;
    private PaySelectAdapter mPaySelectAdapter;
    private String defaultAddressId;

    public void setDefaultAddressId(String defaultAddressId) {
        this.defaultAddressId = defaultAddressId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_address);
        initViews();
        initData();
        initListener();
        getAdressIDNoHttp(); //先得到默认id然后获取地址集合
    }


    private void initViews() {

        mRecyclerView = (RecyclerView) findViewById(R.id.select_pay_adress_list);
        mBackImageView = (ImageView) findViewById(R.id.pay_select_activity_back);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        newAdressTV = (TextView) findViewById(R.id.pay_newaddress);

    }

    private void initData() {
        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE); //初始化偏好设置
        mReceiptList = new ArrayList<>();
    }

    private String authorization() {
        String username = sp.getString("USER_NAME", "");  // 应该从偏好设置中获取账号密码
        String password = sp.getString("PASSWORD", "");
        //Basic 账号+':'+密码  BASE64加密
        String addHeader = username + ":" + password;
        String authorization = "Basic " + new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));
        return authorization;
    }
    // -*****获取默认收货地址 首先获取用户id根据用户信息获取默认地址id
    private void getAdressIDNoHttp() {
        String customerID = sp.getString("CUSTOMER_ID", "");
        String path = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customers/" + customerID;
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(path, RequestMethod.GET);
        request.addHeader("Authorization", authorization());
        mRequestQueue.add(mAdressIDWhat, request, mOnResponseListener);
    }
    //得到地址集合
    private void getAddressNoHttp() {
        String customerID = sp.getString("CUSTOMER_ID", "");
        String path = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customer-addresses";
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(path, RequestMethod.GET);
        request.addHeader("Authorization", authorization());
        request.add("customer_id", customerID);
        mRequestQueue.add(mAdressWhat, request, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mAdressWhat) {
                try {
                    String result = response.get();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        String addressid = jsonData.getString("address_id");
                        String receipt_person = jsonData.getString("firstname");
                        String receipt_phone = jsonData.getString("mobile");
                        String receipt_detail = jsonData.getString("address_1");
                        String district = jsonData.getString("district");
                        Receipt mReceipt = new Receipt();
                        mReceipt.setReceipt_person(receipt_person); //姓名
                        mReceipt.setReceipt_phone(receipt_phone); //电话号码
                        mReceipt.setReceipt_detail(receipt_detail); //详细地址
                        mReceipt.setDistrict(district); //省市区
                        if (addressid.equals(defaultAddressId)){
                            mReceipt.setDefaultAddress(true);
                            mReceiptList.add(0,mReceipt);
                        }else{
                            mReceiptList.add(mReceipt);
                        }
                        // mReceiptList.add(mReceipt);
                    }
                    mPaySelectAdapter = new PaySelectAdapter(mReceiptList,PaySelectActivity.this);
                    mRecyclerView.setAdapter(mPaySelectAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //获取默认id
            if (what == mAdressIDWhat){
                try {
                    String result = response.get();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    String address_id = jsonData.getString("address_id"); //默认收货地址
                    setDefaultAddressId(address_id);
                    //执行获取
                    getAddressNoHttp();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception,
                             int responseCode, long networkMillis) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

    private void initListener() {
        newAdressTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到新建收货地址
                Intent intent = new Intent(PaySelectActivity.this, User_addAddressActivity.class);
                startActivity(intent);
            }
        });
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
