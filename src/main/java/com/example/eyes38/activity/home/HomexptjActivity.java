package com.example.eyes38.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.adapter.Home_ContentAdapter;
import com.example.eyes38.beans.HomeContent;
import com.example.eyes38.beans.HomeContentContent;
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

public class HomexptjActivity extends AppCompatActivity {
    public static final int FINSHED = 1;
    RecyclerView mRecyclerView;
    List<HomeContent> mList;
    List<HomeContentContent> mHomeContentContents;
    Home_ContentAdapter hcAdapter;
    HomeContent hc;
    //下拉刷新控件

    //测试获取json数据
    //创建 请求队列成员变量
    private RequestQueue mRequestQueue;
    private final static int mWhat = 520;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homexptj);
        initView();
        intiData();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.homecontent);
    }

    private void intiData() {
        mList = new ArrayList<>();
        mHomeContentContents = new ArrayList<>();
        HomeContentContent homeContentContent1 = new HomeContentContent("http://hz-ifs.ilexnet.com/eyes38/599334_1_pic500_120.jpg", "第一", 2.2, "个");
        mHomeContentContents.add(homeContentContent1);
        HomeContent homeContent1 = new HomeContent("yizhou", mHomeContentContents);
        //  mList.add(homeContent1);
//        mList.add(homeContent1);
//        mList.add(homeContent1);
        // handler.sendEmptyMessage(FINSHED);
        getHttpMethod("http://38eye.test.ilexnet.com/api/mobile/special-product/listConfig", mOnResponseListener);
    }

    private void getHttpMethod(String url, OnResponseListener mOnResponseListener) {
        mRequestQueue = NoHttp.newRequestQueue();
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
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String zhuantiname = jsonObject.getString("name");
                        int is_app_show = jsonObject.getInt("is_app_show");
                        if (is_app_show == 0) {
                            //break;
                        } else {
                            //初始化mmlist
                            List<HomeContentContent> mmList = new ArrayList<>();
                            JSONArray array2 = jsonObject.getJSONArray("products");

                            Log.e("arrr22222", array2.length() + "");
                            for (int j = 0; j < array2.length(); j++) {
                                if (mmList.size() < 4) {
                                    JSONObject jsonObject1 = array2.getJSONObject(j);
                                    Log.e("循环外面", "dasdaafasf");
                                    if (!jsonObject1.getString("product").equals("false")) {
                                        Log.e("循环里面", jsonObject1.toString());
                                        JSONObject jsonObject2 = jsonObject1.getJSONObject("product");
                                        Log.e("内层循环", jsonObject2.toString());
                                        String image = jsonObject2.getString("image");
                                        String name = jsonObject2.getString("name");
                                        Double price = jsonObject2.getDouble("price");
                                        String extension4 = jsonObject2.getString("extension4");
                                        Log.e("专题", image + ":" + name);
                                        HomeContentContent hcc = new HomeContentContent(image, name, price, extension4);
                                        mmList.add(hcc);
                                    }
                                }
                            }
                            hc = new HomeContent(zhuantiname, mmList);
                            mList.add(hc);
                        }
                    }
                    handler.sendEmptyMessage(FINSHED);
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

    //获取数据的handler
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FINSHED:
                    Log.e("handler", "hhhhhhh");
                    //初始化适配器
                    initAdapter();
                    //这是监听
                    //   setLinstener();
                    Message message = new Message();
                    message.what = 520;
            }
        }
    };

    private void initAdapter() {
        hcAdapter = new Home_ContentAdapter(this, mList);
        mRecyclerView.setAdapter(hcAdapter);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
//        mRecyclerView.setLayoutManager(gridLayoutManager);
        LinearLayoutManager linear = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linear);
    }


    //返回键的单击事件
    public void xptj_back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
