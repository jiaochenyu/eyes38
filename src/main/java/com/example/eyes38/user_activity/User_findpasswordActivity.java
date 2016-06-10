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
import android.widget.Toast;

import com.example.eyes38.R;
import com.example.eyes38.utils.ValidateCode;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class User_findpasswordActivity extends AppCompatActivity {
    public static final int INTWHAT = 4;
    public static final int INTFINISH = 5;
    private EditText mEditText;
    private String user_tel;//输入的手机号
    private Button user_find1;//下一步按钮
    private RequestQueue mRequestQueue;
    private boolean result1;//用户判断该手机号是否注册
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INTFINISH:
                    //如果已经注册，向该手机号发送验证码
                    Log.e("result1", result1 + "");
                    if (result1) {
                        //发送验证码,并跳转到下一步
                        boolean result2 = ValidateCode.getValideCode(user_tel);
                        Intent intent = new Intent(User_findpasswordActivity.this, User_findpassword2Activity.class);
                        intent.putExtra("User_tel", user_tel);
                        startActivity(intent);
                    } else {
                        Toast.makeText(User_findpasswordActivity.this, "该手机未注册！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_findpassword);
        initViews();//初始化控件
        initListener();//监听下一步按钮
    }

    private void initListener() {
        user_find1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_tel = mEditText.getText().toString();
                if (user_tel.equals("") || user_tel.length() != 11) {
                    Toast.makeText(User_findpasswordActivity.this, "请输入注册手机号!", Toast.LENGTH_SHORT).show();
                } else {
                    //当输入不为空时,先检查是否注册
                    getHttpMethod1();
                }
            }
        });

    }

    private void initViews() {
        user_find1 = (Button) findViewById(R.id.user_find1);
        mEditText = (EditText) findViewById(R.id.user_input_tel);
        mRequestQueue = NoHttp.newRequestQueue();
    }


    //网络请求,判断该手机号是否已经注册
    private void getHttpMethod1() {
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customer-helper/check_mobile_exist/" + user_tel;
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        mRequestQueue.add(INTWHAT, request, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == INTWHAT) {
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    result1 = object.getBoolean("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                result1 = false;
            }
            Message message = new Message();
            message.what = INTFINISH;
            mHandler.sendMessage(message);

        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

    //返回键
    public void find_password_back(View view) {
        finish();
    }
}
