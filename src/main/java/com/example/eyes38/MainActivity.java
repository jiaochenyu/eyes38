package com.example.eyes38;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.eyes38.fragment.CartFragment;
import com.example.eyes38.fragment.HomeFragment;
import com.example.eyes38.fragment.SortFragment;
import com.example.eyes38.fragment.UserFragment;
import com.example.eyes38.user_activity.User_loginActivity;
import com.example.eyes38.utils.CartBadgeView;


public class MainActivity extends AppCompatActivity {
    private static final int CARTGOODSCOUNT = 308;
    private SharedPreferences sp;
    private static final int HOME = 1;
    private static final int SORT = 2;
    private static final int CAR = 3;
    private static final int USER = 4;

    private RadioGroup mRadioGroup;
    private HomeFragment mHomeFragment;
    private SortFragment mSortFragment;
    private CartFragment mCarFragment;
    private UserFragment mUserFragment;
    private int cartGoodsCount = 0;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    public static  CartBadgeView mCartBadgeView;


    RadioButton mCarradioButton;
    RadioButton mhomeRadioButton;
    public  Button mcar_badgebutton; //占位按钮 是透明的 为了让 徽章 显示在上面
    public Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CARTGOODSCOUNT:
                    if (((Integer) msg.obj) != 0) {
                        Log.e("购物车传值", msg.obj + "");
                        mCartBadgeView.setText(msg.obj + "");
                        mCartBadgeView.show();
                    } else {
                        mCartBadgeView.hide();
                    }
            }
        }
    };


    public int getCartGoodsCount() {
        return cartGoodsCount;
    }

    public void setCartGoodsCount(int cartGoodsCount) {
        this.cartGoodsCount = cartGoodsCount;
    }

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
        //初始化 cartradiobutton
        mCarradioButton = (RadioButton) findViewById(R.id.carRadiobutton);
        mhomeRadioButton = (RadioButton) findViewById(R.id.homeRadiobutton);
        mcar_badgebutton = (Button) findViewById(R.id.car_badgeviewbutton);
        //CartBadgeView这是购物车上的徽章
        //mCartBadgeView.setBackgroundResource(R.drawable.badge_ifaux);
        mCartBadgeView = new CartBadgeView(MainActivity.this, mcar_badgebutton);
        mCartBadgeView.setTextColor(Color.WHITE);
        mCartBadgeView.setTextSize(12);
        //mCartBadgeView.setBadgeMargin(30,30);
        mCartBadgeView.setBadgeMargin(5);//各个边的边隔
        mCartBadgeView.setBadgeBackgroundColor(this.getResources().getColor(R.color.topical));
        mCartBadgeView.setBadgePosition(CartBadgeView.POSITION_TOP_RIGHT);
        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE); //偏好设置,记录用户登录信息
        initCartBadge();
    }

    //设置徽章 样式
    private void initCartBadge() {
      Boolean login_state = sp.getBoolean("STATE", false);
        if (login_state ) {
            mCartBadgeView.hide();
        } else {
                if (getCartGoodsCount() == 0){
                    mCartBadgeView.hide();
                }else {
                    mCartBadgeView.show();
                }
        }

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
               /* if (mCarFragment == null) {
                    mCarFragment = new CartFragment();
                    mFragmentTransaction.add(R.id.fragment_container, mCarFragment);
                } else {
                    mFragmentTransaction.show(mCarFragment);
                }*/
                mCarFragment = new CartFragment();
                mFragmentTransaction.add(R.id.fragment_container, mCarFragment);
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
            case R.id.homeRadiobutton:
                showFragment(HOME);
                break;
            case R.id.sortRadiobutton:
                showFragment(SORT);
                break;
            case R.id.carRadiobutton:
                showFragment(CAR);
                break;
            case R.id.userRadiobutton:
               boolean login_state = sp.getBoolean("STATE", false);
                Log.e("login", login_state + "");
                if (login_state) {
                    showFragment(USER);
                } else {
                    Intent intent = new Intent(MainActivity.this, User_loginActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }

    }
}
