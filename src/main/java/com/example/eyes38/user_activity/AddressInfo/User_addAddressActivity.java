package com.example.eyes38.user_activity.AddressInfo;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
import java.util.List;

public class User_addAddressActivity extends AppCompatActivity {
    public static final int mWHAT = 561;
    public static final int mFinish = 562;
    public static final int mFinish2 = 563;
    public static final int mWHAT2 = 564;
    public static final int mFinish3 = 565;
    public static final int mWHAT3 = 566;
    private Spinner province, city, area;
    private int num1, num2;
    //定义三个spinner适配器
    List<Integer> districtList, parentList, districtList2;
    List<String> proList, cityList, areaList;
    private RequestQueue mRequestQueue, mRequestQueue2, mRequestQueue3;
    ArrayAdapter<String> proAdapter, cityAdapter, areaAdapter;
    //Handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case mFinish:
                    //加载数据完成
                    break;
                case mFinish2:
                    //第二层
                    break;
                case mFinish3:
                    //第三层
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_address);
        initViews();
        bindData();//绑定数据
        initListener1();//监听省事件
        initListener2();//监听市级事件
    }


    private void initViews() {
        province = (Spinner) findViewById(R.id.province);
        city = (Spinner) findViewById(R.id.city);
        area = (Spinner) findViewById(R.id.area);
        proList = new ArrayList<>();
        cityList = new ArrayList<>();
        areaList = new ArrayList<>();
        districtList = new ArrayList<>();
        districtList2 = new ArrayList<>();
        parentList = new ArrayList<>();
        proAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, proList);
        proAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaList);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void bindData() {
        //首先呢  获取province数据
        httpMethod1();
    }

    //获取province数据
    private void httpMethod1() {
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("parent_id", "0");
        mRequestQueue.add(mWHAT, request, mOnResponseListener);
    }

    /**
     * 请求http结果 回调对象 接受请求结果,用于解析第一层数据
     */
    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWHAT) {
                //请求成功
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray data = object.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object2 = data.getJSONObject(i);
                        String name = object2.getString("name");
                        int district_id = Integer.parseInt(object2.getString("district_id"));
                        int parent_id = Integer.parseInt(object2.getString("parent_id"));
                        proList.add(name);//省数据List已经获取
                        districtList.add(district_id);
                        parentList.add(parent_id);

                    }
                    province.setAdapter(proAdapter);
                    Message message = new Message();
                    message.what = mFinish;
                    Log.e("请求完成", "11");
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
    //用于监听省级
    private void initListener1() {
        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                num1 = districtList.get(position);
                Log.e("num1", num1 + "");
                httpMethod2();//费尽心机获取市级数据
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void httpMethod2() {
        mRequestQueue2 = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        Log.e("num1", num1 + "传入的");
        request.add("parent_id", num1);
        mRequestQueue2.add(mWHAT2, request, mOnResponseListener2);
    }

    private OnResponseListener<String> mOnResponseListener2 = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWHAT2) {
                //请求成功
                String result = response.get();
                try {
                    cityList.clear();
                    districtList2.clear();
                    Log.e("cityList", cityList.size() + "");
                    JSONObject object = new JSONObject(result);
                    JSONArray data = object.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object2 = data.getJSONObject(i);
                        String name = object2.getString("name");
                        int district_id = Integer.parseInt(object2.getString("district_id"));
                        int parent_id = Integer.parseInt(object2.getString("parent_id"));
                        cityList.add(name);//第二级市数据
                        districtList2.add(district_id);
                        parentList.add(parent_id);
                    }
                    city.setAdapter(cityAdapter);
                    Message message = new Message();
                    message.what = mFinish2;
                    Log.e("请求完成", "22");
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

    //用于监听市级单击事件
    private void initListener2() {
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                num2 = districtList2.get(position);
                Log.e("num2", num2 + "");
                httpMethod3();//费尽心机获取市级数据
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //根据上层提供的url数据 获取第三层数据
    private void httpMethod3() {
        areaList.clear();
        mRequestQueue3 = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        Log.e("num2", num2 + "传入的");
        request.add("parent_id", num2);
        mRequestQueue3.add(mWHAT3, request, mOnResponseListener3);
    }

    //解析县级数据
    private OnResponseListener<String> mOnResponseListener3 = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWHAT3) {
                //请求成功
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray data = object.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object2 = data.getJSONObject(i);
                        String name = object2.getString("name");
                        int district_id = Integer.parseInt(object2.getString("district_id"));
                        int parent_id = Integer.parseInt(object2.getString("parent_id"));
                        areaList.add(name);//第三级县数据
                        districtList2.add(district_id);
                        parentList.add(parent_id);
                    }
                    Log.e("areaList", areaList.size() + "");
                    area.setAdapter(areaAdapter);
                    Message message = new Message();
                    message.what = mFinish3;
                    Log.e("请求完成", "33");
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
