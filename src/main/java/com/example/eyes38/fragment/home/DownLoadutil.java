package com.example.eyes38.fragment.home;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
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

/**
 * Created by huangjiechun on 16/5/25.
 */
public class DownLoadutil {
    ArrayList<View> mViewList;
    MainActivity mMainActivity;
    private RequestQueue mRequestQueue;
    private int mWhat;
    public static final int FINSH = 1;
    private ArrayList<String> mList;

    public void downLoadtuil() {
       mRequestQueue = NoHttp.newRequestQueue();
       String url = "http://api.dev.ilexnet.com/simulate/38eye/article-api/banner-images";
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
                try {
                    //解析第一层
                    JSONObject object = new JSONObject(result);
                    JSONArray homelunbo = object.getJSONArray("data");
                    mList = new ArrayList<String>();
                    for (int i = 0; i < homelunbo.length(); i++) {
                        JSONObject jsonObject = homelunbo.getJSONObject(i);
                        String address = jsonObject.getString("image");
                        mList.add(address);
                        Log.e("获取的数据jjjj", address);
                    }
                    mViewList = new ArrayList<View>();
                    for (int i = 0; i < mList.size(); i++) {
                        View view = View.inflate(mMainActivity, R.layout.home_ad_item, null);
                        ImageView mItemIvContent = (ImageView) view.findViewById(R.id.item_iv_content);
                        Glide.with(mMainActivity).load(mList.get(i)).into(mItemIvContent);
                        mViewList.add(view);
                    }
                //    initAdapter();
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
                    //   initAdapter();
                    //这是监听
                 //   setLinstener();
            }
        }
    };
}
