package com.example.eyes38.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.adapter.Cart_GoodsAdapter;
import com.example.eyes38.fragment.cart.CartEmptyView;
import com.example.eyes38.fragment.cart.CartGoodsList;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by jcy on 2016/5/8.
 */
public class CartFragment extends Fragment {

    private final int mWhat = 2;
    private final int mFINFISH = 3;
    MainActivity mMainActivity;
    private View view;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    Cart_GoodsAdapter mCart_goodsAdapter;
    SharedPreferences sp;  //偏好设置 看用户登录是否登录
    RequestQueue mRequestQueue;

    public boolean cartState; //购物车是否为空

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case mFINFISH:
                   int num = (int) msg.obj;
                    if(num>0){
                        CartGoodsList mCartGoodsList = new CartGoodsList();
                        mFragmentTransaction.replace(R.id.cartcontent, mCartGoodsList);
                        mFragmentTransaction.commit();
                    }else{
                        //为空
                        CartEmptyView mCartEmptyView = new CartEmptyView();
                        mFragmentTransaction.replace(R.id.cartcontent, mCartEmptyView);
                        mFragmentTransaction.commit();
                    }
                    break;
            }
        }
    };




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        view = inflater.inflate(R.layout.cart, null);
        initViews();
        initData();
        initOnclickListener();
        //判断 购物车是否为空。 是空显示CartEmptyView
        initStates();
        return view;
    }

    private void initViews() {


    }

    private void initData() {
        mFragmentManager = getChildFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();


    }

    private void initStates() {
        //如果用户没有登录 那么显示空
        sp = mMainActivity.getSharedPreferences("userInfo", mMainActivity.MODE_PRIVATE);  // 偏好设置初始化
        int flag = sp.getInt("STATE", 0);  // 取出用户登录状态， 如果为1 代表登录 如果为0 是没有登录
        if (flag == 0) {
            //如果用户没登录  购物车显示空
            CartEmptyView mCartEmptyView = new CartEmptyView();
            mFragmentTransaction.replace(R.id.cartcontent, mCartEmptyView);
            mFragmentTransaction.commit();
        } else {
            //登录了 进行网络请求 判断用户购物车是否为空
            getHttpMethod();

        }
    }

    private void initOnclickListener() {

    }





    private void getHttpMethod() {
        mRequestQueue = NoHttp.newRequestQueue(); //默认是 3 个 请求
        String url = "http://api.dev.ilexnet.com/simulate/38eye/cart-api/cart";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.setRequestFailedReadCache(true);
        //request.add("shoppingCartIds", "219");
        mRequestQueue.add(mWhat, request, mOnResponseListener);
    }
    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }
        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWhat) {
                Log.e("请求成功", "success");
                //请求成功
                String result = response.get();
                int num =0;
                //JSON解析
                JSONObject object = null;
                try {
                    object = new JSONObject(result);
                    JSONObject object2 = object.getJSONObject("data");
                    JSONArray mJsonArray = object2.getJSONArray("data");
                    for (int i = 0; i < mJsonArray.length(); i++) {
                        JSONObject jsonObject = mJsonArray.getJSONObject(i);
                        JSONArray mJsonArray2 = jsonObject.getJSONArray("data");
                        if(mJsonArray2.length()>0){
                            num = mJsonArray2.length();
                            break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = mFINFISH;
                //Log.e("请求完成", "66666666666666");
                mHandler.sendMessage(mHandler.obtainMessage(mFINFISH,num));

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
