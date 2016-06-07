package com.example.eyes38.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.eyes38.Application.Application;
import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.user_activity.User_backgoodActivity;
import com.example.eyes38.user_activity.User_creditsActivity;
import com.example.eyes38.user_activity.User_loginActivity;
import com.example.eyes38.user_activity.User_message_setActivity;
import com.example.eyes38.user_activity.User_orderActivity;
import com.example.eyes38.user_activity.User_personal_centerActivity;
import com.example.eyes38.user_activity.User_phone_setActivity;
import com.example.eyes38.user_activity.User_take_addressActivity;


/**
 * Created by jcy on 2016/5/8.
 */
public class UserFragment extends Fragment {
    public static final int REFRESH = 400;
    MainActivity mMainActivity;
    ImageView user_set, user_message;
    View view;
    int flag;//用于传值
    RadioButton user_pay, user_send, user_receive, user_back;
    LinearLayout user_person_set;//头像跳转
    LinearLayout user_myorder;//我的订单
    LinearLayout user_mycredits;//我的积分
    LinearLayout user_address;//我的积分
    TextView user_tel_set;
    SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        view = inflater.inflate(R.layout.user, null);
        initSharePreferences();
        initViews();
        //验证是否登录
        adjustIsLogin();
        toUserSet();
        toUserMessage();
        toUserPersonalMessage();
        toMyCredits();
        toMyAddress();
        //监听并传值
        ListenerValues();
        //向我的订单页面传值，告诉其应该默认页
        return view;
    }

    private void initSharePreferences() {
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUsername();
    }


    private void adjustIsLogin() {
        //获取偏好设置的登录状态
        Application.isLogin = sp.getBoolean("STATE", false);
        if (!Application.isLogin) {
            Intent intent = new Intent(mMainActivity, User_loginActivity.class);
            startActivity(intent);
        } else {

            updateUsername();
        }
    }

    public Handler handler = new Handler() {
        //接收数据，更新user
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH:
                    updateUsername();
                    break;
            }
        }
    };

    private void updateUsername() {
        //设置登录名
        String username = sp.getString("USER_NAME", "");
        String custom_id=sp.getString("CUSTOMER_ID","");

        user_tel_set.setText(username);
        user_tel_set.setTextSize(12);

    }

    private void ListenerValues() {
        //跳转到我的订单页面
        user_myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                Intent intent = new Intent(getContext(), User_orderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("TAG", flag);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        //跳转到待付款页面
        user_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                Intent intent = new Intent();
                intent.putExtra("TAG", flag);
                intent.setClass(getActivity(), User_orderActivity.class);
                startActivity(intent);

            }
        });
        //跳转到待发货页面
        user_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2;
                Intent intent = new Intent();
                intent.putExtra("TAG", flag);
                intent.setClass(getActivity(), User_orderActivity.class);
                startActivity(intent);

            }
        });
        //跳转到待收货页面
        user_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 3;
                Intent intent = new Intent();
                intent.putExtra("TAG", flag);
                intent.setClass(getActivity(), User_orderActivity.class);
                startActivity(intent);

            }
        });
        //跳转到待退货页面
        user_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), User_backgoodActivity.class);
                startActivity(intent);

            }
        });

    }


    //跳转到我的收货地址管理页面
    private void toMyAddress() {
        user_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), User_take_addressActivity.class);
                startActivity(intent);
            }
        });
    }

    //跳转到我的积分页面
    private void toMyCredits() {
        user_mycredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), User_creditsActivity.class);
                startActivity(intent);
            }
        });
    }


    //跳转到个人信息中心
    private void toUserPersonalMessage() {
        user_person_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), User_personal_centerActivity.class);
                startActivity(intent);
            }
        });
    }


    //跳转到消息中心设置
    private void toUserMessage() {
        user_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), User_message_setActivity.class);
                startActivity(intent);
            }
        });
    }

    //跳转到个人手机号设置
    private void toUserSet() {
        user_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), User_phone_setActivity.class);
                startActivity(intent);
            }
        });
    }

    //初始化控件
    private void initViews() {
        user_tel_set = (TextView) view.findViewById(R.id.username_tel_set);
        user_set = (ImageView) view.findViewById(R.id.user_set);
        user_message = (ImageView) view.findViewById(R.id.user_message);
        user_person_set = (LinearLayout) view.findViewById(R.id.user_person_set);
        user_myorder = (LinearLayout) view.findViewById(R.id.user_myorder);
        user_mycredits = (LinearLayout) view.findViewById(R.id.user_mycredits);
        user_address = (LinearLayout) view.findViewById(R.id.user_myaddress);
        user_pay = (RadioButton) view.findViewById(R.id.user_purse);
        user_send = (RadioButton) view.findViewById(R.id.user_send);
        user_receive = (RadioButton) view.findViewById(R.id.user_receive);
        user_back = (RadioButton) view.findViewById(R.id.user_back);

    }


}
