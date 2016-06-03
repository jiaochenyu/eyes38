package com.example.eyes38.user_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eyes38.Application.Application;
import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.beans.UserBean;
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

public class User_loginActivity extends AppCompatActivity {
    InputMethodManager imm;
    private Toast mToast;
    public static final int MWHAT = 411;
    public static final int MFINISH = 412;
    private String newHeader;
    List<UserBean> mList;//用来存取用户的一些信息
    private boolean success;
    private EditText username, password;
    private Button user_login;//登录按钮
    private String usernameValue, passwordValue;
    private SharedPreferences sp;
    private RequestQueue mRequestQueue;//请求队列
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MFINISH:
                    //进行更新ui操作
                    if (success) {
                        //默认保存用户信息,并跳转到
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("USER_NAME", usernameValue);
                        editor.putString("PASSWORD", passwordValue);
                        Application.isLogin = true;
                        editor.putBoolean("STATE", Application.isLogin);
                        editor.commit();
                        //跳转到首页
                        Intent intent = new Intent(User_loginActivity.this, MainActivity.class);
                        startActivity(intent);
                        show("登录成功！");
                    } else {
                        show("密码输入错误，请重新输入！");
                        //再次对文本框进行监听
                        initEditTextListener();
                        initButtonListener();
                    }
                    break;

            }
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        initViews();//初始化方法
        //在点击登录按钮之前，需要先判断偏好设置中有没有值
        adjustSpValues();


    }

    private void adjustSpValues() {
        //先去偏好设置里面取，如果有则直接显示在上面
        username.setText(sp.getString("USER_NAME", ""));
        password.setText(sp.getString("PASSWORD", ""));
        //如果当前账号密码不为空，则直接将当前值传入进行判断,即偏好设置中有值
        if (!username.getText().toString().equals("") && !password.getText().toString().equals("")) {
            //先将值放入
            usernameValue = username.getText().toString();
            passwordValue = password.getText().toString();
            //对登录按钮进行监听,其一：单击了登录按钮
            user_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击登录按钮后
                    //对账号+":"+密码 Base64进行加密
                    String addHeader = usernameValue + ":" + passwordValue;
                    newHeader = new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));//加密后的header
                    httpMethod();//传递url

                }
            });
            //其二：未单击登录按钮，对账号和密码文本框进行监听
            initEditTextListener();
            initButtonListener();

        } else {
            //表示偏好设置中没有值，此时需要对文本框进行监听
            initEditTextListener();
            String usernameValue=username.getText().toString();
            String passwordValue=password.getText().toString();
            if (passwordValue.equals("") || usernameValue.equals("")){
                user_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //隐藏软键盘
                        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        show("输入有误，请重新输入！");
                    }
                });
            }
            initButtonListener();
        }
    }

    private void initButtonListener() {
        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对账号+":"+密码 Base64进行加密
                String addHeader = usernameValue + ":" + passwordValue;
                newHeader = new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));//加密后的header
                httpMethod();//传递url
            }
        });
    }

    //用于账号和密码文本框监听
    private void initEditTextListener() {
        //否则 ，对账号和密码框进行监听
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setFocusableInTouchMode(true);
                username.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        usernameValue = username.getText().toString();
                        Log.e("TTT", usernameValue);
                    }
                });
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setFocusableInTouchMode(true);
                password.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        passwordValue = password.getText().toString();
                        Log.e("TTT", passwordValue);
                    }
                });
            }
        });
    }


    private void httpMethod() {
        //查看用户输入的账号和密码是否正确
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer/login";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        String header = "Basic " + newHeader;
        Log.e("TTT", header);
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
                try {
                    JSONObject object1 = new JSONObject(result);
                    success = object1.getBoolean("success");//取得用户是否成功登录
                    JSONObject object2 = object1.getJSONObject("data");
                    String customer_id = object2.getString("customer_id");
                    String username = object2.getString("username");
                    String firstname = object2.getString("firstname");
                    String email = object2.getString("email");
                    String sex = object2.getString("sex");
                    Log.e("TTT", success + "-->" + customer_id + "-->" + firstname);
                    //将取到的值存入bean中
                    UserBean userBean = new UserBean();
                    userBean.setCustomer_id(customer_id);
                    userBean.setFirstname(firstname);
                    userBean.setEmail(email);
                    userBean.setSex(sex);
                    userBean.setUsername(username);
                    mList.add(userBean);
                    Log.e("TTT", success + "");
                    Message message = new Message();
                    message.what = MFINISH;
                    mHandler.sendMessage(message);//通知更新主线程更新ui
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            //隐藏软键盘
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
           show("输入有误，请重新输入！");
        }

        @Override
        public void onFinish(int what) {

        }
    };

    private void initViews() {
        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);//偏好设置初始化
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        user_login = (Button) findViewById(R.id.user_login);
        mList = new ArrayList<>();
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
        if (mToast == null) {
            mToast = Toast.makeText(User_loginActivity.this, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }
}
