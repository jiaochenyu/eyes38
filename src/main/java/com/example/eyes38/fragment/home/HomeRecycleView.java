package com.example.eyes38.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.eyes38.activity.GoodDetailActivity;
import com.example.eyes38.adapter.Home_item_adapter;
import com.example.eyes38.beans.Goods;
import com.example.eyes38.beans.HomeContent;
import com.example.eyes38.beans.HomeContentContent;
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
 * 此类写的是recycleView的实现
 * Created by huangjiechun on 16/5/23.
 */
public class HomeRecycleView {

    private RecyclerView mRecyclerView;
    private Home_item_adapter mAdapter;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private int mWhat = 123;
    public static final int FINSH = 1;
    String sortname;
    private ArrayList<HomeContent> mList;
    private HomeContent hc;

    public HomeRecycleView(Context context, RecyclerView recycler, String zhuantiname) {
        this.mContext = context;
        this.mRecyclerView = recycler;
        this.sortname = zhuantiname;
    }

    public void startItem() {
        initData();
    }

    private void initData() {
        //  mData = new ArrayList<HomeGrid>();
//        HomeGrid hg1 = new HomeGrid(R.drawable.home_c1);
//        HomeGrid hg2 = new HomeGrid(R.drawable.home_c2);
//        HomeGrid hg3 = new HomeGrid(R.drawable.home_c3);
//        HomeGrid hg4 = new HomeGrid(R.drawable.home_c4);
//        mData.add(hg1);
//        mData.add(hg2);
//        mData.add(hg3);
//        mData.add(hg4);

        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/special-product/listConfig";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
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
                mList = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String zhuantiname = jsonObject.getString("name");
                        if (sortname.equals(zhuantiname)) {
                            //初始化mmlist
                            List<HomeContentContent> mmList = new ArrayList<>();
                            JSONArray array2 = jsonObject.getJSONArray("products");
                            for (int j = 0; j < array2.length(); j++) {
                                JSONObject jsonObject1 = array2.getJSONObject(j);
                                if (!jsonObject1.getString("product").equals("false")) {
                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("product");
                                    String image = jsonObject2.getString("image");
                                    String name = jsonObject2.getString("name");
                                    Double price = jsonObject2.getDouble("price");
                                    String extension4 = jsonObject2.getString("extension4");
                                    HomeContentContent hcc = new HomeContentContent(image, name, price, extension4);
                                    mmList.add(hcc);
                                }

                                hc = new HomeContent(zhuantiname, mmList);
                                if (mmList.size() == 0) {
                                    Log.e("看看mmlist里有什么", zhuantiname);
                                } else {
                                    Log.e("看看mmlist里有什么", zhuantiname + mmList.get(0).toString());
                                }
                                mList.add(hc);
                            }
                        }
                    }
                    handler.sendEmptyMessage(FINSH);
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
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FINSH:
                    //初始化适配器
                    initAdapter();
                    //这是监听
                    //setLinstener();
            }
        }
    };

    private void initAdapter() {

        //设置recycleview的布局管理
        //listview风格
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        //grid
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new Home_item_adapter(mList, mContext, sortname);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setmOnItemClickListener(new Home_item_adapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, HomeContentContent hcc) {
                //跳转到商品详情页面,传一个goods对象,键值是values,
<<<<<<< HEAD
                Goods goods = new Goods(0, hcc.getName(), hcc.getImage(), null, 0, 0, 0, 0, "");
=======
<<<<<<< HEAD



                Goods goods = new Goods(0, hcc.getName(), hcc.getImage(), null, 0, 0, 0, 0, "");


=======

                Goods goods = new Goods(0, hcc.getName(), hcc.getImage(), null, 0, 0, 0, 0, "");
>>>>>>> 5566d1ca6651c37a3350fcd57eae2ca394e103dd
>>>>>>> 19a785258421f7a7212f318bc1c0d7b65484a20c
                Intent intent = new Intent(mContext, GoodDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("values", goods);
                intent.putExtra("values", bundle);
                mContext.startActivity(intent);
            }
        });
    }
}
