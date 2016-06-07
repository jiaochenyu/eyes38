package com.example.eyes38.fragment.cart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eyes38.R;
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
 * Created by jqchen on 2016/5/20.
 */
public class CartEmptyView extends Fragment {

    public final static int SHOWCARTLIST = 3;
    private final static int mWhat = 381; //请求成功
    private PtrClassicFrameLayout ptrFrame;
    private View mView;
    private RequestQueue mRequestQueue;
    private SharedPreferences sp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.cart_empty,null);
        initViews();
        initData();
        initListener();
        return mView;
    }


    private void initViews() {
        ptrFrame = (PtrClassicFrameLayout) mView.findViewById(R.id.car_goods_refresh);
    }
    private void initData(){
        sp = getActivity().getSharedPreferences("userInfo",getContext().MODE_PRIVATE);
    }
    private void initListener() {
        //利用 pulltorefesh 刷新
        LoadMoreFooterView header = new LoadMoreFooterView(mView.getContext()); //刷新动画效果 自定义
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

    //请求http 获取购物车信息
    private void getHttpMethod() {
        mRequestQueue = NoHttp.newRequestQueue(); //默认是 3 个 请求
        String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.addHeader("Authorization", authorization()); // 添加请求头
        //request.add("shoppingCartIds", "");
        mRequestQueue.add(mWhat, request, mOnResponseListener);
    }

    private String authorization() {
        String username = sp.getString("USER_NAME","");  // 应该从偏好设置中获取账号密码
        String password = sp.getString("PASSWORD","");
        //Basic 账号+':'+密码  BASE64加密
        String addHeader = username + ":" + password;
        String authorization = "Basic " + new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));
        return authorization;
    }
    /**
     * 请求http结果  回调对象，接受请求结果
     */
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
                        //不为空显示购物车 // 通知cartFragment显示cartlistFragment
                       /* CartGoodsList mCartGoodsList = new CartGoodsList();
                        FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
                        mFragmentTransaction.replace(R.id.cartcontent, mCartGoodsList);
                        mFragmentTransaction.commit(); */
                      /*  Handler handler = new CartFragment().mHandler;
                        handler.sendEmptyMessage(SHOWCARTLIST);*/
                    } /*if( num ==0) {
                        //为空
                        CartEmptyView mCartEmptyView = new CartEmptyView();
                        FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
                        mFragmentTransaction.replace(R.id.cartcontent, mCartEmptyView);
                        mFragmentTransaction.commit();
                    }*/
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
