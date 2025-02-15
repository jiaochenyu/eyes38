package com.example.eyes38.user_activity.AddressInfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.eyes38.R;
import com.example.eyes38.beans.ReceiptAddress;
import com.example.eyes38.user_activity.User_take_addressActivity;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User_modifyAddressActivity extends AppCompatActivity {
    private static final int mWHAT = 561;
    private static final int mWHAT2 = 564;
    private static final int mWHAT3 = 566;
    private static final int mWHAT4 = 567;
    private static final int mWHAT5 = 569;
    private static final int mWHAT6 = 570;
    private static final int mFinish = 562;
    private static final int mFinish2 = 563;
    private static final int mFinish3 = 565;
    private static final int mFinish4 = 568;
    private boolean true1, true2, true3, true4; //分别代表收货人信息,电话号码,区级地区,详细地址,
    private boolean showFirst;//只用于提示手机号码一次
    private ImageView address_adjust1, address_adjust2, address_adjust4;//四个用于判断的图片
    private Toast mToast;
    private boolean flag, flag1, flag2, flag3, flag4, flag5;
    private Button address_button;
    private EditText address_name, address_tel, address_detail;//控件
    private Spinner province, city, area, plot;
    private int num1, num2, num3;//用于url的传值
    private int country_id;//用于取到country_id
    private LinearLayout city_linear, area_linear, plot_linear;
    private SharedPreferences sp;//用于去偏好设置里取customer_id
    private String customer_id;//保存取到的customer_id
    private String newHeader;//保存用户的头信息
    //定义四个spinner适配器
    List<Integer> districtList, parentList, districtList2, districtList3, countryList;//用于url的取值
    List<String> proList, cityList, areaList, plotList;//适配数据
    private RequestQueue mRequestQueue;//请求队列
    ArrayAdapter<String> proAdapter, cityAdapter, areaAdapter, plotAdapter;//适配器
    private ReceiptAddress mReceipt;//收货地址的javabean
    private ReceiptAddress mReceiptAddress;//接收传递过来的收货地址javabean
    String[] tmp;//保存spinner的值
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
                    initListener3();
                    break;
                case mFinish4:
                    //第四层
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_modify_address);
        Intent intent = getIntent();
        mReceiptAddress = (ReceiptAddress) intent.getSerializableExtra("modifyvalues");

        //取出偏好设置里面的信息
        gethead();
        initViews();
        bindData();//绑定数据
        initListener();//用于控件的监听
        ButtonListener();//监听按键单击事件
        initListener1();//监听省事件
        initListener2();//监听市级事件
        initListener3();//监听区级事件
        initListener4();//监听小区事件
    }

    private void ButtonListener() {
        address_button.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  //获取所有控件中得内容,存到mReceipt中,用来传值
                                                  mReceipt.setCustomer_id(customer_id);
                                                  mReceipt.setFirstname(address_name.getText().toString());
                                                  mReceipt.setMobile(address_tel.getText().toString());
                                                  mReceipt.setAddress_1(address_detail.getText().toString());
                                                  //取到了所有需要的参数,现在用增加收货地址的接口保存收货地址
                                                  if (!true1) {
                                                      show("收货人不合法");
                                                  } else if (!true2) {
                                                      show("电话号码不合法");
                                                  } else if (!true3) {
                                                      show("地区选择不合法");
                                                  } else if (!true4) {
                                                      show("详细地址为空!");
                                                  } else {
                                                      flag4 = true;
                                                      httpMethod();
                                                      //跳转到前一个页面
                                                      Intent intent = new Intent(User_modifyAddressActivity.this, User_take_addressActivity.class);
                                                      startActivity(intent);
                                                      finish();
                                                  }
                                              }
                                          }
        );
    }

    private void initListener() {
        ButtonListener();//监听按键单击事件
        //收货人监听事件
        address_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (address_name.length() == 0) {

                    //address_adjust1.setVisibility(View.VISIBLE);//默认设置不合法
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
                    address_adjust1.setImageResource(R.mipmap.invalid);
                    true1 = false;
                } else {
                    address_adjust1.setImageResource(R.mipmap.valid);
                    true1 = true;
                }
            }
        });
        //对电话号码输入框进行监听
        address_tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (address_tel.length() == 0) {
                    //address_adjust2.setVisibility(View.VISIBLE);//默认设置不合法
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
                    address_adjust2.setImageResource(R.mipmap.invalid);
                    true2 = false;
                } else if (address_tel.length() == 0) {
                    address_adjust2.setImageResource(R.mipmap.invalid);
                    true2 = false;
                } else if (address_tel.length() == 11) {
                    if (isMobile(text)) {
                        address_adjust2.setImageResource(R.mipmap.valid);
                        true2 = true;
                        ButtonListener();
                    } else {
                        address_adjust2.setImageResource(R.mipmap.invalid);
                        true2 = false;
                        ButtonListener();
                    }
                }
            }
        });
        //用于详细地址EditText事件的监听
        address_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (address_detail.length() == 0) {
                    //address_adjust4.setVisibility(View.VISIBLE);//默认设置不合法
                    true4 = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (address_detail.length() == 0) {
                    address_adjust4.setImageResource(R.mipmap.invalid);
                    true4 = false;
                } else {
                    address_adjust4.setImageResource(R.mipmap.valid);
                    true4 = true;
                    ButtonListener();//按钮的监听
                }
            }
        });

    }

    //判断电话号码是否合法
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    private void initViews() {
        //开始传过来的信息都是正确的
        true1 = true;
        true2 = true;
        true3 = true;
        true4 = true;
        address_button = (Button) findViewById(R.id.modify_address_button);
        address_adjust1 = (ImageView) findViewById(R.id.modify_address_adjust1);
        address_adjust2 = (ImageView) findViewById(R.id.modify_address_adjust2);
        address_adjust4 = (ImageView) findViewById(R.id.modify_address_adjust4);
        address_adjust1.setImageResource(R.mipmap.valid);
        address_adjust2.setImageResource(R.mipmap.valid);
        address_adjust4.setImageResource(R.mipmap.valid);
        city_linear = (LinearLayout) findViewById(R.id.modify_city_linear);
        area_linear = (LinearLayout) findViewById(R.id.modify_area_linear);
        plot_linear = (LinearLayout) findViewById(R.id.modify_plot_linear);
        address_name = (EditText) findViewById(R.id.modify_address_name);//收货人姓名
        address_tel = (EditText) findViewById(R.id.modify_address_tel);//收货人电话
        address_detail = (EditText) findViewById(R.id.modify_address_detail);//详细收货地址
        province = (Spinner) findViewById(R.id.modify_province);
        city = (Spinner) findViewById(R.id.modify_city);
        area = (Spinner) findViewById(R.id.modify_area);
        plot = (Spinner) findViewById(R.id.modify_polt);
        proList = new ArrayList<>();
        cityList = new ArrayList<>();
        areaList = new ArrayList<>();
        plotList = new ArrayList<>();
        districtList = new ArrayList<>();
        districtList2 = new ArrayList<>();
        districtList3 = new ArrayList<>();
        parentList = new ArrayList<>();
        countryList = new ArrayList<>();
        mReceipt = new ReceiptAddress();
        //初始化spinnner
        proAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, proList);
        proAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaList);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plotAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, plotList);
        plotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        initData();//设置各个控件的默认值
    }

    private void initData() {
        address_name.setText(mReceiptAddress.getFirstname());
        address_tel.setText(mReceiptAddress.getMobile());
        address_detail.setText(mReceiptAddress.getAddress_1());
        mReceipt.setZone_id(mReceiptAddress.getZone_id());
        String district = mReceiptAddress.getDistrict();
        tmp = district.split(" ");
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
            request1.setCacheMode(CacheMode.DEFAULT);
            mRequestQueue.add(mWHAT, request1, mOnResponseListener);
            flag = true;
        }
        if (flag1) {
            //获取市数据
            String url2 = "http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
            Request<String> request2 = NoHttp.createStringRequest(url2, RequestMethod.GET);
            request2.add("parent_id", num1);
            request2.setCacheMode(CacheMode.DEFAULT);
            mRequestQueue.add(mWHAT2, request2, mOnResponseListener);
            flag1 = false;
        }
        if (flag2) {
            //获取区数据
            areaList.clear();
            String url3 = "http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
            Request<String> request3 = NoHttp.createStringRequest(url3, RequestMethod.GET);
            request3.add("parent_id", num2);
            request3.setCacheMode(CacheMode.DEFAULT);
            mRequestQueue.add(mWHAT3, request3, mOnResponseListener);
            flag2 = false;
        }
        if (flag3) {
            //获取小区数据
            plotList.clear();
            String url4 = "http://38eye.test.ilexnet.com/api/mobile/community/list";
            Request<String> request4 = NoHttp.createStringRequest(url4, RequestMethod.GET);
            request4.add("district_id", num3);
            request4.setCacheMode(CacheMode.DEFAULT);
            mRequestQueue.add(mWHAT4, request4, mOnResponseListener);
            flag3 = false;
        }
        if (flag4) {
            //http://38eye.test.ilexnet.com/api/mobile/address-to-community/detail/105
            //http://38eye.test.ilexnet.com/api/mobile/customer-api/customer-addresses/105修改收货地址
            //http://38eye.test.ilexnet.com/api/mobile/address-to-community/detail/106
            //http://38eye.test.ilexnet.com/api/mobile/address-to-community/save这个接口用来保存收货箱
            //修改收货地址
            String url5 = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customer-addresses/" + mReceiptAddress.getAddress_id();
            Request<String> request5 = NoHttp.createStringRequest(url5, RequestMethod.POST);
            //增加头信息
            String header = "Basic " + newHeader;
            request5.addHeader("Authorization", header);
            //增加post请求中得body
            request5.add("address_1", mReceipt.getAddress_1());
            request5.add("country_id", mReceipt.getCountry_id());
            request5.add("customer_id", mReceipt.getCustomer_id());
            request5.add("district_id", mReceipt.getDistrict_id());
            request5.add("firstname", mReceipt.getFirstname());
            request5.add("mobile", mReceipt.getMobile());
            request5.add("zone_id", mReceipt.getZone_id());
            request5.setCacheMode(CacheMode.DEFAULT);
            mRequestQueue.add(mWHAT5, request5, mOnResponseListener);
            flag4 = false;
        }
        if (flag5) {
            String url6 = "http://38eye.test.ilexnet.com/api/mobile/address-to-community/save";
            Request<String> request6 = NoHttp.createStringRequest(url6, RequestMethod.POST);
            //增加头信息
            String header = "Basic " + newHeader;
            request6.addHeader("Authorization", header);
            //增加post请求中得body
            request6.add("address_id", mReceipt.getAddress_id());
            request6.add("community_id", 1);
            mRequestQueue.add(mWHAT6, request6, mOnResponseListener);
            flag5 = false;
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
                        int country_id = Integer.parseInt(object2.getString("country_id"));
                        proList.add(name);//省数据List已经获取
                        districtList.add(district_id);
                        countryList.add(country_id);
                        parentList.add(parent_id);
                    }
                    province.setAdapter(proAdapter);
                    setSpinnerItemSelectedByValue(province, tmp[0]);//设置spinner的默认值
                    Message message = new Message();
                    message.what = mFinish;
                    mHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (what == mWHAT2) {
                //请求成功
                String result = response.get();
                try {
                    cityList.clear();
                    districtList2.clear();
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
                    setSpinnerItemSelectedByValue(city, tmp[1]);//设置spinner的默认值
                    Message message = new Message();
                    message.what = mFinish2;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (what == mWHAT3) {
                //请求成功
                String result = response.get();
                try {
                    areaList.clear();
                    districtList3.clear();
                    JSONObject object = new JSONObject(result);
                    JSONArray data = object.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object2 = data.getJSONObject(i);
                        String name = object2.getString("name");
                        int district_id = Integer.parseInt(object2.getString("district_id"));
                        int parent_id = Integer.parseInt(object2.getString("parent_id"));
                        areaList.add(name);//第三级县数据
                        districtList2.add(district_id);
                        districtList3.add(district_id);
                        parentList.add(parent_id);
                    }
                    area.setAdapter(areaAdapter);
                    setSpinnerItemSelectedByValue(area, tmp[2]);//设置spinner的默认值
                    Message message = new Message();
                    message.what = mFinish3;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (what == mWHAT4) {
                //请求成功,获取第四级小区
                String result = response.get();
                try {
                    plotList.clear();
                    JSONObject object = new JSONObject(result);
                    JSONArray data = object.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object2 = data.getJSONObject(i);
                        String name = object2.getString("name");
                        plotList.add(name);//第三级县数据
                    }
                    // plot.setSelection(mReceiptAddress.getZone_id(), true);//设置spinner的默认值
                    if (plotList.size() != 0) {
                        //找到收货小区,允许修改
                        true3 = true;
                        plot.setAdapter(plotAdapter);
                        plot.setSelection(mReceiptAddress.getZone_id(), true);//设置spinner的默认值
                        plot_linear.setVisibility(View.VISIBLE);
                        ButtonListener();
                    } else {
                        //没有收货小区,不能修改
                        true3 = false;
                        plot_linear.setVisibility(View.GONE);
                        ButtonListener();
                    }
                    Message message = new Message();
                    message.what = mFinish4;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (what == mWHAT5) {
                //请求成功,修改收货地址
                String result = response.get();
                try {
                    flag5 = true;
                    httpMethod();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (what == mWHAT6) {
                //请求成功,修改收货箱成功
                String result = response.get();
                try {

                } catch (Exception e) {
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
                country_id = countryList.get(position);
                mReceipt.setCountry_id(country_id + "");
                flag1 = true;
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
                flag2 = true;
                httpMethod();//费尽心机获取市级数据

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //用于监听区级单击事件
    private void initListener3() {
        area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                num3 = districtList3.get(position);
                mReceipt.setDistrict_id(num3 + "");
                flag3 = true;
                httpMethod();//费尽心机获取区级数据

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //用于监听小区级单击事件
    private void initListener4() {
        plot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mReceipt.setZone_id(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void show(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(User_modifyAddressActivity.this, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    private void gethead() {
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String username = sp.getString("USER_NAME", "");
        String password = sp.getString("PASSWORD", "");
        customer_id = sp.getString("CUSTOMER_ID", "");
        String addHeader = username + ":" + password;
        newHeader = new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));//加密后的header
    }

    //通过spinner的值来确定spinner的默认选定位置
    public static void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter.getCount();
        for (int i = 0; i < k; i++) {
            if (value.equals(apsAdapter.getItem(i).toString())) {
                spinner.setSelection(i, true);// 默认选中项
                break;
            }
        }
    }

    public void update_password_back3(View view) {
        Intent intent = new Intent(User_modifyAddressActivity.this, User_take_addressActivity.class);
        startActivity(intent);
        finish();
    }
}
