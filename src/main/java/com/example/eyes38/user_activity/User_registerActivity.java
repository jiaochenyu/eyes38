package com.example.eyes38.user_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.eyes38.R;
import com.example.eyes38.activity.UserRegisterDetailActivity;
import com.example.eyes38.utils.ValidateCode;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户注册，输入电话，接收验证码后，填写详细信息
 */
public class User_registerActivity extends AppCompatActivity {
    public static final int JUST = 1;
    //输入电话号码
    private EditText telEditText;
    //点击下一步的按钮
    private Button nextButton;
    private String telNum;
    private RequestQueue mRequestQueue;
    //偏好设置
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);//偏好设置初始化
    }

    private void initListener() {
        //对注册按钮进行监听
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证电话是否已注册
                telNum = telEditText.getText().toString();
                JustValidate();
            }
        });
        //对电话输入框输入进行实时监听
        telEditText.addTextChangedListener(new PhoneEditviewListener());
    }

    private class PhoneEditviewListener implements TextWatcher {
        //对注册页面电话输入框进行监听
        CharSequence temp;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //如果长度到达11位，设置下一步按钮可以点击
            if (temp.length() == 11){
                nextButton.setClickable(true);
                nextButton.setBackground(getResources().getDrawable(R.color.them_color));
                nextButton.setTextColor(getResources().getColor(R.color.black));
//                nextButton.setBackgroundColor(getColor(R.color.them_color));
//                nextButton.setTextColor(getColor(R.color.white));
            }else {
                nextButton.setClickable(false);
                //getcolor api23中添加的
//                nextButton.setBackgroundColor(getColor(R.color.background));
//                nextButton.setBackground(getDrawable(R.color.background));
                nextButton.setBackground(getResources().getDrawable(R.color.background));
                nextButton.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }

    private void JustValidate() {
        //验证电话是否已注册
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customer-helper/check_mobile_exist/" +
                telNum;
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
//        request.add("mobile","15638897507");
        mRequestQueue.add(JUST,request,mOnResponseListener);
    }
    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == JUST){
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    boolean isRegister = object.getBoolean("success");
                    //如果未注册，获取验证码，如果已注册，提示用户
                    if (isRegister){
                        //提示用户已注册
                        showIsRegister();
                    }else {
                        //获取验证码
                        getValidate();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //取消所有的请求
            mRequestQueue.cancelAll();
            mRequestQueue.stop();
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

    private void showIsRegister() {
        //提示用户已经注册
//        Toast.makeText(this,"已注册",Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("用户已注册")
                .setPositiveButton("确定",null)
                .show();
    }

    private void getValidate() {
        //获取验证码，并跳转到注册详细信息界面
        boolean result = ValidateCode.getValideCode(telNum);
        Intent intent = new Intent(User_registerActivity.this, UserRegisterDetailActivity.class);
        intent.putExtra("telNum", telNum);
        startActivity(intent);
    }

    private void initView() {
        telEditText = (EditText) findViewById(R.id.user_register_telnum);
        nextButton = (Button) findViewById(R.id.user_password_save);
    }

    public void user_register_back(View view) {
        finish();
    }

    public void user_register_login(View view) {
        finish();
    }

}
