package com.example.eyes38.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eyes38.R;
import com.example.eyes38.adapter.CustomerServiceAdapter;
import com.example.eyes38.beans.Consult;
import com.example.eyes38.utils.ConsultDataBase;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerServiceActivity extends AppCompatActivity {
    public static final int GETCONSULT = 100;
    public static final int GETHEADER = 100;
    private List<Consult> mList;
    private CustomerServiceAdapter serviceAdapter;
    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private Button mButton;
    private RequestQueue mRequestQueue;
    private Toast mToast;
    private ConsultDataBase consultDataBase;
    private int customer_id;
    private String info;
    private String imagePath;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);
        initView();
        ininData();
        initAdapter();
        initListener();
    }
    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.servicecustomer_recyclerview);
        mEditText = (EditText) findViewById(R.id.servicecustomer_content);
        mButton = (Button) findViewById(R.id.servicecustomer_send);
    }

    private void ininData() {
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        mList = new ArrayList<>();
        mRequestQueue = NoHttp.newRequestQueue();
        consultDataBase = new ConsultDataBase(this);
        getCustomerID();
        getConsultFromDataBase();
        getHeaderimagePath();
    }

    private void getHeaderimagePath() {
        String username = sharedPreferences.getString("USER_NAME", "");
        String password = sharedPreferences.getString("PASSWORD", "");
        String header = username + "" + password;
        String newHeader = new String(Base64.encode(header.getBytes(), Base64.DEFAULT));//加密后的header
        String Authorization = "Basic " + newHeader;
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customers/"+customer_id;
        Request<String> mRequest = NoHttp.createStringRequest(url, RequestMethod.GET);
        mRequest.setCacheMode(CacheMode.DEFAULT);
        mRequest.addHeader("Authorization", Authorization);
        mRequestQueue.add(GETHEADER, mRequest, mOnResponseListener);
    }

    private void getCustomerID() {
        //获取用户id

        String customer = sharedPreferences.getString("CUSTOMER_ID", "");
        customer_id = Integer.parseInt(customer);
    }

    private void getConsultFromDataBase() {
        //从数据库中回去聊天历史记录
        List<Consult> list = consultDataBase.selectAllConsult(customer_id);
        mList.addAll(list);
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(CustomerServiceActivity.this));
        serviceAdapter = new CustomerServiceAdapter(mList, CustomerServiceActivity.this);
        mRecyclerView.setAdapter(serviceAdapter);
        resetRecyclerView();
    }

    private void resetRecyclerView() {
        //使recyclerview显示到底部
        mRecyclerView.scrollToPosition(serviceAdapter.getItemCount());
    }

    private void initListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info = mEditText.getText().toString();
                if (!info.equals("")){
                    Consult consult = new Consult(imagePath,info);
                    mList.add(consult);
                    serviceAdapter.notifyDataSetChanged();
                    getConsultHttp(info);
                    //清空输入框
                    mEditText.setText("");
                }else {
                    show("内容不能为空!");
                }

            }
        });
    }

    private void getConsultHttp(String info) {
        String key = "4139ae26e568353ac337a9299935aa75";
        String url = "http://www.tuling123.com/openapi/api";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.setCacheMode(CacheMode.DEFAULT);
        JSONObject object = new JSONObject();
        try {
            object.put("key",key);
            object.put("info",info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setDefineRequestBodyForJson(object);
        mRequestQueue.add(GETCONSULT, request, mOnResponseListener);

    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {


        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == GETCONSULT) {
                String result = response.get();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String text = jsonObject.getString("text");
                    Consult consult = new Consult("", text);
                    mList.add(consult);
                    serviceAdapter.notifyDataSetChanged();
                    resetRecyclerView();
                    consultDataBase.InsertDataBase(customer_id,info,text,imagePath);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (what == GETHEADER){
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject object1 = object.getJSONObject("data");
                    imagePath = object1.getString("image");
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

    public void back(View view) {
        finish();
    }
    private void show(String text) {
        //显示信息
        if (mToast == null) {
            mToast = Toast.makeText(CustomerServiceActivity.this, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }
}
