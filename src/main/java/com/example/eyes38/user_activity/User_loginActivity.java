package com.example.eyes38.user_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;

public class User_loginActivity extends AppCompatActivity {
    public static final int STATE = 1;
    private EditText username,password;
    private Button user_login;
    private String usernameValue,passwordValue;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        initViews();//初始化方法
        judgeLoginState();//判断登录状态
    }

    private void judgeLoginState() {
        username.setText(sp.getString("USER_NAME",""));
        password.setText(sp.getString("PASSWORD",""));
        //登录监听事件，现在默认为用户名为123，密码也为123
        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameValue=username.getText().toString();
                passwordValue=password.getText().toString();
                if (usernameValue.equals("123")&&passwordValue.equals("123")){
                    //默认保存用户信息
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("USER_NAME",usernameValue);
                    editor.putString("PASSWORD",passwordValue);
                    editor.putInt("STATE",STATE);
                    editor.commit();
                    Intent intent=new Intent(User_loginActivity.this,MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(User_loginActivity.this, "输入错误，登录失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews() {
        sp=this.getSharedPreferences("userInfo",MODE_PRIVATE);
        username= (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.password);
        user_login= (Button) findViewById(R.id.user_login);
    }

    //忘记密码
    public void user_forget_password(View view) {
        Intent intent=new Intent(User_loginActivity.this,User_findpasswordActivity.class);
        startActivity(intent);
    }
    //登录

    /**
     * 未写
     * @param view
     */
    public void user_login(View view) {
        Intent intent=new Intent(User_loginActivity.this, MainActivity.class);
        startActivity(intent);
    }
    //手机注册
    public void user_phone_register(View view) {
        Intent intent=new Intent(User_loginActivity.this,User_registerActivity.class);
        startActivity(intent);
    }
}
