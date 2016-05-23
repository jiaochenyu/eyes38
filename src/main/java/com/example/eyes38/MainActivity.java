package com.example.eyes38;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

import com.example.eyes38.fragment.CarFragment;
import com.example.eyes38.fragment.HomeFragment;
import com.example.eyes38.fragment.SortFragment;
import com.example.eyes38.fragment.UserFragment;
import com.example.eyes38.user_activity.User_loginActivity;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp;
    int count;
    private static final int HOME = 1;
    private static final int SORT = 2;
    private static final int CAR = 3;
    private static final int USER = 4;

    private RadioGroup mRadioGroup;
    private HomeFragment mHomeFragment;
    private SortFragment mSortFragment;
    private CarFragment mCarFragment;
    private UserFragment mUserFragment;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化组件
        initView();
        initData();
        initListeners();
    }

    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.group);
        sp=this.getSharedPreferences("userInfo",MODE_PRIVATE);
    }

    //初始化数据
    private void initData() {
        mFragmentManager = getSupportFragmentManager();
        //设置home 为默认页面
        showFragment(HOME);
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
            case HOME:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    mFragmentTransaction.add(R.id.fragment_container, mHomeFragment);
                } else {
                    mFragmentTransaction.show(mHomeFragment);
                }
                break;
            case SORT:
                if (mSortFragment == null) {
                    mSortFragment = new SortFragment();
                    mFragmentTransaction.add(R.id.fragment_container, mSortFragment);
                } else {
                    mFragmentTransaction.show(mSortFragment);
                }
                break;
            case CAR:

                if (mCarFragment == null) {
                    mCarFragment = new CarFragment();
                    mFragmentTransaction.add(R.id.fragment_container, mCarFragment);
                } else {
                    mFragmentTransaction.show(mCarFragment);
                }
                break;
            case USER:
                if (mUserFragment == null) {
                    mUserFragment = new UserFragment();
                    mFragmentTransaction.add(R.id.fragment_container, mUserFragment);
                } else {
                    mFragmentTransaction.show(mUserFragment);
                }
                break;
        }
        mFragmentTransaction.commit();
    }

    //初始化组件
    private void hideFragment(FragmentTransaction ft) {
        if (mHomeFragment != null) {
            ft.hide(mHomeFragment);
        }
        if (mSortFragment != null) {
            ft.hide(mSortFragment);
        }
        if (mCarFragment != null) {
            ft.hide(mCarFragment);
        }
        if (mUserFragment != null) {
            ft.hide(mUserFragment);
        }
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
            case R.id.home:
                showFragment(HOME);
                break;
            case R.id.sort:
                showFragment(SORT);
                break;
            case R.id.car:
                showFragment(CAR);
                break;
            case R.id.user:
               int login_state=sp.getInt("STATE",0);
                Log.e("login",login_state+"");
                if (login_state==1){
                    showFragment(USER);
                }
                else {
                    Intent intent=new Intent(MainActivity.this,User_loginActivity.class);
                    startActivity(intent);
                }

                break;
            default:
                break;
        }

    }
}
