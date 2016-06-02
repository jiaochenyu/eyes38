package com.example.eyes38.user_activity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User_loginActivity extends AppCompatActivity {
    private Toast mToast;
    public static final int MWHAT = 411;
    public static final int MFINISH = 412;
    private String newHeader;
    List<UserBean> mList;//用来存取用户的一些信息
    public static final int STATE = 1;
    private String success;
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
                    if (success.equals("true")) {
                        //默认保存用户信息,并跳转到
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("USER_NAME", usernameValue);
                        editor.putString("PASSWORD", passwordValue);
                        editor.putInt("STATE", STATE);
                        editor.commit();
                        finish();
                    } else {
                        show("密码输入错误，请重新输入错误！");
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
        judgeLoginState();//判断登录状态
    }

    private void judgeLoginState() {
        //先去偏好设置里面取，如果有则直接显示在上面
        username.setText(sp.getString("USER_NAME", ""));
        password.setText(sp.getString("PASSWORD", ""));
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
        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对账号+":"+密码 Base64进行加密
                String addHeader = usernameValue + ":" + passwordValue;
                newHeader = new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));//加密后的header
                Log.e("TTT",newHeader);
                httpMethod();//传递url
            }
        });


    }

    private void httpMethod() {
        //查看用户输入的账号和密码是否正确
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer/login";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        Log.e("TTT", newHeader);
        String header="Basic "+newHeader;
        Log.e("TTT",header);
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
            //请求成功
            String result = response.get();
            try {

                JSONObject object1 = new JSONObject(result);
                success = object1.getString("success");//取得用户是否成功登录
                Log.e("TTT",success);
                JSONObject object2 = new JSONObject("data");
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
                Message message = new Message();
                message.what = MFINISH;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            Log.e("TTT","请求数据失败！");
        }

        @Override
        public void onFinish(int what) {

        }
    };

    private void initViews() {
        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
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

    /**
     * 未写
     *
     * @param view
     */
    public void user_login(View view) {
        Intent intent = new Intent(User_loginActivity.this, MainActivity.class);
        startActivity(intent);
    }

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
