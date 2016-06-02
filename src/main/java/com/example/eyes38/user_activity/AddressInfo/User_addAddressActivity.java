package com.example.eyes38.user_activity.AddressInfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User_addAddressActivity extends AppCompatActivity {
    private static final int mWHAT = 561;
    private static final int mWHAT2 = 564;
    private static final int mWHAT3 = 566;
    private static final int mFinish = 562;
    private static final int mFinish2 = 563;
    private static final int mFinish3 = 565;
    private boolean true1, true2, true3, true4, onlyTrue;
    private boolean showFirst;//只用于提示手机号码一次
    private ImageView address_adjust1, address_adjust2, address_adjust3, address_adjust4;//四个用于判断的图片
    private Toast mToast;
    private boolean flag, flag1, flag2;
    private Button address_button;
    private EditText address_name, address_tel, address_plot, address_detail;//控件
    private Spinner province, city, area;
    private int num1, num2;//用于url的传值
    private LinearLayout city_linear, area_linear;
    //定义三个spinner适配器
    List<Integer> districtList, parentList, districtList2;//用于url的取值
    List<String> proList, cityList, areaList;//适配数据
    private RequestQueue mRequestQueue;//请求队列
    ArrayAdapter<String> proAdapter, cityAdapter, areaAdapter;//适配器
    //Handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case mFinish:
                    initListener1();
                    //加载数据完成
                    break;
                case mFinish2:
                    //第二层
                    initListener2();
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
        initListener();//用于控件的监听
        initListener1();//监听省事件
        initListener2();//监听市级事件
    }

    private void ButtonListener() {
        while (true1 && true2 && true3 && true4){
            address_button.setBackgroundResource(R.color.topical);
            address_button.setEnabled(true);
            address_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    //在此处传值
            }
            });
            break;
        }
    }

    private void initListener() {
        //收货人监听事件
        address_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (address_name.length() == 0) {
                    address_adjust1.setVisibility(View.VISIBLE);//默认设置不合法
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (address_name.length() == 16) {
                    show("无法输入，超出字数限制！");
                } else if (address_name.length() == 0) {
                    address_adjust1.setImageResource(R.drawable.invalid);
                } else {
                    address_adjust1.setImageResource(R.drawable.valid);
                    true1 = true;
                }
            }
        });
        //对电话号码输入框进行监听
        address_tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (address_tel.length() == 0) {
                    address_adjust2.setVisibility(View.VISIBLE);//默认设置不合法
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = address_tel.getText().toString();
                Pattern pattern = Pattern.compile("[0-9]*");
                Matcher matcher = pattern.matcher(text);
                if (address_tel.length() != 11 || !matcher.matches()) {
                    if (!showFirst) {
                        showFirst = true;
                    }
                    address_adjust2.setImageResource(R.drawable.invalid);
                } else if (address_tel.length() == 0) {
                    address_adjust2.setImageResource(R.drawable.invalid);
                } else if (address_tel.length() == 11) {
                    address_adjust2.setImageResource(R.drawable.valid);
                    true2 = true;
                }
            }
        });
        //用于小区EditText事件的监听
        address_plot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (address_plot.length() == 0) {
                    address_adjust3.setVisibility(View.VISIBLE);//默认设置不合法
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (address_plot.length() == 0) {
                    address_adjust3.setImageResource(R.drawable.invalid);
                } else {
                    address_adjust3.setImageResource(R.drawable.valid);
                    true3 = true;
                }
            }
        });
        //用于详细地址EditText事件的监听
        address_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (address_detail.length() == 0) {
                    address_adjust4.setVisibility(View.VISIBLE);//默认设置不合法
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (address_detail.length() == 0) {
                    address_adjust4.setImageResource(R.drawable.invalid);
                } else {
                    address_adjust4.setImageResource(R.drawable.valid);
                    true4 = true;
                    ButtonListener();//按钮的监听
                }
            }
        });

    }
    private void initViews() {
        address_button = (Button) findViewById(R.id.address_button);
        address_adjust1 = (ImageView) findViewById(R.id.address_adjust1);
        address_adjust2 = (ImageView) findViewById(R.id.address_adjust2);
        address_adjust3 = (ImageView) findViewById(R.id.address_adjust3);
        address_adjust4 = (ImageView) findViewById(R.id.address_adjust4);
        city_linear = (LinearLayout) findViewById(R.id.city_linear);
        area_linear = (LinearLayout) findViewById(R.id.area_linear);
        address_name = (EditText) findViewById(R.id.address_name);
        address_tel = (EditText) findViewById(R.id.address_tel);
        address_plot = (EditText) findViewById(R.id.address_plot);
        address_detail = (EditText) findViewById(R.id.address_detail);
        province = (Spinner) findViewById(R.id.province);
        city = (Spinner) findViewById(R.id.city);
        area = (Spinner) findViewById(R.id.area);
        proList = new ArrayList<>();
        cityList = new ArrayList<>();
        areaList = new ArrayList<>();
        districtList = new ArrayList<>();
        districtList2 = new ArrayList<>();
        parentList = new ArrayList<>();
        //初始化spinnner
        proAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, proList);
        proAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaList);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void bindData() {
        //首先呢  获取province数据
        httpMethod();
    }

    //获取province数据
    private void httpMethod() {
        if (!flag) {
            //获取省数据
            mRequestQueue = NoHttp.newRequestQueue();
            String url1 = "http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
            Request<String> request1 = NoHttp.createStringRequest(url1, RequestMethod.GET);
            request1.add("parent_id", "0");
            mRequestQueue.add(mWHAT, request1, mOnResponseListener);
            flag = true;
        }
        if (flag1) {
            //获取市数据
            String url2 = "http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
            Request<String> request2 = NoHttp.createStringRequest(url2, RequestMethod.GET);
            Log.e("num1", num1 + "传入的");
            request2.add("parent_id", num1);
            mRequestQueue.add(mWHAT2, request2, mOnResponseListener);
            flag1 = false;
        }
        if (flag2) {
            //获取区数据
            areaList.clear();
            String url3 = "http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
            Request<String> request3 = NoHttp.createStringRequest(url3, RequestMethod.GET);
            Log.e("num2", num2 + "传入的");
            request3.add("parent_id", num2);
            mRequestQueue.add(mWHAT3, request3, mOnResponseListener);
            flag2 = false;
        }

    }

    /**
     * 请求http结果 回调对象 接受请求结果,用于解析数据
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
                    mHandler.sendMessage(message);
                    Log.e("请求完成", "11");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //
            else if (what == mWHAT2) {
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
                    if (cityList.size() != 0) {
                        city_linear.setVisibility(View.VISIBLE);
                        city.setAdapter(cityAdapter);
                    } else {
                        city_linear.setVisibility(View.GONE);

                    }
                    Message message = new Message();
                    message.what = mFinish2;
                    Log.e("请求完成", "22");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (what == mWHAT3) {
                //请求成功
                String result = response.get();
                try {
                    areaList.clear();
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
                    if (areaList.size() != 0) {
                        area_linear.setVisibility(View.VISIBLE);
                        area.setAdapter(areaAdapter);
                    } else {
                        area_linear.setVisibility(View.GONE);
                    }
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

    //用于监听省级
    private void initListener1() {
        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                num1 = districtList.get(position);
                Log.e("num1", num1 + "");
                flag1 = true;
                area_linear.setVisibility(View.GONE);
                httpMethod();//费尽心机获取市级数据

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //用于监听市级单击事件
    private void initListener2() {
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                num2 = districtList2.get(position);
                Log.e("num2", num2 + "");
                flag2 = true;
                httpMethod();//费尽心机获取市级数据
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void show(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(User_addAddressActivity.this, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

}
