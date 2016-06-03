package com.example.eyes38.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.eyes38.R;
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

public class UserRegisterDetailActivity extends AppCompatActivity {
    public static final int WHAT = 1;
    public static final int PRO = 2;
    public static final int CITY = 3;
    public static final int AREA = 4;
    private EditText inputValidatecodeEditText, passwordEditText, confirmpasswordEditText;
    private EditText plotdetailEditText, consigneeEditText, telnumEditText, addressEditText;
    private Button obtainButton, registerButton;
    private TextView telnumTextView;
    //注册的电话号码
    private String telNum;
    //nohttp
    RequestQueue mRequestQueue = NoHttp.newRequestQueue();

    //三级联动解析
    //三个下拉列表
    private Spinner province, city, area;
    //存储三个下拉列表的位置
    private List<Integer> districtList, districtList2;
    //定义显示下拉列表的值
    private List<String> proList, cityList, areaList;
    //三个适配器
    private ArrayAdapter<String> proAdapter, cityAdapter, areaAdapter;
    private int num1, num2;

    boolean isSpinnerFirst1 = true ;
    boolean isSpinnerFirst2 = true ;
    boolean isSpinnerFirst3 = true ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_detail);
        getTelNum();
        initView();
        setData();
        initListener();
    }

    private void setData() {
        //将传来的是电话号码显示、
        telnumTextView.setText(telNum);
        //解析地区列表
        getProHttp();
    }

    private void getProHttp() {
        //获取省级列表
        String url = "http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("parent_id", "0");
        mRequestQueue.add(PRO, request, mOnResponseListener);
    }


    private void getTelNum() {
        Intent intent = getIntent();
        telNum = intent.getStringExtra("telNum");
    }

    private void initListener() {
        //再次获取验证码
        obtainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //注册
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        //省级下拉列表监听
        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerFirst1){
                    view.setVisibility(View.INVISIBLE);
                }
                isSpinnerFirst1 = false;
                num1 = districtList.get(position);
                getCityHttp();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //城市下拉列表监听
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerFirst2){
                    view.setVisibility(View.INVISIBLE);
                }
                isSpinnerFirst2 = false;
                num2 = districtList2.get(position);
                getAreaHttp();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //县区下拉列表监听
        area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerFirst3){
                    view.setVisibility(View.INVISIBLE);
                }
                isSpinnerFirst3 = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getAreaHttp() {
        //获取县级列表
        areaList.clear();
        String url = "http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("parent_id", num2);
        mRequestQueue.add(AREA, request, mOnResponseListener);
    }

    private void getCityHttp() {
        String url = "http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("parent_id", num1);
        mRequestQueue.add(CITY, request, mOnResponseListener);
    }

    private void register() {
        //注册
        String communityName = plotdetailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String validateCode = inputValidatecodeEditText.getText().toString();
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer/create";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.add("community", 6);
        request.add("communityName", "高博小区");
        request.add("confirmPassword", "123456");
        request.add("password", "123456");
        request.add("validateCode", validateCode);
        mRequestQueue.add(WHAT, request, mOnResponseListener);

    }

    //nohttp监听
    private OnResponseListener<String> mOnResponseListener;

    {
        mOnResponseListener = new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                if (what == WHAT) {
                    String result = response.get();
                    try {
                        JSONObject object = new JSONObject(result);
                        boolean success = object.getBoolean("success");
                        String msg = object.getString("msg");
                        Log.e("注册", success + msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (what == PRO) {
                    //解析省级列表
                    String result = response.get();
                    try {
                        districtList = new ArrayList<>();
                        proList = new ArrayList<>();
                        JSONObject object = new JSONObject(result);
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object2 = data.getJSONObject(i);
                            String name = object2.getString("name");
                            int district_id = Integer.parseInt(object2.getString("district_id"));
                            int parent_id = Integer.parseInt(object2.getString("parent_id"));
                            proList.add(name);//省数据List已经获取
                            districtList.add(district_id);
                        }
                        initProAdapter();
                        province.setAdapter(proAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (what == CITY) {
                    //解析城市列表
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
                        }
                        if (cityList.size() != 0) {
                            city.setVisibility(View.VISIBLE);
                        }else {
                            city.setVisibility(View.GONE);
                            area.setVisibility(View.GONE);
                        }
                        initCityAdapter();
                        city.setAdapter(cityAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (what == AREA) {
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
                        }
                        if (areaList.size() != 0) {
                            area.setVisibility(View.VISIBLE);
                        }else {
                            area.setVisibility(View.GONE);
                        }
                        initAreaAdapter();
                        area.setAdapter(areaAdapter);
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

    private void initProAdapter() {
        //初始化省级适配器
        proAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, proList);
        proAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void initCityAdapter() {
        //初始化省级适配器
        cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void initAreaAdapter() {
        //初始化省级适配器
        areaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaList);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void initView() {
        inputValidatecodeEditText = (EditText) findViewById(R.id.user_register_detail_validatecode);
        passwordEditText = (EditText) findViewById(R.id.user_register_detail_password);
        confirmpasswordEditText = (EditText) findViewById(R.id.user_register_detail_confirmpassword);
        plotdetailEditText = (EditText) findViewById(R.id.user_register_detail_plotname);
        consigneeEditText = (EditText) findViewById(R.id.user_register_detail_consignee);
        telnumEditText = (EditText) findViewById(R.id.user_register_detail_consignee_telnum);
        addressEditText = (EditText) findViewById(R.id.user_register_detail_address);
        obtainButton = (Button) findViewById(R.id.user_register_detail_obtain);
        registerButton = (Button) findViewById(R.id.user_register_detail_register);
        telnumTextView = (TextView) findViewById(R.id.user_register_detail_num);
        province = (Spinner) findViewById(R.id.user_register_detail_plot_spinner_pro);
        city = (Spinner) findViewById(R.id.user_register_detail_plot_spinner_city);
        area = (Spinner) findViewById(R.id.user_register_detail_plot_spinner_area);
        //初始化list
        proList = new ArrayList<>();
        cityList = new ArrayList<>();
        areaList = new ArrayList<>();
        districtList = new ArrayList<>();
        districtList2 = new ArrayList<>();
        //初始化适配器

    }

    public void back(View view) {
        finish();
    }
}
