package com.example.eyes38.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
                                String type = jsonObject1.getString("type");
                                if (!jsonObject1.getString("product").equals("false")) {
                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("product");
                                    int product_id = jsonObject2.getInt("product_id");
                                    String image = jsonObject2.getString("image");
                                    String name = jsonObject2.getString("name");
                                    Double price = jsonObject2.getDouble("price");
                                    float market_price = (float) jsonObject2.getDouble("market_price");
                                    String extension4 = jsonObject2.getString("extension4");
                                    int stock_num = jsonObject2.getInt("stock_num");
                                    String description = jsonObject2.getString("description");
                                    HomeContentContent hcc = new HomeContentContent();
                                    hcc.setGoods_id(product_id);
                                    hcc.setGoods_name(name);
                                    hcc.setPath(image);
                                    hcc.setGoods_unit(extension4);
                                    hcc.setGoods_market_price(market_price);
                                    hcc.setGoods_stock(stock_num);
                                    hcc.setGoods_description(description);
                                    if (type.equals("week")){
                                        hcc.setExtension("true");
                                    }
                                    mmList.add(hcc);
                                }

                                hc = new HomeContent(zhuantiname, mmList);
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
                Goods goods = new Goods();
                goods.setGoods_id(hcc.getGoods_id());
                goods.setGoods_name(hcc.getGoods_name());
                goods.setPath(hcc.getPath());
                goods.setGoods_unit(hcc.getGoods_unit());
                goods.setGoods_market_price(hcc.getGoods_market_price());
                goods.setGoods_platform_price(hcc.getGoods_platform_price());
                goods.setExtension(hcc.getExtension());
                goods.setGoods_description(hcc.getGoods_description());
                Intent intent = new Intent(mContext, GoodDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("values", goods);
                intent.putExtra("values", bundle);
                mContext.startActivity(intent);
            }
        });
    }
}
