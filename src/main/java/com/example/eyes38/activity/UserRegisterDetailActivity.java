package com.example.eyes38.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eyes38.R;

public class UserRegisterDetailActivity extends AppCompatActivity {
    private EditText inputValidatecodeEditText,passwordEditText,confirmpasswordEditText;
    private EditText plotdetailEditText,consigneeEditText,telnumEditText,addressEditText;
    private Button obtainButton,registerButton;
    private TextView telnumTextView;
    //注册的电话号码
    private String telNum;

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
    }

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
