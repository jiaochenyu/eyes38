package com.example.eyes38.fragment.home;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.adapter.Home_ad_adapter;
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
 * Created by huangjiechun on 16/5/23.
 */
public class HomeLunboView {

    MainActivity mMainActivity;
    View view;
    public static final int IMAGE_UPDATE = 1;
    public static final int REFRESHTIME = 5 * 1000;
    public static final int IMAGE_CHANGED = 2;
    ViewPager mViewPager;
    ArrayList<View> mViewList;
    List<String> mList;
    int mCurrentItem = Integer.MAX_VALUE / 2;
    private RequestQueue mRequestQueue;
    private int mWhat =123;
    public static final int FINSH = 1;

    public HomeLunboView(MainActivity activity, ViewPager viewPager) {
        this.mMainActivity=activity;
        this.mViewPager=viewPager;
    }
    public  void startLubo(){
        view =  View.inflate(mMainActivity,R.layout.home,null);
        initData();
//        Home_ad_adapter myAdapter = new Home_ad_adapter(mViewList);
//        mViewPager.setAdapter(myAdapter);
//        mViewPager.setCurrentItem(mCurrentItem);
//        mHandler.sendEmptyMessageDelayed(IMAGE_UPDATE,REFRESHTIME);
    }
    private void setLinstener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                //手动滑到这个广告的时候,发送改位置的值
                mHandler.sendMessage(Message.obtain(mHandler,IMAGE_CHANGED,position,0));
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initData() {
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/article-api/banner-images";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        mRequestQueue.add(mWhat, request, mOnResponseListener);

//        mViewList = new ArrayList<View>();
//        int[] imageId = {R.drawable.banner01, R.drawable.banner1,R.drawable.banner01,R.drawable.banner1};
//        for (int i = 0; i < imageId.length; i++) {
//            View view = View.inflate(mMainActivity,R.layout.home_ad_item, null);
//            ImageView mItemIvContent = (ImageView) view.findViewById(R.id.item_iv_content);
//            mItemIvContent.setImageResource(imageId[i]);
//            mViewList.add(view);
//        }
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
                        Log.e("获取的数据jjjj",address);
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
                    Log.e("handler", "hhhhhhh");
                    //初始化适配器
                    initAdapter();
                    //这是监听
                    setLinstener();
            }
        }
    };

    private void initAdapter() {
        mViewList = new ArrayList<View>();
        for (int i = 0; i < mList.size(); i++) {
            View view = View.inflate(mMainActivity,R.layout.home_ad_item, null);
            ImageView mItemIvContent = (ImageView) view.findViewById(R.id.item_iv_content);
            Glide.with(mMainActivity).load(mList.get(i)).into(mItemIvContent);
            //Log.e("获取的",);
            mViewList.add(view);
        }
        Home_ad_adapter myAdapter = new Home_ad_adapter(mViewList);
        mViewPager.setAdapter(myAdapter);
        mViewPager.setCurrentItem(mCurrentItem);
        mHandler.sendEmptyMessageDelayed(IMAGE_UPDATE,REFRESHTIME);

    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int action = msg.what;
            if (mHandler.hasMessages(IMAGE_UPDATE)){
                mHandler.removeMessages(IMAGE_UPDATE);
            }
            switch (action){
                case IMAGE_UPDATE:
                    //轮播图经行更新
                    mCurrentItem +=1;
                    mViewPager.setCurrentItem(mCurrentItem);
                    mHandler.sendEmptyMessageDelayed(IMAGE_UPDATE, REFRESHTIME);
                    break;
                case IMAGE_CHANGED:
                    //手动滑了广告
                    mCurrentItem = msg.arg1;
                    mViewPager.setCurrentItem(mCurrentItem);
                    mHandler.sendEmptyMessageDelayed(IMAGE_UPDATE,REFRESHTIME);
                    break;

            }
        }
    };
}
