package com.example.eyes38.user_activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eyes38.R;
import com.example.eyes38.utils.ValidateCode;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class User_findpassword2Activity extends AppCompatActivity {
    public static final int FINDWHAT = 59;
    private TextView user_findpassword_text;//提示信息
    private EditText user_find_code;//验证码输入框
    private Button user_get_code;//获取验证码按钮框
    private String user_tel;//手机号
    private String code;//用户输入的验证码
    private RequestQueue mRequestQueue;
    private int count = 59;//倒计时时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_findpassword2);
        initViews();
        getTelnum();
        initListener();
        //启动倒计时
        startMyCount();
    }

    private void startMyCount() {
        Mycount mcount=new Mycount(60*1000,1000);
        mcount.start();
    }

    private void initListener() {
        //再次获取验证码
        user_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用获取验证码的方法
                ValidateCode.getValideCode(user_tel);
                //设置按钮显示的样式
                count = 59;
                Mycount mycount = new Mycount(60 * 1000, 1000);
                mycount.start();
            }
        });
    }


    //获取上一级的手机号
    private void getTelnum() {
        Intent intent = getIntent();
        user_tel = intent.getStringExtra("User_tel");
        user_findpassword_text.setText("验证码已发送到手机" + user_tel);
    }

    //绑定控件
    private void initViews() {
        user_findpassword_text = (TextView) findViewById(R.id.user_findpassword_text);
        user_find_code = (EditText) findViewById(R.id.user_find_code);
        user_get_code = (Button) findViewById(R.id.user_get_code);
        mRequestQueue = NoHttp.newRequestQueue();
    }

    public void find_password_backTwo(View view) {
        onBackPressed();
    }

    public void user_find_next2(View view) {
        //对输入的验证码框进行监听
        code = user_find_code.getText().toString();//验证码框输入内容
        if (code.equals("") || code.length() != 6) {
            Toast.makeText(User_findpassword2Activity.this, "验证码输入错误！", Toast.LENGTH_SHORT).show();
        } else {
            //验证验证码是否正确
            confirmCode();
        }
    }

    //用于验证验证码是否正确
    private void confirmCode() {
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customers/valicode";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.add("mobile", user_tel);
        request.add("validateCode", code);
        mRequestQueue.add(FINDWHAT, request, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == FINDWHAT) {
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    boolean success = object.getBoolean("success");
                    if (success){
                        Intent intent = new Intent(User_findpassword2Activity.this, User_find_password3Activity.class);
                        intent.putExtra("user_tel2",user_tel);
                        startActivity(intent);
                    }else {
                        Toast.makeText(User_findpassword2Activity.this, "验证码输入错误！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(User_findpassword2Activity.this, "验证码输入错误！", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

    //自定义倒计时类，实现60秒重新获取验证码
    private class Mycount extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public Mycount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            //当倒计时的时候,设置按钮不可点击
            user_get_code.setClickable(false);
            user_get_code.setBackgroundColor(getResources().getColor(R.color.background));
            user_get_code.setTextColor(getResources().getColor(R.color.black));
        }

        @Override
        public void onTick(long millisUntilFinished) {
            user_get_code.setText(count + "秒后获取验证码");
            count--;
        }

        @Override
        public void onFinish() {
            user_get_code.setText("获取验证码");
            user_get_code.setBackgroundColor(getResources().getColor(R.color.them_color));
            user_get_code.setTextColor(getResources().getColor(R.color.white));
            user_get_code.setClickable(true);
        }
    }

}
