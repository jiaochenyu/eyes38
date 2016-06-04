package com.example.eyes38.user_activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.eyes38.R;
import com.example.eyes38.fragment.user.AllFragment;
import com.example.eyes38.fragment.user.CommitFragment;
import com.example.eyes38.fragment.user.DelieverFragment;
import com.example.eyes38.fragment.user.PayFragment;
import com.example.eyes38.fragment.user.ReceiverFragment;

public class User_orderActivity extends AppCompatActivity {
    private static final int ALL = 1;
    private static final int PAY = 2;
    private static final int DELIEVER = 3;
    private static final int RECEIVER = 4;
    private static final int COMMIT = 5;

    private RadioGroup mRadioGroup;
    private AllFragment mAllFragment;
    private PayFragment mPayFragment;
    private DelieverFragment mDelieverFragment;
    private ReceiverFragment mReceiverFragment;
    private CommitFragment mCommitFragment;
    //事务
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private  RadioButton user_order_all,user_order_pay,user_order_deliever,user_order_receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        //初始化组件
        initViews();
        initData();
        initListeners();
    }

    //初始化数据
    private void initData() {
        mFragmentManager = getSupportFragmentManager();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        int flag=bundle.getInt("TAG");
        Log.e("cfj",""+flag);
        if (flag==0) {
            showFragment(ALL);
            user_order_all.setChecked(true);
        } else if (flag==1) {
            showFragment(PAY);
            user_order_pay.setChecked(true);
        } else if (flag == 2) {
            showFragment(DELIEVER);
            user_order_deliever.setChecked(true);
        } else if (flag == 3) {
            showFragment(RECEIVER);
            user_order_receiver.setChecked(true);
        }


    }

    //显示fragment
    private void showFragment(int index) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        //隐藏
        hideFragment(mFragmentTransaction);
        /**
         * 如果Fragment为空，就新建一个实例
         * 如果不为空，就将它从栈中显示出来
         */
        switch (index) {
            case ALL:
                if (mAllFragment == null) {
                    mAllFragment = new AllFragment();
                    mFragmentTransaction.add(R.id.user_fragment_container, mAllFragment);
                } else {
                    mFragmentTransaction.show(mAllFragment);
                }
                break;
            case PAY:
                if (mPayFragment == null) {
                    mPayFragment = new PayFragment();
                    mFragmentTransaction.add(R.id.user_fragment_container, mPayFragment);
                } else {
                    mFragmentTransaction.show(mPayFragment);
                }
                break;
            case DELIEVER:
                if (mDelieverFragment == null) {
                    mDelieverFragment = new DelieverFragment();
                    mFragmentTransaction.add(R.id.user_fragment_container, mDelieverFragment);
                } else {
                    mFragmentTransaction.show(mDelieverFragment);
                }
                break;
            case RECEIVER:
                if (mReceiverFragment == null) {
                    mReceiverFragment = new ReceiverFragment();
                    mFragmentTransaction.add(R.id.user_fragment_container, mReceiverFragment);
                } else {
                    mFragmentTransaction.show(mReceiverFragment);
                }
                break;
            case COMMIT:
                if (mCommitFragment == null) {
                    mCommitFragment = new CommitFragment();
                    mFragmentTransaction.add(R.id.user_fragment_container, mCommitFragment);
                } else {
                    mFragmentTransaction.show(mCommitFragment);
                }
                break;
        }
        mFragmentTransaction.commit();

    }

    //隐藏
    private void hideFragment(FragmentTransaction ft) {
        if (mAllFragment != null) {
            ft.hide(mAllFragment);
        }
        if (mPayFragment != null) {
            ft.hide(mPayFragment);
        }
        if (mDelieverFragment != null) {
            ft.hide(mDelieverFragment);
        }
        if (mReceiverFragment != null) {
            ft.hide(mReceiverFragment);
        }
        if (mCommitFragment != null) {
            ft.hide(mCommitFragment);
        }

    }

    private void initViews() {
        mRadioGroup = (RadioGroup) findViewById(R.id.group);
        user_order_all= (RadioButton) findViewById(R.id.user_order_all);
        user_order_pay= (RadioButton) findViewById(R.id.user_order_pay);
        user_order_deliever= (RadioButton) findViewById(R.id.user_order_deliever);
        user_order_receiver= (RadioButton) findViewById(R.id.user_order_receiver);
    }

    private void initListeners() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //选定界面
                resetPager(checkedId);
            }
        });
    }

    //选定界面
    private void resetPager(int checkedId) {
        switch (checkedId) {
            case R.id.user_order_all:
                showFragment(ALL);
                break;
            case R.id.user_order_pay:
                showFragment(PAY);
                break;
            case R.id.user_order_deliever:
                showFragment(DELIEVER);
                break;
            case R.id.user_order_receiver:
                showFragment(RECEIVER);
                break;
            case R.id.user_order_commit:
                showFragment(COMMIT);
                break;
            default:
                break;
        }
    }
    //返回键
    public void user_myorder_back(View view) {
        onBackPressed();
    }
}
