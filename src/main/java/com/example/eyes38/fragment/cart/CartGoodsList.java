package com.example.eyes38.fragment.cart;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.adapter.Cart_GoodsAdapter;
import com.example.eyes38.beans.CartGoods;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jqchen on 2016/5/20.
 */
public class CartGoodsList extends Fragment {
    private final static int mWhat = 520;
    private View mView;
    MainActivity mMainActivity;
    private List<CartGoods> mList;
    private RecyclerView mRecyclerView;
    private TextView mCountTopTextView;  // n件商品 有包邮优惠
    Cart_GoodsAdapter mCart_goodsAdapter = null;
    //采用 NoHttp
    //创建 请求队列成员变量
    private RequestQueue mRequestQueue;
    //Handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 6666:
                    /*mCart_goodsAdapter = new Cart_GoodsAdapter(mList,mMainActivity);*/
                  /*  mCart_goodsAdapter.setOnItemClickListener(new Cart_GoodsAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, CartGoods cartGoods) {
                            Toast.makeText(mMainActivity,cartGoods.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    Log.e("handler接受message","测试事实上司是事实");
                    //Toast.makeText(mMainActivity, "andler接受messag", Toast.LENGTH_SHORT).show();
                    initListener();
                    break;

            }
            super.handleMessage(msg);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        mView = inflater.inflate(R.layout.cart_goods, null);
        initViews();
        initData();
        getHttpMethod();
       // initListener();
        return mView;
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.car_goods_list_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCountTopTextView = (TextView) mView.findViewById(R.id.checkedCountTop);
    }

    private void initData() {
        mList = new ArrayList<>();
    }

    private void initListener() {
        mCart_goodsAdapter.setOnItemClickListener(new Cart_GoodsAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, CartGoods cartGoods) {
                Toast.makeText(mMainActivity,cartGoods.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //请求http 获取图片
    private void getHttpMethod() {
        mRequestQueue = NoHttp.newRequestQueue(); //默认是 3 个 请求
        String url = "http://fuwuqi.guanweiming.top/headvip/json/testdata";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("size", "7");
        mRequestQueue.add(mWhat, request, mOnResponseListener);

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

                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray cartgoods = object.getJSONArray("goods");
                    for (int i = 0; i < cartgoods.length(); i++) {
                        JSONObject jsonObject = cartgoods.getJSONObject(i);
                        CartGoods mCartGoods = new CartGoods();
                        mCartGoods.setPath(jsonObject.getString("pic"));
                        mCartGoods.setTitle(jsonObject.getString("title"));
                        mCartGoods.setPrice(i);
                        mCartGoods.setNum(i);
                        Log.e("获取的数据jjjj", jsonObject.getString("pic") + jsonObject.getString("title"));
                        mList.add(mCartGoods);
                    }

                    mCart_goodsAdapter = new Cart_GoodsAdapter(mList, mMainActivity);
                    mRecyclerView.setAdapter(mCart_goodsAdapter);
                    Message message = new Message();
                    message.what = 6666;
                    Log.e("请求完成","66666666666666");
                    mHandler.sendMessage(message);

                 /*   mCart_goodsAdapter.setOnItemClickListener(new Cart_GoodsAdapter.OnRecyclerViewItemClickListener() {

                        @Override
                        public void onItemClick(View view, CartGoods data) {
                            Toast.makeText(mMainActivity, data.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    mCountTopTextView.setText(mList.size() + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


               /* Gson gson = new Gson();
                try {
                //  用 Gson 必须 与实体 一一对应
                    JSONObject object = new JSONObject(result);
                    JSONArray cartgoods = object.getJSONArray("goods");
                    for (int i = 0; i < cartgoods.length(); i++) {
                        CartGoods mCartGoods = gson.fromJson(cartgoods.getJSONObject(i).toString(),CartGoods.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

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
