package com.example.eyes38.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.eyes38.Application.Application;
import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.adapter.AreaAdapter;
import com.example.eyes38.adapter.SpinnerAdapter;
import com.example.eyes38.beans.Area;
import com.example.eyes38.beans.Community;
import com.example.eyes38.utils.Substring;
import com.example.eyes38.utils.ValidateCode;
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
    public static final int REGISTER = 1;
    public static final int PRO = 2;
    public static final int CITY = 3;
    public static final int AREA = 4;
    public static final int COMMIUNITY = 5;
    private EditText inputValidatecodeEditText, passwordEditText, confirmpasswordEditText;
    private Button obtainButton, registerButton;
    private TextView telnumTextView;
    //对验证码输入框验证图片
    private ImageView validateCodeAdjust;
    //对密码和验证密码的验证图片
    private ImageView passwordAdjust, confirmPasswordAdjust;
    //对小区的选择的验证图片
    private ImageView communityAdjust;
    //注册的电话号码
    private String telNum;
    //nohttp
    private RequestQueue mRequestQueue = NoHttp.newRequestQueue();
    //存储其他的控件的可用性
    private List<Boolean> mlist;

    //三级联动解析
    //三个下拉列表
    private Spinner province, city, area, communityspinner;
    //小区的数据源
    private List<Community> communitiesList;
    //记录小区信息
    private Community community = new Community();
    //适配器
    private AreaAdapter proAreaAdapter, cityAreaAdapter, areaAdapter;
    //三级联动数据源
    private List<Area> proAreas, cityAreas, areaArea;
    private int count = 59;
    //偏好设置
    SharedPreferences sp;


    //自定义倒计时类,实现60秒后重新获取验证码
    private class MyCount extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            //设置按钮不可点击
            obtainButton.setClickable(false);
            obtainButton.setBackgroundColor(getResources().getColor(R.color.background));
            obtainButton.setTextColor(getResources().getColor(R.color.black));
        }

        @Override
        public void onTick(long millisUntilFinished) {
            obtainButton.setText(count + "秒后获取验证码");
            count--;
        }

        @Override
        public void onFinish() {
            obtainButton.setText("获取验证码");
            obtainButton.setBackgroundColor(getResources().getColor(R.color.them_color));
            obtainButton.setTextColor(getResources().getColor(R.color.white));
            obtainButton.setClickable(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_detail);
        getTelNum();
        initView();
        setData();
        initListener();
        //启动倒计时
        startMyCount();
    }

    private void startMyCount() {
        MyCount myCount = new MyCount(60 * 1000, 1000);
        myCount.start();
    }

    private void setData() {
        //将传来的是电话号码显示、
        telnumTextView.setText(Substring.getPhoneString(telNum));
        //解析地区列表
        getProHttp();
        mlist = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mlist.add(false);
        }
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
                //调用获取验证码的方法
                ValidateCode.getValideCode(telNum);
                //设置按钮显示的样式
                count = 59;
                MyCount myCount = new MyCount(60 * 1000, 1000);
                myCount.start();
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
                //传入点击的district_id
                int district_id = proAreas.get(position).getDistrict_id();
                getCityHttp(district_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //城市下拉列表监听
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int district_id = cityAreas.get(position).getDistrict_id();
                getAreaHttp(district_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //县区下拉列表监听
        area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int district_id = areaArea.get(position).getDistrict_id();
                getCommunity(district_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //对小区下拉监听
        communityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                community.setId(communitiesList.get(position).getId());
                community.setName(communitiesList.get(position).getName());
                communityAdjust.setImageResource(R.drawable.right);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                communityAdjust.setImageResource(R.drawable.exclamation);
            }
        });
        /**
         * 对各个输入框添加实时监听
         */
        //对验证码的输入框的监听
        inputValidatecodeEditText.addTextChangedListener(new ValidateCodeListener());
        //对密码和确认密码的输入框的监听
        passwordEditText.addTextChangedListener(new PasswordEditViewListener());
        confirmpasswordEditText.addTextChangedListener(new ConfirmPasswordEditViewListener());

    }

    private void getCommunity(int district_id) {
        //获取community
        String url = "http://38eye.test.ilexnet.com/api/mobile/community/list";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("district_id", district_id);
        mRequestQueue.add(COMMIUNITY, request, mOnResponseListener);
    }

    private void getAreaHttp(int district_id) {
        //获取县级列表
        String url = "http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("parent_id", district_id);
        mRequestQueue.add(AREA, request, mOnResponseListener);
    }

    private void getCityHttp(int district_id) {
        String url = "http://38eye.test.ilexnet.com/api/mobile/setting-api/districts";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("parent_id", district_id);
        mRequestQueue.add(CITY, request, mOnResponseListener);
    }

    private void register() {
        //注册
        String communityName = community.getName();
        int communityid = community.getId();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmpasswordEditText.getText().toString();
        String validateCode = inputValidatecodeEditText.getText().toString();
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer/create";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.add("community", communityid);
        request.add("mobile", telNum);
        request.add("communityName", communityName);
        request.add("confirmPassword", password);
        request.add("password", password);
        request.add("validateCode", validateCode);
        mRequestQueue.add(REGISTER, request, mOnResponseListener);

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
                if (what == REGISTER) {
                    String result = response.get();
                    try {
                        JSONObject object = new JSONObject(result);
                        boolean success = object.getBoolean("success");
                        String msg = object.getString("msg");
                        Log.e("注册", success + msg);
                        if (success) {
                            //保存注册信息并跳转到mainactivity
                            saveLoginInformation();
                            goToUser();

                        } else {
                            //注册不成功，提示用户
                            showRegisterFail();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (what == PRO) {
                    //解析省级列表
                    String result = response.get();
                    try {
                        //初始化
                        proAreas = new ArrayList<>();
                        JSONObject object = new JSONObject(result);
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object2 = data.getJSONObject(i);
                            String name = object2.getString("name");
                            int district_id = object2.getInt("district_id");
                            int parent_id = object2.getInt("parent_id");
                            Area area = new Area(district_id, parent_id, name);
                            proAreas.add(area);
                        }
                        proAreaAdapter = new AreaAdapter(proAreas, UserRegisterDetailActivity.this);
                        province.setAdapter(proAreaAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (what == CITY) {
                    //解析城市列表
                    //请求成功
                    String result = response.get();
                    try {
                        cityAreas = new ArrayList<>();
                        JSONObject object = new JSONObject(result);
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object2 = data.getJSONObject(i);
                            String name = object2.getString("name");
                            int district_id = object2.getInt("district_id");
                            int parent_id = object2.getInt("parent_id");
                            Area area = new Area(district_id, parent_id, name);
                            cityAreas.add(area);
                        }
                        if (cityAreas.size() != 0) {
                            city.setVisibility(View.VISIBLE);
                        } else {
                            city.setVisibility(View.GONE);
                            area.setVisibility(View.GONE);
                        }
                        cityAreaAdapter = new AreaAdapter(cityAreas, UserRegisterDetailActivity.this);
                        city.setAdapter(cityAreaAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (what == AREA) {
                    //请求成功
                    String result = response.get();
                    try {
                        areaArea = new ArrayList<>();
                        JSONObject object = new JSONObject(result);
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object2 = data.getJSONObject(i);
                            String name = object2.getString("name");
                            int district_id = Integer.parseInt(object2.getString("district_id"));
                            int parent_id = Integer.parseInt(object2.getString("parent_id"));
                            Area area = new Area(district_id, parent_id, name);
                            areaArea.add(area);
                        }
                        if (areaArea.size() != 0) {
                            area.setVisibility(View.VISIBLE);
                        } else {
                            area.setVisibility(View.GONE);
                        }
                        areaAdapter = new AreaAdapter(areaArea, UserRegisterDetailActivity.this);
                        area.setAdapter(areaAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (what == COMMIUNITY) {
                    //请求成功
                    String result = response.get();
                    try {
                        communitiesList = new ArrayList<>();
                        JSONObject object = new JSONObject(result);
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object1 = array.getJSONObject(i);
                            int id = object1.getInt("id");
                            String name = object1.getString("name");
                            int district_id = object1.getInt("district_id");
                            Community community = new Community(id, name, district_id);
                            communitiesList.add(community);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (communitiesList.size() != 0) {
                        mlist.set(3, true);
                    } else {
                        mlist.set(3, false);

                    }
                    registerAdjust();
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(communitiesList, UserRegisterDetailActivity.this);
                    communityspinner.setAdapter(spinnerAdapter);
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

    private void goToUser() {
        Intent intent = new Intent(UserRegisterDetailActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveLoginInformation() {
        //保存注册信息
        sp = UserRegisterDetailActivity.this.getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_NAME", telNum);
        editor.putString("PASSWORD", passwordEditText.getText().toString());
        Application.isLogin = true;
        editor.putBoolean("STATE", Application.isLogin);
        editor.commit();
    }

    private void initView() {
        inputValidatecodeEditText = (EditText) findViewById(R.id.user_register_detail_validatecode);
        passwordEditText = (EditText) findViewById(R.id.user_register_detail_password);
        confirmpasswordEditText = (EditText) findViewById(R.id.user_register_detail_confirmpassword);
        obtainButton = (Button) findViewById(R.id.user_register_detail_obtain);
        registerButton = (Button) findViewById(R.id.user_register_detail_register);
        telnumTextView = (TextView) findViewById(R.id.user_register_detail_num);
        province = (Spinner) findViewById(R.id.user_register_detail_plot_spinner_pro);
        city = (Spinner) findViewById(R.id.user_register_detail_plot_spinner_city);
        area = (Spinner) findViewById(R.id.user_register_detail_plot_spinner_area);
        communityspinner = (Spinner) findViewById(R.id.user_register_detail_plotname);
        //对各个输入框验证的imageview的初始化
        validateCodeAdjust = (ImageView) findViewById(R.id.user_register_detail_validatecode_adjust);
        passwordAdjust = (ImageView) findViewById(R.id.user_register_detail_password_adjust);
        confirmPasswordAdjust = (ImageView) findViewById(R.id.user_register_detail_confirmpassword_adjust);
        communityAdjust = (ImageView) findViewById(R.id.user_register_detail_communtiy_adjust);
    }

    public void back(View view) {
        finish();
    }

    /**
     * 对各个输入框的监听事件的监听类
     */

    private class ValidateCodeListener implements TextWatcher {
        //对验证码的输入监听
        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() == 6) {
                inputValidatecodeEditText.clearFocus();
                validateCodeAdjust.setImageResource(R.drawable.right);
                mlist.set(0, true);
            } else {
                validateCodeAdjust.setImageResource(R.drawable.exclamation);
                mlist.set(0, false);
            }
            registerAdjust();
        }
    }


    private class PasswordEditViewListener implements TextWatcher {
        //对密码的输入框的实时监听
        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
            confirmpasswordEditText.setText("");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() >= 6) {
                passwordAdjust.setImageResource(R.drawable.right);
                mlist.set(1, true);

            } else {
                passwordAdjust.setImageResource(R.drawable.exclamation);
                mlist.set(1, false);
            }
            registerAdjust();
        }
    }

    private class ConfirmPasswordEditViewListener implements TextWatcher {
        //对确认密码的输入框的实时监听
        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String password = passwordEditText.getText().toString();
            if (temp.toString().equals(password)) {
                confirmPasswordAdjust.setImageResource(R.drawable.right);
                mlist.set(2, true);
                confirmpasswordEditText.clearFocus();
            } else {
                confirmPasswordAdjust.setImageResource(R.drawable.exclamation);
                mlist.set(2, false);
            }
            registerAdjust();
        }
    }

    private void registerAdjust() {
        int count = 0;
        for (int i = 0; i < mlist.size(); i++) {
            if (mlist.get(i)) {
                count++;
            }
        }
        if (count == 4) {
            registerButton.setClickable(true);
            registerButton.setBackground(getResources().getDrawable(R.color.them_color));
            registerButton.setTextColor(getResources().getColor(R.color.white));
        } else {
            registerButton.setClickable(false);
            registerButton.setBackground(getResources().getDrawable(R.color.bottom_line));
            registerButton.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void showRegisterFail() {
        //提示用户已经注册
//        Toast.makeText(this,"已注册",Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("注册失败")
                .setPositiveButton("确定", null)
                .show();
    }

}
