package com.example.eyes38.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eyes38.EventActivity.EventActivity;
import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.activity.home.HomexptjActivity;
import com.example.eyes38.adapter.Home_ad_adapter;
import com.example.eyes38.beans.HomeFourSort;
import com.example.eyes38.fragment.home.HomeLunboView;
import com.example.eyes38.fragment.home.HomeRecycleView;
import com.example.eyes38.fragment.home.HomeSpinnerView;
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
 * Created by jcy on 2016/5/8.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    MainActivity mMainActivity;
    View view;
    //初始化轮播图的viewpager
    ViewPager mViewPager;
    //初始化recycleView
    RecyclerView mRecyclerView;
    //封装的recycleView实现类
    HomeRecycleView mHomeRecycleView;
    //封装的轮播图的实现类
    HomeLunboView mHomeLunboView;
    //初始化spinner
    Spinner mSpinner;
    //封装的spinner的实现类
    HomeSpinnerView mHomeSpinnerView;

    ImageView home_xptjgengduo;
    ImageView home_yzcpgengduo;
    private int mWhat = 123;
    ArrayList<View> mViewList;
    List<String> mList;
    List<HomeFourSort> mHomeFourSortsList;
    public static final int FINSH = 3;
    int mCurrentItem = Integer.MAX_VALUE / 2;
    public static final int IMAGE_UPDATE = 1;
    public static final int REFRESHTIME = 5 * 1000;
    public static final int IMAGE_CHANGED = 2;

    ImageView mImageView;
    int height;
    private RequestQueue mRequestQueue;


    //4个home_sort的图标和文字初始化
    private ImageView home_sort1image;
    private ImageView home_sort2image;
    private ImageView home_sort3image;
    private ImageView home_sort4image;
    private TextView home_sort1text;
    private TextView home_sort2text;
    private TextView home_sort3text;
    private TextView home_sort4text;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, null);
        mMainActivity = (MainActivity) getActivity();
        initView();
        //获取轮播图的数据
        getHttpMethod("http://api.dev.ilexnet.com/simulate/38eye/article-api/banner-images", mlunboOnResponseListener);
        //获取四大类的数据
        getHttpMethod("http://38eye.test.ilexnet.com/api/mobile/home-category/list", mhomecategoryOnResponseListener);
        //初始化轮播图并实现
//        mHomeLunboView = new HomeLunboView(mMainActivity,mViewPager);
//        mHomeLunboView.startLubo();
        //初始化recycleview并实现
        mHomeRecycleView = new HomeRecycleView(mMainActivity, mRecyclerView);
        mHomeRecycleView.startItem();
        //计算屏幕的尺寸
        caculate();
        //初始化spinner并实现
        mHomeSpinnerView = new HomeSpinnerView(mMainActivity, mSpinner, height);
        mHomeSpinnerView.startspinner();
        setonClick();
        return view;
    }

    private void getHttpMethod(String url, OnResponseListener mOnResponseListener) {
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        mRequestQueue.add(mWhat, request, mOnResponseListener);

    }

    //轮播图的OnResponseListener
    private OnResponseListener<String> mlunboOnResponseListener = new OnResponseListener<String>() {
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
                    Home_ad_adapter myAdapter = new Home_ad_adapter(mViewList);
                    mViewPager.setAdapter(myAdapter);
                    //  mViewPager.setCurrentItem(mCurrentItem);
                    Message message = new Message();
                    message.what = FINSH;
                    handler.sendMessage(message);
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

    private OnResponseListener<String> mhomecategoryOnResponseListener = new OnResponseListener<String>() {
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
                    JSONArray homecategory = object.getJSONArray("data");
                    mHomeFourSortsList = new ArrayList<>();
                    for (int i = 0; i < homecategory.length(); i++) {
                        JSONObject jsonObject = homecategory.getJSONObject(i);
                        HomeFourSort homeFourSort = new HomeFourSort();
                        homeFourSort.setCategory_name(jsonObject.getString("category_name"));
                        homeFourSort.setCategory_image(jsonObject.getString("category_image"));
                        mHomeFourSortsList.add(homeFourSort);
                        Log.e("javabean", homeFourSort.getCategory_name() + homeFourSort.getCategory_image());
                    }

                    Glide.with(mMainActivity).load(mHomeFourSortsList.get(0).getCategory_image()).into(home_sort1image);
                    home_sort1text.setText(mHomeFourSortsList.get(0).getCategory_name());
                    Glide.with(mMainActivity).load(mHomeFourSortsList.get(1).getCategory_image()).into(home_sort2image);
                    home_sort2text.setText(mHomeFourSortsList.get(1).getCategory_name());
                    Glide.with(mMainActivity).load(mHomeFourSortsList.get(2).getCategory_image()).into(home_sort3image);
                    home_sort3text.setText(mHomeFourSortsList.get(2).getCategory_name());
                    Glide.with(mMainActivity).load(mHomeFourSortsList.get(3).getCategory_image()).into(home_sort4image);
                    home_sort4text.setText(mHomeFourSortsList.get(3).getCategory_name());

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

    //轮播图的listener
    private void setLinstener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //手动滑到这个广告的时候,发送改位置的值
                mHandler.sendMessage(Message.obtain(mHandler, IMAGE_CHANGED, position, 0));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //获取数据的handler
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
                    setLinstener();
                    Message message = new Message();
                    message.what = 520;
            }
        }
    };

    //轮播图的handler
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int action = msg.what;
            if (mHandler.hasMessages(IMAGE_UPDATE)) {
                mHandler.removeMessages(IMAGE_UPDATE);
            }
            switch (action) {


                case IMAGE_UPDATE:
                    //轮播图经行更新
                    mCurrentItem += 1;
                    mViewPager.setCurrentItem(mCurrentItem);
                    mHandler.sendEmptyMessageDelayed(IMAGE_UPDATE, REFRESHTIME);
                    break;
                case IMAGE_CHANGED:
                    //手动滑了广告
                    mCurrentItem = msg.arg1;
                    mViewPager.setCurrentItem(mCurrentItem);
                    mHandler.sendEmptyMessageDelayed(IMAGE_UPDATE, REFRESHTIME);
                    break;

            }
        }
    };


    //更多的单击事件
    private void setonClick() {
        home_xptjgengduo.setOnClickListener(this);
        home_yzcpgengduo.setOnClickListener(this);
    }

    //初始化视图
    private void initView() {
        mViewPager = (ViewPager) view.findViewById(R.id.main_ad_show);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        mSpinner = (Spinner) view.findViewById(R.id.home_spinner);
        mImageView = (ImageView) view.findViewById(R.id.home_jisuan);
        home_xptjgengduo = (ImageView) view.findViewById(R.id.home_xptjgengduo);
        home_yzcpgengduo = (ImageView) view.findViewById(R.id.home_yzcpgengduo);
        home_sort1image = (ImageView) view.findViewById(R.id.home_sort1image);
        home_sort1text = (TextView) view.findViewById(R.id.home_sort1text);
        home_sort2image = (ImageView) view.findViewById(R.id.home_sort2image);
        home_sort2text = (TextView) view.findViewById(R.id.home_sort2text);
        home_sort3image = (ImageView) view.findViewById(R.id.home_sort3image);
        home_sort3text = (TextView) view.findViewById(R.id.home_sort3text);
        home_sort4image = (ImageView) view.findViewById(R.id.home_sort4image);
        home_sort4text = (TextView) view.findViewById(R.id.home_sort4text);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void caculate() {
        WindowManager manager = (WindowManager) mMainActivity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        int width2 = dm.widthPixels;
        height = dm.heightPixels;
        Toast.makeText(mMainActivity, "height:" + height, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int buttonid = v.getId();
        switch (buttonid) {
            case R.id.home_xptjgengduo:
                Intent intent = new Intent(mMainActivity, HomexptjActivity.class);
                startActivity(intent);
                break;
            case R.id.home_yzcpgengduo:
                Intent intent1 = new Intent(mMainActivity, EventActivity.class);
                startActivity(intent1);
                break;

        }
    }
}