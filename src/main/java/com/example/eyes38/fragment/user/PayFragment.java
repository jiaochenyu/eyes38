package com.example.eyes38.fragment.user;


import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.eyes38.adapter.User_order_AllAdapter;
import com.example.eyes38.beans.UserOrderBean;
import com.example.eyes38.beans.UserOrderGoods;
import com.example.eyes38.user_activity.User_orderActivity;
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


/**
 * Created by cfj on 2016/5/8.
 */
public class PayFragment extends Fragment {
    private static final int MFINISH = 223;
    private static final int MWHAT = 224;
    User_orderActivity mMainActivity;
    View view;
    RecyclerView mRecyclerView;
    private double goods_total;
    User_order_AllAdapter mUser_order_allAdapter = null;//适配器
    //用来存放全部订单的集合
    List<UserOrderBean> mList;
    List<UserOrderGoods> mGoodsList;
    UserOrderBean mUserOrderBean;
    UserOrderGoods userOrderGoods;
    //偏好设置
    private String newHeader;
    private SharedPreferences sp;
    private String customer_id, usernameValue, passwordValue;
    //Nohttp工具
    private RequestQueue mRequestQueue;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MFINISH:

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
        return view;
    }

    private void HttpMethod() {
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/order-api/orders/list-by-customers/" + customer_id;
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        String addHeader = usernameValue + ":" + passwordValue;
        newHeader = new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));//加密后的header
        String header = "Basic " + newHeader;
        Log.e("TTT", header);
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
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object2 = array.getJSONObject(i);
                        String order_no = object2.getString("order_no");//物品订单号
                        String shipping_name = object2.getString("shipping_firstname");//收货人姓名
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
                            String name = object3.getString("name");//物品名称
                            int quantity = Integer.parseInt(object3.getString("quantity"));//物品数量
                            double price = Double.parseDouble(object3.getString("price"));//物品单价
                            goods_total += price * quantity;
                            String image = object3.getString("image");//物品图片
                            Log.e("kkk123", name + "->" + image + "->" + price + "->" + quantity);
                            userOrderGoods = new UserOrderGoods(image, name, price, quantity);
                            mGoodsList.add(userOrderGoods);

                        }
                        double shipping_freight = total - goods_total;//当前购物的运费
                        mUserOrderBean = new UserOrderBean(create_date, order_status_id, total_count, total, mGoodsList, order_no, shipping_name, shipping_mobile, shipping_address, shipping_freight, shipping_district_id);
                        mList.add(mUserOrderBean);
                    }
                    Log.e("array", mList.size() + "N");
                    //先判断应该显示哪个界面
                    if (mList.size() == 0) {
                        user_order_pay_header.setVisibility(View.VISIBLE);
                    } else {
                        user_order_pay_footer.setVisibility(View.VISIBLE);
                    }
                    if (mUser_order_allAdapter == null) {
                        mUser_order_allAdapter = new User_order_AllAdapter(mList, getContext());
                        mRecyclerView.setAdapter(mUser_order_allAdapter);
                    } else {
                        mUser_order_allAdapter.notifyDataSetChanged();
                    }
                    Message message = new Message();
                    message.what = MFINISH;

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
        //设置recycleview的布局管理
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

    }

    //从网络上获取数据
    private void initData() {
        customer_id = sp.getString("CUSTOMER_ID", "");
        usernameValue = sp.getString("USER_NAME", "");
        passwordValue = sp.getString("PASSWORD", "");
        mList = new ArrayList<>();

    }
}
