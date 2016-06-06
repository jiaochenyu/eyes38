package com.example.eyes38.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.example.eyes38.Application.Application;
import com.example.eyes38.R;
import com.example.eyes38.fragment.cart.CartEmptyView;
import com.example.eyes38.fragment.cart.CartGoodsList;
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

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class CartActivity extends AppCompatActivity {
    private final int mWhat = 2;
    private ImageView mBackImageView;
    private PtrClassicFrameLayout ptrFrame;//pulltorefresh
    SharedPreferences sp;  //偏好设置 看用户登录是否登录
    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initViews();
        initData();
        //判断 购物车是否为空。 是空显示CartEmptyView
        initStates();
    }



    private void initViews() {
        ptrFrame = (PtrClassicFrameLayout) findViewById(R.id.car_goods_refresh);
        mBackImageView = (ImageView) findViewById(R.id.cartactivity_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        sp = this.getSharedPreferences("userInfo",MODE_PRIVATE);
    }

    private void initListener() {
        //利用 pulltorefesh 刷新
        LoadMoreFooterView header = new LoadMoreFooterView(this); //刷新动画效果 自定义
        ptrFrame.setHeaderView(header); //刷新动画效果
        ptrFrame.addPtrUIHandler(header); //刷新动画效果
        //刷新方法
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
                        getHttpMethod();
                        ptrFrame.refreshComplete();
                    }
                }, 1800);
            }
        });
    }

    private void initStates() {
        //如果用户没有登录 那么显示空
        if (!Application.isLogin) {
            //如果用户没登录  购物车显示空
            CartEmptyView mCartEmptyView = new CartEmptyView();
            FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.replace(R.id.cartcontent, mCartEmptyView);
            mFragmentTransaction.commit();
        } else {
            //登录了 进行网络请求 判断用户购物车是否为空
            getHttpMethod();
        }
    }

    private void getHttpMethod() {
        mRequestQueue = NoHttp.newRequestQueue(); //默认是 3 个 请求
        String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.addHeader("Authorization", authorization());
        mRequestQueue.add(mWhat, request, mOnResponseListener);
    }
    ///获取用户名和密码
    private String authorization() {

        String username = sp.getString("USER_NAME","");  // 应该从偏好设置中获取账号密码
        String password = sp.getString("PASSWORD","");
        //Basic 账号+':'+密码  BASE64加密
        String addHeader = username + ":" + password;
        String authorization = "Basic " + new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));
        return authorization;
    }
    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }
        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWhat) {
                //请求成功
                String result = response.get();
                int num = 0; // 购物车商品
                //JSON解析
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONArray jsonArray2 = jsonObject.getJSONArray("data");
                        if (jsonArray2.length() > 0) {
                            num = jsonArray2.length();
                            break;
                        }
                    }
                    if (num > 0) {
                        //不为空显示购物车
                        CartGoodsList mCartGoodsList = new CartGoodsList();
                        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        mFragmentTransaction.replace(R.id.cartcontent, mCartGoodsList);
                        mFragmentTransaction.commit();
                    } else {
                        //为空
                        CartEmptyView mCartEmptyView = new CartEmptyView();
                        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                        mFragmentTransaction.replace(R.id.cartcontent, mCartEmptyView);
                        mFragmentTransaction.commit();
                    }
                    initListener();
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
            //请求结束。 通知初始化 适配器
        }
    };
}
