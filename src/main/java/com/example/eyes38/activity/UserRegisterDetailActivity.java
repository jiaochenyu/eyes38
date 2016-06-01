package com.example.eyes38.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eyes38.R;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRegisterDetailActivity extends AppCompatActivity {
    public static final int WHAT = 1;
    private EditText inputValidatecodeEditText,passwordEditText,confirmpasswordEditText;
    private EditText plotdetailEditText,consigneeEditText,telnumEditText,addressEditText;
    private Button obtainButton,registerButton;
    private TextView telnumTextView;
    //注册的电话号码
    private String telNum;
    //nohttp
    RequestQueue mRequestQueue;

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
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        //注册
        String communityName = plotdetailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String validateCode = inputValidatecodeEditText.getText().toString();
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer/create";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.add("community",6);
        request.add("communityName","高博小区");
        request.add("confirmPassword","123456");
        request.add("password","123456");
        request.add("validateCode",validateCode);
        mRequestQueue.add(WHAT,request,mOnResponseListener);

    }
    //nohttp监听
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
                    boolean success = object.getBoolean("success");
                    String msg = object.getString("msg");
                    Log.e("注册",success+msg);
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
    }

    public void back(View view) {
        finish();
    }
}
