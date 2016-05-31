package com.example.eyes38.user_activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.eyes38.R;
import com.example.eyes38.activity.UserRegisterDetailActivity;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户注册，输入电话，接收验证码后，填写详细信息
 */
public class User_registerActivity extends AppCompatActivity {
    public static final int WHAT = 1;
    public static final int SUCCESS = 1;
    public static final int FAIL = 2;
    //输入电话号码
    private EditText telEditText;
    //点击下一步的按钮
    private Button nextButton;
    private String telNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        initView();
        initListener();
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SUCCESS:
                    goRegisterDetail();
                    break;
                case FAIL:
                    break;
            }
        }
    };

    private void goRegisterDetail() {
        Intent intent = new Intent(User_registerActivity.this, UserRegisterDetailActivity.class);
        intent.putExtra("telNum",telEditText.getText().toString());
        startActivity(intent);
    }

    private void initListener() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValidatecode();
            }
        });
    }

    private void getValidatecode() {
        //获取验证码
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer/validatecode";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        //获取电话号码
        telNum = telEditText.getText().toString();
        Log.e("tel",telNum);
        request.add("mobile",telNum);
        mRequestQueue.add(WHAT,request,mOnResponseListener);

    }
    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == WHAT){
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    boolean status = object.getBoolean("success");
                    if (status){
                        mHandler.sendEmptyMessage(SUCCESS);
                    }else {
                        mHandler.sendEmptyMessage(FAIL);
                    }
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

    private void initView() {
        telEditText = (EditText) findViewById(R.id.user_register_telnum);
        nextButton = (Button) findViewById(R.id.user_password_save);
    }

    public void user_register_back(View view) {
        onBackPressed();
    }

    public void user_register_login(View view) {
        Intent intent=new Intent(User_registerActivity.this,User_loginActivity.class);
        startActivity(intent);
    }

}
