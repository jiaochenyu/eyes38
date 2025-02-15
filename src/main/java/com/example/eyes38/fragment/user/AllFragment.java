package com.example.eyes38.fragment.user;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.eyes38.R;
import com.example.eyes38.adapter.User_order_AllAdapter;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * Created by cfj on 2016/5/21.
 */
public class AllFragment extends Fragment {
    public static final int MFINISH = 222;
    public static final int REFRESHWHAT = 308;
    private static final int MWHAT = 221;
    User_orderActivity mMainActivity;
    View view;
    RecyclerView mRecyclerView;
    User_order_AllAdapter mUser_order_allAdapter = null;//适配器
    //用来存放全部订单的集合
    List<UserOrderBean> mList;
    //刷新界面
    private PtrClassicFrameLayout ptrFrame;
    private GridLayoutManager gridLayoutManager;
    List<UserOrderGoods> mGoodsList;
    //加载部分
    private ImageView loading;
    //上拉加载的footview
    private LinearLayout footView;
    //表示是否还有数据可以加载
    private boolean isLoad = true;
    UserOrderBean mUserOrderBean;
    UserOrderGoods userOrderGoods;
    String order_id;
    private Button cancel_order,pay_order,follow_order,evaluate_order;
    private boolean addFlag, totalFlag, flag;//用于加载的判断
    private int dataAllCount;//请求数据的总量
    private int everyRequestStart = 0, everyRequest;//一次请求的数据量
    //偏好设置
    private String newHeader;
    private SharedPreferences sp;
    private String customer_id, usernameValue, passwordValue;
    //Nohttp工具
    private RequestQueue mRequestQueue;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MFINISH:
                    if (dataAllCount <= 0) {
                        //没有数据可以继续加载
                        Toast.makeText(getActivity(), "加载完毕!", Toast.LENGTH_SHORT).show();
                        footView.setVisibility(View.GONE);
                    } else {
                        everyRequestStart = everyRequest;
                        if (dataAllCount - 6 <= 0) {
                            everyRequest = everyRequestStart + dataAllCount;
                            dataAllCount = 0;
                            flag = true;
                        }
                        HttpMethod();
                    }
                    break;
            }

        }
    };
    //控件定义
    private LinearLayout user_order_all_header, user_order_all_footer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainActivity = (User_orderActivity) getActivity();
        view = inflater.inflate(R.layout.activity_user_all, null);
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
                       HttpMethodReFresh();//请求数据
                        ptrFrame.refreshComplete();
                    }
                }, 1800);

            }
        });
        //上拉加载
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isScrolling = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && isScrolling && isLoad) {
                    int lastVisibleItem = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = gridLayoutManager.getItemCount();
                    if (lastVisibleItem == totalItemCount - 1) {
                        //显示footview
                        footView.setVisibility(View.VISIBLE);
                        loading.setBackgroundResource(R.drawable.anim);
                        final AnimationDrawable animationDrawable = (AnimationDrawable) loading.getBackground();
                        loading.post(new Runnable() {
                            @Override
                            public void run() {
                                animationDrawable.start();
                            }
                        });
                        Message message = new Message();
                        message.what = MFINISH;
                        mHandler.sendMessageDelayed(message, 2000);
                        isScrolling = false;

                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isScrolling = (dy > 0);
            }
        });
    }

    private void HttpMethod() {
        String url = "http://38eye.test.ilexnet.com/api/mobile/order-api/orders/list-by-customers/" + customer_id;
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        String addHeader = usernameValue + ":" + passwordValue;
        newHeader = new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));//加密后的header
        String header = "Basic " + newHeader;
        request.addHeader("Authorization", header);
        mRequestQueue.add(MWHAT, request, mOnResponseListener);
    }

    //刷新操作 只允许刷新6条数据
    private void HttpMethodReFresh() {
        String url = "http://38eye.test.ilexnet.com/api/mobile/order-api/orders/list-by-customers/" + customer_id;
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        String addHeader = usernameValue + ":" + passwordValue;
        newHeader = new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));//加密后的header
        String header = "Basic " + newHeader;
        request.addHeader("Authorization", header);
        mRequestQueue.add(REFRESHWHAT, request, mOnResponseListener);
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
                    JSONArray array = object1.getJSONArray("all");
                    if (!addFlag) {
                        dataAllCount = array.length();//仅仅第一次获取请求数据的总量
                        addFlag = true;
                    }
                    if (dataAllCount <= 6 && !totalFlag) {
                        //当数据总量少于一次请求的数据时  并且也是仅仅在第一次进入时判断
                        everyRequest = dataAllCount;//将数据全部加载
                        dataAllCount = dataAllCount - everyRequest;
                        totalFlag = true;
                    } else if (!flag) {
                        dataAllCount = dataAllCount - 6;
                        everyRequest = everyRequestStart + 6;
                    }
                    for (int i = everyRequestStart; i < everyRequest; i++) {
                        JSONObject object2 = array.getJSONObject(i);
                        String order_no = object2.getString("order_no");//物品订单号
                        String shipping_name = object2.getString("shipping_firstname");//收货人姓名
                        order_id = object2.getString("order_id");//订单号
                        String shipping_address = object2.getString("shipping_address_1");//收货人详细地址
                        String shipping_mobile = object2.getString("shipping_mobile");//收货人的手机号
                        double order_goods_amount = Double.parseDouble(object2.getString("order_goods_amount"));//除了运费的总金额
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
                            String image = object3.getString("image");//物品图片
                            userOrderGoods = new UserOrderGoods(image, name, price, quantity, order_id);
                            mGoodsList.add(userOrderGoods);

                        }
                        mUserOrderBean = new UserOrderBean(create_date, order_status_id, total_count, total, mGoodsList, order_no, shipping_name, shipping_mobile, shipping_address, order_goods_amount, shipping_district_id, order_id);
                        mList.add(mUserOrderBean);
                    }

                    //先判断应该显示哪个界面
                    if (mList.size() == 0) {
                        user_order_all_header.setVisibility(View.VISIBLE);
                    } else {
                        user_order_all_footer.setVisibility(View.VISIBLE);
                    }
                    mUser_order_allAdapter.notifyDataSetChanged();
                    Message message = new Message();
                    message.what = MFINISH;
                    footView.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (what == REFRESHWHAT) {
                //请求成功
                mList = new ArrayList<>();
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject object1 = object.getJSONObject("data");
                    JSONArray array = object1.getJSONArray("all");
                    for (int i = 0; i < 6; i++) {
                        JSONObject object2 = array.getJSONObject(i);
                        String order_no = object2.getString("order_no");//物品订单号
                        String shipping_name = object2.getString("shipping_firstname");//收货人姓名
                        order_id = object2.getString("order_id");//订单号
                        String shipping_address = object2.getString("shipping_address_1");//收货人详细地址
                        String shipping_mobile = object2.getString("shipping_mobile");//收货人的手机号
                        double order_goods_amount = Double.parseDouble(object2.getString("order_goods_amount"));//除了运费的总金额
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
                            String image = object3.getString("image");//物品图片
                            userOrderGoods = new UserOrderGoods(image, name, price, quantity, order_id);
                            mGoodsList.add(userOrderGoods);
                        }
                        mUserOrderBean = new UserOrderBean(create_date, order_status_id, total_count, total, mGoodsList, order_no, shipping_name, shipping_mobile, shipping_address, order_goods_amount, shipping_district_id, order_id);
                        mList.add(mUserOrderBean);
                    }
                    //先判断应该显示哪个界面
                    if (mList.size() == 0) {
                        user_order_all_header.setVisibility(View.VISIBLE);
                    } else {
                        user_order_all_footer.setVisibility(View.VISIBLE);
                    }
                    mUser_order_allAdapter = new User_order_AllAdapter(mList, getContext());
                    mRecyclerView.setAdapter(mUser_order_allAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isLoad = true;
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
        user_order_all_header = (LinearLayout) view.findViewById(R.id.user_order_all_header);
        user_order_all_footer = (LinearLayout) view.findViewById(R.id.user_order_all_footer);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.user_order_all_recycle);
        ptrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.user_order_all_ptr);
        mList = new ArrayList<>();
        mUser_order_allAdapter = new User_order_AllAdapter(mList, getContext());
        mRecyclerView.setAdapter(mUser_order_allAdapter);
        //按钮部分
        cancel_order= (Button) view.findViewById(R.id.cancel_order);
        pay_order= (Button) view.findViewById(R.id.pay_order);
        follow_order= (Button) view.findViewById(R.id.follow_order);
        evaluate_order= (Button) view.findViewById(R.id.evalute_order);
        //加载部分
        footView = (LinearLayout) view.findViewById(R.id.user_all_footview);
        loading = (ImageView) view.findViewById(R.id.user_all_footview_image);
        //设置recycleview的布局管理
        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    //从网络上获取数据
    private void initData() {
        customer_id = sp.getString("CUSTOMER_ID", "");
        usernameValue = sp.getString("USER_NAME", "");
        passwordValue = sp.getString("PASSWORD", "");
        mRequestQueue = NoHttp.newRequestQueue();
    }

}
