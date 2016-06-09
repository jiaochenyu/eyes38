package com.example.eyes38.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.adapter.PayAdapter;
import com.example.eyes38.beans.CartGoods;
import com.example.eyes38.beans.Receipt;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PayActivity extends AppCompatActivity {
    public final static int mAdressIDWhat = 1;
    public final static int mDefaultAdressWhat = 2;
    private ImageView payBackImag; //返回
    private RelativeLayout mPayAddressRl, mNotEmptyRl;
    private TextView allGoodsPrice, peisongMoney, totalPrice; //收货地址(显示默认)、总价、运费、总价格(商品价格+运费)
    private TextView emptyTV, firstNameTV, phoneTV, districtTV;
    private int flag = 0;
    private TextView goPay;  // 去付款
    private RadioGroup mPayRadioGroup; //支付方式
    private RadioButton mWeChatRadioButton; //微信支付
    private RecyclerView mGoodsRecyclerView; // 显示订单商品
    private List<CartGoods> mList; // 把购物车选中的商品信息传过来
    private float peisong = 0;
    private RequestQueue mRequestQueue;
    private SharedPreferences sp; //偏好设置 获取密码和用户id
    PayAdapter mPayAdapter;
    private List<Receipt> mReceiptList;  // 默认地址
    private Receipt mReceipt;
    private String address_id; //收货地址id

    public float getPeisong() {
        return peisong;
    }

    public void setPeisong(float peisong) {
        this.peisong = peisong;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_jiesuan);
        initViews();
        initData();
        setView();
        initListener();
        getAdressIDNoHttp();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mReceipt = (Receipt) intent.getSerializableExtra("addressInfo");
        firstNameTV.setText(mReceipt.getReceipt_person());
        phoneTV.setText(mReceipt.getReceipt_phone());
        districtTV.setText(mReceipt.getDistrict() + " " + mReceipt.getReceipt_detail());
    }


    private void initViews() {
        payBackImag = (ImageView) findViewById(R.id.pay_back);
        mPayAddressRl = (RelativeLayout) findViewById(R.id.pay_adress);
        mNotEmptyRl = (RelativeLayout) findViewById(R.id.gopay_address_notempty);
        emptyTV = (TextView) findViewById(R.id.gopay_address_empty);   //为空收货地址
        firstNameTV = (TextView) findViewById(R.id.firstname);
        phoneTV = (TextView) findViewById(R.id.phone);
        districtTV = (TextView) findViewById(R.id.district);
        allGoodsPrice = (TextView) findViewById(R.id.allGoodsPrice);  //商品价格
        peisongMoney = (TextView) findViewById(R.id.peisongmoney); // 配送费用
        totalPrice = (TextView) findViewById(R.id.totalPrice);  // 总价 商品价格+配送费用
        mGoodsRecyclerView = (RecyclerView) findViewById(R.id.orderGoodsInfo);
        mGoodsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void setView() {
        DecimalFormat df = new DecimalFormat("0.00");
        String st = df.format(allGoodsPrice()); // 商品价格
        allGoodsPrice.setText(st);

        setPeisong(20);
        peisongMoney.setText(getPeisong() + "");  //配送价格

        String st2 = df.format(allGoodsPrice() + getPeisong()); //商品价格 + 配送价格
        totalPrice.setText(st2);
    }

    private void initData() {
        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE); //初始化偏好设置
        mList = new ArrayList<>();
        mReceiptList = new ArrayList<>();

        mReceipt = new Receipt();

        Intent intent = getIntent();
        mList = (List<CartGoods>) intent.getSerializableExtra("list"); // 从购物车中获取list

        mPayAdapter = new PayAdapter(mList, PayActivity.this);
        mGoodsRecyclerView.setAdapter(mPayAdapter);
    }

    private void initListener() {
        ClickListener clickListener = new ClickListener();
       /* mPayRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            }
        });*/
        payBackImag.setOnClickListener(clickListener);
        mPayAddressRl.setOnClickListener(clickListener);
    }

    //**********账号密码进行base64加密
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

    //根据收货地址id获取District(省市区)详细信息
    private void getDistrictNoHttp() {
        String customerID = sp.getString("CUSTOMER_ID", "");
        String path = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customer-addresses";
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(path, RequestMethod.GET);
        request.addHeader("Authorization", authorization());
        request.add("customer_id", customerID);
        mRequestQueue.add(mDefaultAdressWhat, request, mOnResponseListener);

    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mAdressIDWhat) {
                try {
                    String result = response.get();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    String address_id = jsonData.getString("address_id"); //默认收货地址
                    setAddress_id(address_id);
                    //执行获取
                    getDistrictNoHttp();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (what == mDefaultAdressWhat) {
                try {
                    String result = response.get();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        String addressid = jsonData.getString("address_id");
                        if (address_id.equals(addressid)) {
                            String receipt_person = jsonData.getString("firstname");
                            String receipt_phone = jsonData.getString("mobile");
                            String receipt_detail = jsonData.getString("address_1");
                            String district = jsonData.getString("district");
                            mReceipt.setReceipt_person(receipt_person);
                            mReceipt.setReceipt_phone(receipt_phone);
                            mReceipt.setReceipt_detail(receipt_detail);
                            mReceipt.setDistrict(district);
                            mReceiptList.add(mReceipt);
                            mNotEmptyRl.setVisibility(View.VISIBLE);
                            emptyTV.setVisibility(View.GONE);
                            firstNameTV.setText(receipt_person);
                            phoneTV.setText(receipt_phone);
                            districtTV.setText(district + " " + receipt_detail);
                        }
                    }
                    if (mReceiptList.size() == 0) {
                        emptyTV.setVisibility(View.VISIBLE);
                        mNotEmptyRl.setVisibility(View.GONE);
                    }
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


    //事件监听
    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.pay_back:
                    //返回键
                    //onBackPressed();
                    finish();
                    break;
                case R.id.pay_adress:
                    //点击这个跳转到选择地址界面
                    Intent intent = new Intent(PayActivity.this, PaySelectActivity.class);
                    startActivity(intent);
                    break;
                case R.id.weChatPay:
                    //选择了微信支付方式
                    break;
                case R.id.gopay:
                    //付款之前先判断收货地址 和支付方式
                    break;
            }
        }
    }

    private double allGoodsPrice() {
        double goodsPrice = 0;
        for (int i = 0; i < mList.size(); i++) {
            goodsPrice += mList.get(i).getPrice() * mList.get(i).getQuantity();
        }
        return goodsPrice;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("销毁了","销毁了payactivity");
    }
}
