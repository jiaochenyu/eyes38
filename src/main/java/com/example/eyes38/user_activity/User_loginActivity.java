package com.example.eyes38.user_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eyes38.Application.Application;
import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class User_loginActivity extends AppCompatActivity {
    InputMethodManager imm;
    private Toast mToast;
    public static final int MWHAT = 411;
    private EditText username, password;
    private Button user_login;//登录按钮
    private String usernameValue, passwordValue;
    private SharedPreferences sp;
    private String customer_id;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        initViews();//初始化方法
        initData();
        //在点击登录按钮之前，需要先判断偏好设置中有没有值
        adjustSpValues();
        initListener();
    }

    private void initData() {
        mRequestQueue = NoHttp.newRequestQueue();
    }

    private void adjustSpValues() {
        //先去偏好设置里面取，如果有值则直接显示在输入框内
        usernameValue = sp.getString("USER_NAME", "");
        passwordValue = sp.getString("PASSWORD", "");
        if (!usernameValue.equals("") && !passwordValue.equals("")) {
            username.setText(usernameValue);
            password.setText(passwordValue);
        }
    }

    private void initListener() {
        //对登录按钮进行监听
        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpMethod();//传递url
            }
        });
    }

    private void httpMethod() {
        //对账号+":"+密码 Base64进行加密
        usernameValue = username.getText().toString();
        passwordValue = password.getText().toString();
        String addHeader = usernameValue + ":" + passwordValue;
        String newHeader = new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));//加密后的header
        //查看用户输入的账号和密码是否正确
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer/login";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        String header = "Basic " + newHeader;
        request.addHeader("Authorization", header);
        mRequestQueue.add(MWHAT, request, mOnResponseListener);
    }

    //请求http结果 回调对象 接受请求结果 用于解析数据
    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == MWHAT) {
                //请求成功
                String result = response.get();
                if (result.equals("密码错误")){
                    resetEditTex();
                }
                else {
                    //登录成功
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject object = (JSONObject) jsonObject.get("data");
                        customer_id = object.getString("customer_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    successLogin();
                }
            }

        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            //隐藏软键盘
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }

        @Override
        public void onFinish(int what) {

        }
    };

    private void successLogin() {
        //登录成功
        //默认保存用户信息,并跳转到
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_NAME", usernameValue);
        editor.putString("PASSWORD", passwordValue);
        Application.isLogin = true;
        editor.putString("CUSTOMER_ID", customer_id+"");
        editor.putBoolean("STATE", Application.isLogin);
        editor.apply();
        //跳转到首页
        Intent intent = new Intent(User_loginActivity.this, MainActivity.class);
        startActivity(intent);
        show("登录成功！");
    }

    private void resetEditTex() {
        //密码错误
        show("密码输入错误，请重新输入！");
        password.setText("");
    }

    private void initViews() {
        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);//偏好设置初始化
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        user_login = (Button) findViewById(R.id.user_login);
    }

    //忘记密码
    public void user_forget_password(View view) {
        Intent intent = new Intent(User_loginActivity.this, User_findpasswordActivity.class);
        startActivity(intent);
    }
    //登录

    //手机注册
    public void user_phone_register(View view) {
        Intent intent = new Intent(User_loginActivity.this, User_registerActivity.class);
        startActivity(intent);
    }

    private void show(String text) {
        //显示信息
        if (mToast == null) {
            mToast = Toast.makeText(User_loginActivity.this, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }
}
