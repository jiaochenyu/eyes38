package com.example.eyes38.user_activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eyes38.R;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.SSLEngineResult;

public class User_find_password3Activity extends AppCompatActivity {
    public static final int INWHAT = 16;
    public static final int INFINISH = 18;
    private EditText firstpd, secondpd;
    private boolean success;
    private String firstValue, secondValue;
    private String user_tel;
    RequestQueue mRequestQueue;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INFINISH:
                    if (success){
                        Toast.makeText(User_find_password3Activity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        //说明修改成功
                        Intent intent = new Intent(User_find_password3Activity.this, User_loginActivity.class);
                        startActivity(intent);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_find_password3);
        initView();
    }


    //初始化控件
    private void initView() {
        firstpd = (EditText) findViewById(R.id.firstpd);
        secondpd = (EditText) findViewById(R.id.secondpd);
        Intent intent = getIntent();
        user_tel = intent.getStringExtra("user_tel2");
    }

    public void update_password_back3(View view) {
        finish();
    }

    public void user_findpassword_reback(View view) {
        //当点击确定按钮后
        firstValue = firstpd.getText().toString();
        secondValue = secondpd.getText().toString();
        if (firstValue.equals("")||secondValue.equals("")){
            Toast.makeText(User_find_password3Activity.this, "输入不能为空!", Toast.LENGTH_SHORT).show();
        }else if (!firstValue.equals(secondValue)){
            Toast.makeText(User_find_password3Activity.this, "两次输入的密码不一致,请重新输入!", Toast.LENGTH_SHORT).show();
            firstpd.setText("");
            secondpd.setText("");
        }else {
            getHttpMethod();//修改密码请求；
        }

    }

    private void getHttpMethod() {
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customers/recover-password/sms";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.add("mobile", user_tel);
        request.add("password", firstValue);
        request.add("confirmPassword", secondValue);
        mRequestQueue.add(INWHAT, request, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == INWHAT) {
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    success = object.getBoolean("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                success = false;
                Toast.makeText(User_find_password3Activity.this, "输入错误，请重新输入!", Toast.LENGTH_SHORT).show();
            }
            Message message = new Message();
            message.what = INFINISH;
            mHandler.sendMessage(message);

        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }

        @Override
        public void onFinish(int what) {

        }
    };
}
