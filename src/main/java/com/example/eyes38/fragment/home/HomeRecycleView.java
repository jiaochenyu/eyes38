package com.example.eyes38.fragment.home;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.eyes38.adapter.Home_item_adapter;
import com.example.eyes38.beans.HomeGrid;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**此类写的是recycleView的实现
 * Created by huangjiechun on 16/5/23.
 */
public class HomeRecycleView {

    private RecyclerView mRecyclerView;
    private Home_item_adapter mAdapter;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private int mWhat =123;
    public static final int FINSH = 1;
    public HomeRecycleView(Context context,RecyclerView recycler) {
        this.mContext=context;
        this.mRecyclerView=recycler;
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
        String url = "http://api.dev.ilexnet.com/simulate/38eye/product-api/products/:product_id=4";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        mRequestQueue.add(mWhat, request, mOnResponseListener);
    }

    private ArrayList<HomeGrid> mList;
    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWhat) {
                //请求成功
                String result = response.get();
                Log.e("resultwcoaosss",result);
                try {
                    //解析第一层
                    JSONObject object = new JSONObject(result);
                    //JSONArray homelunbo = object.getJSONArray("data");
                    mList = new ArrayList<HomeGrid>();
//                    for (int i = 0; i < homelunbo.length(); i++) {
//                        JSONObject jsonObject = homelunbo.getJSONObject(i);
//                        HomeGrid address = new HomeGrid();
//                        address.setPic(jsonObject.getString("image"));
//                        mList.add(address);
//                        Log.e("获取的数据recycle",jsonObject.getString("image"));
//                    }
                    JSONObject object1 = object.getJSONObject("data");
                    HomeGrid address = new HomeGrid();
                    address.setPic(object1.getString("image"));
                    Log.e("获取的数据recycle",object1.getString("image"));
                    mList.add(address);
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
                    Log.e("handler", "hhhhhhh");
                    //初始化适配器
                       initAdapter();
                    //这是监听
                    //setLinstener();
            }
        }
    };

    private void initAdapter() {
        mAdapter = new Home_item_adapter(mList,mContext);
        mRecyclerView.setAdapter(mAdapter);
        //设置recycleview的布局管理
        //listview风格
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        //grid
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }
}
