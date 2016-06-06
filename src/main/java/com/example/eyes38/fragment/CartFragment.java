package com.example.eyes38.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


/**
 * Created by jcy on 2016/5/8.
 */
public class CartFragment extends Fragment {

    private final int mWhat = 2;
    private View view;
    RequestQueue mRequestQueue;
    private PtrClassicFrameLayout ptrFrame;
    private SharedPreferences sp; // 偏好设置

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cart, null);
        initViews();
        initData();
        //判断 购物车是否为空。 是空显示CartEmptyView
        initStates();
        return view;
    }

    private void initViews() {
        ptrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.car_goods_refresh);

    }

    private void initData() {
     /*   mFragmentManager = getChildFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();*/
        sp = getActivity().getSharedPreferences("userInfo",getContext().MODE_PRIVATE);
    }

    private void initStates() {
        //如果用户没有登录 那么显示空
        if (!Application.isLogin) {
            //如果用户没登录  购物车显示空
            CartEmptyView mCartEmptyView = new CartEmptyView();
            FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
            mFragmentTransaction.replace(R.id.cartcontent, mCartEmptyView);
            mFragmentTransaction.commit();
        } else {
            //登录了 进行网络请求 判断用户购物车是否为空
            getHttpMethod();
        }
    }

    private void initOnclickListener() {
        //利用 pulltorefesh 刷新
        LoadMoreFooterView header = new LoadMoreFooterView(view.getContext()); //刷新动画效果 自定义
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

    ///获取用户名和密码
    private String authorization() {

        String username = sp.getString("USER_NAME","");  // 应该从偏好设置中获取账号密码
        String password = sp.getString("PASSWORD","");
        //Basic 账号+':'+密码  BASE64加密
        String addHeader = username + ":" + password;
        String authorization = "Basic " + new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));
        return authorization;
    }
    private void getHttpMethod() {
        mRequestQueue = NoHttp.newRequestQueue(); //默认是 3 个 请求
        String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.addHeader("Authorization", authorization());
        mRequestQueue.add(mWhat, request, mOnResponseListener);
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
                        FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
                        mFragmentTransaction.replace(R.id.cartcontent, mCartGoodsList);
                        mFragmentTransaction.commit();
                    } else {
                        //为空
                        CartEmptyView mCartEmptyView = new CartEmptyView();
                        FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
                        mFragmentTransaction.replace(R.id.cartcontent, mCartEmptyView);
                        mFragmentTransaction.commit();
                    }
                    initOnclickListener();
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


}
