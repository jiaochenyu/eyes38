package com.example.eyes38.EventActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.eyes38.R;
import com.example.eyes38.adapter.EventRecycleViewAdapter;
import com.example.eyes38.beans.EventContentGood;
import com.example.eyes38.utils.DividerItemDecoration;
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
 * Created by weixiao on 2016/5/24.
 */
public class EventDay2 extends Fragment {
    public static final int mWhat = 532;
    public static final int mFinish = 533;
    EventActivity mEventActivity;
    ImageView mImageView;
    View view;
    RecyclerView mRecyclerView;
    EventRecycleViewAdapter mEventRecycleViewAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新
    //适配器
    List<EventContentGood> mList;
    //采用NoHttp
    private RequestQueue mRequestQueue;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case mFinish:
                    //说明数据加载完成
                    break;
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mEventActivity = (EventActivity) getActivity();
        view = inflater.inflate(R.layout.event_day2, null);
        initViews();
        initData();
        getHttpMethod();
        return view;
    }

    //请求http获取数据
    private void getHttpMethod() {
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://api.dev.ilexnet.com/simulate/38eye/cart-api/cart";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        mRequestQueue.add(mWhat, request, mOnResponseListener);
    }

    /**
     * 请求http结果 回调对象 接受请求结果
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
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    JSONArray data = jsonObject1.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        JSONArray jsonArray = object.getJSONArray("data");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String text = jsonObject2.getString("product_name");
                            Log.e("ss",text);
                            JSONObject jsonObject3=jsonObject2.getJSONObject("product");
                            String image=jsonObject3.getString("image");
                            Log.e("ss",image);
                            EventContentGood good = new EventContentGood();
                            good.setTitle(text);
                            good.setPic(image);
                            mList.add(good);
                            Log.e("TAg", image + "-->" + text);
                        }
                        mEventRecycleViewAdapter = new EventRecycleViewAdapter(mList, mEventActivity);
                        mRecyclerView.setAdapter(mEventRecycleViewAdapter);
                        Message message = new Message();
                        message.what = mFinish;


                    }
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

    private void initData() {
        mList = new ArrayList<>();
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.event_day2_recycle);
        //设置recycleview布局
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        //增加下划线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));

    }
}
