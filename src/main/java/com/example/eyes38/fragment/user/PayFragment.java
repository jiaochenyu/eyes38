package com.example.eyes38.fragment.user;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.eyes38.R;
import com.example.eyes38.adapter.ComeBack;
import com.example.eyes38.adapter.User_order_AllAdapter;
import com.example.eyes38.adapter.User_order_PayAdapter;
import com.example.eyes38.beans.UserOrderBean;
import com.example.eyes38.beans.UserOrderGoods;
import com.example.eyes38.user_activity.User_orderActivity;
import com.example.eyes38.utils.LoadMoreFooterView;
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

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * Created by cfj on 2016/5/8.
 */
public class PayFragment extends Fragment {
    public static final int MFINISH = 223;
    private static final int MWHAT = 224;
    User_orderActivity mMainActivity;
    View view;
    RecyclerView mRecyclerView;
    String order_id;
    User_order_PayAdapter mUser_order_payAdapter = null;//适配器
    //用来存放全部订单的集合
     private String header;
    List<UserOrderBean> mList;
    List<UserOrderGoods> mGoodsList;
    UserOrderBean mUserOrderBean;
    UserOrderGoods userOrderGoods;
    private PtrClassicFrameLayout ptrFrame;
    //偏好设置
    private String newHeader;
    private SharedPreferences sp;
    private String customer_id, usernameValue, passwordValue;
    //Nohttp工具
    private RequestQueue mRequestQueue;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MFINISH:
                    HttpMethod();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    //控件定义
    private LinearLayout user_order_pay_header, user_order_pay_footer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainActivity = (User_orderActivity) getActivity();
        view = inflater.inflate(R.layout.activity_user_pay, null);
        initViews();//初始化控件
        //获取数据
        initData();
        //Nohttp请求
        HttpMethod();//请求数据
        initListener();//下拉监听刷新
        return view;
    }
    private void initListener() {
        LoadMoreFooterView header = new LoadMoreFooterView(getContext());
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
                        //Nohttp请求
                        HttpMethod();//请求数据
                        ptrFrame.refreshComplete();
                    }
                }, 1800);

            }
        });

    }
    private void HttpMethod() {
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/order-api/orders/list-by-customers/" + customer_id;
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        String addHeader = usernameValue + ":" + passwordValue;
        newHeader = new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));//加密后的header
        header = "Basic " + newHeader;
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
                    JSONObject object1 = object.getJSONObject("data");
                    JSONArray array = object1.getJSONArray("unpayed");
                    mList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object2 = array.getJSONObject(i);
                        String order_no = object2.getString("order_no");//物品订单号
                        double order_goods_amount= Double.parseDouble(object2.getString("order_goods_amount"));//除了运费的总金额
                        String shipping_name = object2.getString("shipping_firstname");//收货人姓名
                        order_id=object2.getString("order_id");//订单号
                        String shipping_address = object2.getString("shipping_address_1");//收货人详细地址
                        String shipping_mobile = object2.getString("shipping_mobile");//收货人的手机号
                        int shipping_district_id = Integer.parseInt(object2.getString("shipping_district_id"));//收货人小区的地址
                        double total = Double.parseDouble(object2.getString("total"));//物品总价
                        int order_status_id = Integer.parseInt(object2.getString("order_status_id"));//订单状态
                        String date = object2.getString("create_date");//下单日期
                        String create_date = date.substring(0, 10);//截取日期的长度
                        //再次进入一层
                        JSONArray array1 = object2.getJSONArray("products");
                        int total_count = array1.length();
                        mGoodsList = new ArrayList<>();
                        for (int j = 0; j < array1.length(); j++) {
                            JSONObject object3 = array1.getJSONObject(j);
                            String order_id=object3.getString("order_id");//订单id
                            String name = object3.getString("name");//物品名称
                            int quantity = Integer.parseInt(object3.getString("quantity"));//物品数量
                            double price = Double.parseDouble(object3.getString("price"));//物品单价
                            String image = object3.getString("image");//物品图片
                            userOrderGoods = new UserOrderGoods(image, name, price, quantity,order_id);
                            mGoodsList.add(userOrderGoods);
                        }
                        mUserOrderBean = new UserOrderBean(create_date, order_status_id, total_count, total, mGoodsList, order_no, shipping_name, shipping_mobile, shipping_address,order_goods_amount, shipping_district_id,order_id);
                        mList.add(mUserOrderBean);
                    }
                    //先判断应该显示哪个界面
                    if (mList.size() == 0) {
                        user_order_pay_header.setVisibility(View.VISIBLE);
                    }else {
                        user_order_pay_header.setVisibility(View.GONE);
                        user_order_pay_footer.setVisibility(View.VISIBLE);
                        mUser_order_payAdapter = new User_order_PayAdapter(mList, getContext(),new ComeBack(){
                            @Override
                            public void getNotity() {
                                HttpMethod();
                                mUser_order_payAdapter.notifyDataSetChanged();
                            }
                        });

                        mRecyclerView.setAdapter(mUser_order_payAdapter);
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

    private void initViews() {
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);//偏好设置初始化
        user_order_pay_header = (LinearLayout) view.findViewById(R.id.user_order_pay_header);
        user_order_pay_footer = (LinearLayout) view.findViewById(R.id.user_order_pay_footer);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.user_order_pay_recycle);
        ptrFrame= (PtrClassicFrameLayout) view.findViewById(R.id.user_order_pay_ptr);
        //设置recycleview的布局管理
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

    }

    //从网络上获取数据
    private void initData() {
        customer_id = sp.getString("CUSTOMER_ID", "");
        usernameValue = sp.getString("USER_NAME", "");
        passwordValue = sp.getString("PASSWORD", "");
    }


}
