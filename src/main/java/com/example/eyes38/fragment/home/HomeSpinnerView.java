package com.example.eyes38.fragment.home;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.beans.Home_district;
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
 * Created by huangjiechun on 16/5/24.
 */
public class HomeSpinnerView implements AdapterView.OnItemSelectedListener {
    Spinner mSpinner;
    List<String> mList;
    List<Home_district> mCity;
    ArrayAdapter<String> apapter;
    MainActivity mMainActivity;
    int height;
    private RequestQueue mRequestQueue;
    private final static int mWhat = 123;
    public static final int FINSH = 1;

    public HomeSpinnerView(MainActivity activity, Spinner spinner, int height) {
        this.mMainActivity = activity;
        this.mSpinner = spinner;
        this.height = height;
    }

    public void startspinner() {
        initData();
    }

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
                    initListener();
            }
        }
    };

    private void initListener() {
        mSpinner.setOnItemSelectedListener(this);
    }

    private void initAdapter() {
        apapter = new ArrayAdapter<String>(mMainActivity, R.layout.home_spinner, mList);
        // 3:adapter设置一个下拉列表样式
        apapter.setDropDownViewResource(R.layout.home_spinner_item);
        mSpinner.setDropDownHorizontalOffset(200);
        mSpinner.setDropDownVerticalOffset(height);
        //  4:spinner加载适配器
        mSpinner.setAdapter(apapter);
    }

    private void initData() {
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/sell-district/list";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
       // request.setRequestFailedReadCache(true);
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
                    //解析第一层
                    JSONObject object = new JSONObject(result);
                    JSONArray homefirst = object.getJSONArray("data");
                    mList = new ArrayList<String>();
                    mCity = new ArrayList<Home_district>();
                    for (int i = 0; i < homefirst.length(); i++) {
                        JSONObject jsonObject = homefirst.getJSONObject(i);
                        //mCartGoods.setPath(jsonObject.getString("create_date"));
                        JSONObject mtest = jsonObject.getJSONObject("district");
                        String name = mtest.getString("name");
                       int district_id = mtest.getInt("district_id");
                        Home_district home_district = new Home_district(name,district_id);
                        mCity.add(home_district);
                        mList.add(name);
                        Log.e("获取的数据哈达和", jsonObject.getString("create_date") + " " + name+district_id);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}