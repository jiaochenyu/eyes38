package com.example.eyes38.user_activity.AddressInfo;

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
     private  int num1,num2;
    //定义三个spinner适配器
    List<AreaBean> mList;
    List<Integer> districtList,parentList;
    List<String> proList,cityList,areaList;
    private RequestQueue mRequestQueue,mRequestQueue2,mRequestQueue3;
    ArrayAdapter<String> proAdapter,cityAdapter,areaAdapter;
    //Handler
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case mFinish:
                    //加载数据完成
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
    }




    private void initViews() {
        province = (Spinner) findViewById(R.id.province);
        city = (Spinner) findViewById(R.id.city);
        area = (Spinner) findViewById(R.id.area);
        mList=new ArrayList<>();
        proList=new ArrayList<>();
        cityList=new ArrayList<>();
        areaList=new ArrayList<>();
        districtList=new ArrayList<>();
        parentList=new ArrayList<>();
        cityAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, cityList);
        proAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, proList);
       areaAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, areaList);
    }
    private void bindData() {
        //首先呢  获取province数据
        httpMethod1();
    }
    //获取province数据
    private void httpMethod1() {
        mRequestQueue= NoHttp.newRequestQueue();
        String url="http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
        Request<String> request=NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("parent_id", "0");
        mRequestQueue.add(mWHAT,request,mOnResponseListener);
    }

    /**
     * 请求http结果 回调对象 接受请求结果
     */
    private OnResponseListener<String> mOnResponseListener=new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what==mWHAT){
                //请求成功
                String result=response.get();
                try {
                    JSONObject object=new JSONObject(result);
                    JSONArray data=object.getJSONArray("data");
                    for (int i=0;i<data.length();i++){
                        JSONObject object2=data.getJSONObject(i);
                       String name=object2.getString("name");
                        int district_id= Integer.parseInt(object2.getString("district_id"));
                        int parent_id= Integer.parseInt(object2.getString("parent_id"));
                        proList.add(name);//省数据
                        districtList.add(district_id);
                        parentList.add(parent_id);
                        AreaBean areaBean=new AreaBean();
                        areaBean.setDistrict_id(district_id);
                        areaBean.setName(name);
                        areaBean.setParent_id(parent_id);
                        mList.add(areaBean);
                    }
                    province.setAdapter(proAdapter);
                    Message message=new Message();
                    message.what=mFinish;
                    Log.e("请求完成","11");
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
    private void initListener1() {
       province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               num1=districtList.get(position);
               Log.e("num1",num1+"");
               httpMethod2();//费尽心机获取市级数据
               initListener2();//第二层市级
           }
           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

    }
    //监听city spinner事件
    private void initListener2() {
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                num2=districtList.get(position);
                Log.e("num2",num2+"");
                httpMethod3();//费尽心机获取市级数据
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //第三层数据获取
    private void httpMethod3() {
        mRequestQueue3= NoHttp.newRequestQueue();
        String url="http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
        Request<String> request=NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("parent_id", num2);
        mRequestQueue3.add(mWHAT3,request,mOnResponseListener3);
    }
    /**
     * 请求http结果 回调对象 接受请求结果
     */
    private OnResponseListener<String> mOnResponseListener3= new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {

        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }

        @Override
        public void onFinish(int what) {

        }
    };
    private void httpMethod2() {
        mRequestQueue2= NoHttp.newRequestQueue();
        String url="http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
        Request<String> request=NoHttp.createStringRequest(url, RequestMethod.GET);
        Log.e("num1",num1+"传入的");
        request.add("parent_id", num1);
        mRequestQueue2.add(mWHAT2,request,mOnResponseListener2);
    }
    private  OnResponseListener<String> mOnResponseListener2=new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }
        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what==mWHAT2){
                //请求成功
                String result=response.get();
                try {
                    cityList.clear();
                    Log.e("cityList11",cityList.size()+"-->"+districtList.size()+"-->"+parentList.size()) ;
                    JSONObject object=new JSONObject(result);
                    JSONArray data=object.getJSONArray("data");
                    for (int i=0;i<data.length();i++){
                        JSONObject object2=data.getJSONObject(i);
                        String name=object2.getString("name");
                        int district_id= Integer.parseInt(object2.getString("district_id"));
                        int parent_id= Integer.parseInt(object2.getString("parent_id"));
                        cityList.add(name);
                        districtList.add(district_id);
                        parentList.add(parent_id);
                    }
                    for (int i=0;i<cityList.size();i++){
                       Log.e("cityList",cityList.get(i)+"") ;
                    }
                    city.setAdapter(cityAdapter);
                    Message message=new Message();
                    message.what= mFinish2;
                    Log.e("请求完成","22");
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
