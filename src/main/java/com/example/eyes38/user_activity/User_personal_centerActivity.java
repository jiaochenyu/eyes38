package com.example.eyes38.user_activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.eyes38.R;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class User_personal_centerActivity extends AppCompatActivity {
    public static final int mWHAT = 321;
    public static final int MFINISH = 322;
    public static final int MWHAT2 = 323;
    public static final int MFINISH2 = 344;
    //先从偏好设置中取出账号
    private SharedPreferences sp;
    //初始化数据
    private String customer_id, username, firstname, sex, eamil;
    //更新的数据
    private String update_name,update_email,update_sex,update_image;
    //定义控件
    private TextView person_center_tel;
    private EditText person_center_name, person_center_email;
    private Spinner mSpinner;
    private RequestQueue mRequestQueue;
    private Button user_person_center_save;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MFINISH:
                break;
                case MFINISH2:
                    Log.e("VVV","no");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_personal_center);
        //初始化控件
        initViews();
        //先将bean中的数据传入
        initData();
        //在更新数据
        updateData();
    }
    //更新数据
    private void updateData() {
        initEditListener();
    }

    private void initEditListener() {
        //对两个EditText进行监听


        person_center_name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                person_center_name.setFocusableInTouchMode(true);
                person_center_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                            //姓名文本改变之后
                            update_name=person_center_name.getText().toString();
                    }
                });
            }
        });
        //对性别spinner进行判断
       mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             String sex= (String) mSpinner.getItemAtPosition(position);
               if (sex.equals("男")){
                   update_sex="M";
               }else {
                   update_sex="F";
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
        person_center_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                person_center_email.setFocusableInTouchMode(true);
                person_center_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        update_email=person_center_email.getText().toString();
                    }
                });
            }
        });
        //对确定按钮进行监听
        user_person_center_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpMethod2();
                finish();
            }
        });


    }
    //进行数据的更新更新
    private void httpMethod2() {
        mRequestQueue=NoHttp.newRequestQueue();
        String url="http://38eye.test.ilexnet.com/api/mobile/customer-api/customers/"+customer_id;
        Request<String> request=NoHttp.createStringRequest(url,RequestMethod.POST);
        request.add("email",update_email);
        request.add("firstname",update_name);
        request.add("sex",update_sex);
        mRequestQueue.add(MWHAT2,request,mOnResponseListener);
    }

    //初始化控件上需要显示的值
    private void initData() {
        //先从偏好设置中取出customer_id
        customer_id = sp.getString("CUSTOMER_ID", "");
        httpMethod();
    }

    private void httpMethod() {
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customers/"+customer_id;
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        mRequestQueue.add(mWHAT, request, mOnResponseListener);
    }
    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWHAT) {
                //请求成功
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject object1 = object.getJSONObject("data");
                    username = object1.getString("username");
                    firstname = object1.getString("firstname");
                    sex = object1.getString("sex");
                    eamil = object1.getString("email");
                    //进行更新操作
                    //用户名
                    person_center_tel.setText(username);
                    person_center_tel.setTextSize(15);
                    //姓名
                    person_center_name.setText(firstname);
                    person_center_tel.setTextSize(15);
                    //性别
                    if (sex.equals("M")){
                        mSpinner.setSelection(1);

                    }else {
                        mSpinner.setSelection(0);
                    }
                    mSpinner.setVisibility(View.VISIBLE);
                    //邮箱
                    person_center_email.setText(eamil);
                    person_center_tel.setTextSize(15);
                    Log.e("VVV","yes");
                    Message message=new Message();
                    message.what= MFINISH;
                    mHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (what==MWHAT2){
                Message message=new Message();
                message.what= MFINISH2;
                mHandler.sendMessage(message);
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
        }

        @Override
        public void onFinish(int what) {

        }
    };

    //完成控件的绑定
    private void initViews() {
        //绑定控件
        user_person_center_save= (Button) findViewById(R.id.person_center_save);
        person_center_tel = (TextView) findViewById(R.id.person_center_tel);
        person_center_name = (EditText) findViewById(R.id.person_center_name);
        mSpinner = (Spinner) findViewById(R.id.spinner_sex);
        person_center_email = (EditText) findViewById(R.id.person_center_email);
        //初始化偏好设置
        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);//偏好设置
    }

    //返回键
    public void user_personal_center_back(View view) {
        finish();
    }

}
