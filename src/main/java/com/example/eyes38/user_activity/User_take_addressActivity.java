package com.example.eyes38.user_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.eyes38.R;
import com.example.eyes38.adapter.User_receiptaddressAdapter;
import com.example.eyes38.beans.ReceiptAddress;
import com.example.eyes38.beans.SpinnerSelect;
import com.example.eyes38.user_activity.AddressInfo.User_addAddressActivity;
import com.example.eyes38.utils.LoadMoreFooterView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class User_take_addressActivity extends AppCompatActivity {
    private ListView add_address_list;
    private LinearLayout add_address_header, add_address_footer;
    private SharedPreferences sp;//获取偏好设置
    private String newHeader;
    private List<ReceiptAddress> mListView;//存储收货地址的javabean
    private RequestQueue mRequestQueue;
    private int mWhat = 123;
    private int mjudge = 124;
    private User_receiptaddressAdapter mAdapter;
    Context mContext;
    private PtrFrameLayout ptrFrame;
    private String customer_id;//获取的用户id用于获取address_id
    private String address_id;//判断是否是默认地址
    private String header;//请求头信息
    private SpinnerSelect mSpinnerSelect;//记录选中的spinner的位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_take_address);
        mContext = this;
        initViews();
        //初始化数据并显示
        refresh();
        //下拉刷新的监听
        listener();
    }

    private void listener() {
        LoadMoreFooterView header = new LoadMoreFooterView(mContext);
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);
        //刷新
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrame.refreshComplete();
                        refresh();

                    }
                }, 1800);
            }
        });
    }

    private void refresh() {
        //获取头信息
        gethead();
        //获取接口的数据并显示
        getHttpMethod("http://38eye.test.ilexnet.com/api/mobile/customer-api/customers/" + customer_id, mjudge, null);

    }


    private void gethead() {
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String username = sp.getString("USER_NAME", "");
        String password = sp.getString("PASSWORD", "");
        customer_id = sp.getString("CUSTOMER_ID", "");
        String addHeader = username + ":" + password;
        newHeader = new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));//加密后的header
    }

    private void getHttpMethod(String url, int what, String address_id) {
        mListView = new ArrayList<>();
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        header = "Basic " + newHeader;
        request.addHeader("Authorization", header);
        request.setCacheMode(CacheMode.DEFAULT);
        mRequestQueue.add(what, request, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWhat) {
                //spinner获取数据
                String result = response.get();
                try {
                    //解析第一层
                    JSONObject object = new JSONObject(result);
                    JSONArray homefirst = object.getJSONArray("data");
                    for (int i = 0; i < homefirst.length(); i++) {
                        JSONObject jsonObject = homefirst.getJSONObject(i);
                        ReceiptAddress receiptAddress = new ReceiptAddress();
                        String firstname = jsonObject.getString("firstname");
                        String mobile = jsonObject.getString("mobile");
                        String address_1 = jsonObject.getString("address_1");
                        String district = jsonObject.getString("district");
                        String district_id = jsonObject.getString("district_id");
                        String maddress_id = jsonObject.getString("address_id");
                        String mcustomer_id = jsonObject.getString("customer_id");
                        receiptAddress.setDistrict_id(district_id);
                        receiptAddress.setFirstname(firstname);
                        receiptAddress.setMobile(mobile);
                        receiptAddress.setAddress_1(address_1);
                        receiptAddress.setDistrict(district);
                        receiptAddress.setAddress_id(maddress_id);
                        receiptAddress.setCustomer_id(mcustomer_id);
                        if (address_id.equals(maddress_id)) {
                            mListView.add(0, receiptAddress);
                        } else {
                            mListView.add(receiptAddress);
                        }
                    }
                    mAdapter = new User_receiptaddressAdapter(mContext, mListView,header);
                    add_address_list.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (what == mjudge) {
                //spinner获取数据
                String result = response.get();
                Log.e("kankanres", result);
                try {
                    //解析第一层
                    JSONObject object = new JSONObject(result);
                    JSONObject homefirst = object.getJSONObject("data");
                    address_id = homefirst.getString("address_id");
                    getHttpMethod("http://38eye.test.ilexnet.com/api/mobile/customer-api/customer-addresses", mWhat, address_id);
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

//    private void adjustViews() {
//        int count=add_address_list.getCount();//count表示listview当前数据的个数
//        if (count==0){
//            //表示当前没有地址，则显示空页面
//            add_address_header.setVisibility(View.VISIBLE);
//        }else {
//            //表示当前显示的有地址，则显示listview
//            add_address_footer.setVisibility(View.VISIBLE);
//        }
//    }

    private void initViews() {
        ptrFrame = (PtrFrameLayout) findViewById(R.id.user_take_address_ptr);
        add_address_list = (ListView) findViewById(R.id.add_address_list);
        //  add_address_footer= (LinearLayout) findViewById(R.id.add_address_header);
        // add_address_header= (LinearLayout) findViewById(R.id.add_address_footer);
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
