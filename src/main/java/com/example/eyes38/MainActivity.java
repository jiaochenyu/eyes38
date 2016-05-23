package com.example.eyes38;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.eyes38.fragment.CartFragment;
import com.example.eyes38.fragment.HomeFragment;
import com.example.eyes38.fragment.SortFragment;
import com.example.eyes38.fragment.UserFragment;
import com.example.eyes38.utils.CartBadgeView;

public class MainActivity extends AppCompatActivity {

    private static final int HOME = 1;
    private static final int SORT = 2;
    private static final int CAR = 3;
    private static final int USER = 4;

    private RadioGroup mRadioGroup;
    private HomeFragment mHomeFragment;
    private SortFragment mSortFragment;
    private CartFragment mCarFragment;
    private UserFragment mUserFragment;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    RadioButton  mCarradioButton ;
    RadioButton  mhomeRadioButton;
    Button mcar_badgebutton; //占位按钮 是透明的 为了让 徽章 显示在上面


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
        CartBadgeView mCartBadgeView = new CartBadgeView(MainActivity.this,mcar_badgebutton);
        //mCartBadgeView.setBackgroundResource(R.drawable.badge_ifaux);
        mCartBadgeView.setText("3");
        mCartBadgeView.setTextColor(Color.WHITE);
        mCartBadgeView.setTextSize(12);
        //mCartBadgeView.setBadgeMargin(30,30);
        mCartBadgeView.setBadgeMargin(5);//各个边的边隔
        mCartBadgeView.setBadgeBackgroundColor(this.getResources().getColor(R.color.topical));
        mCartBadgeView.setBadgePosition(CartBadgeView.POSITION_TOP_RIGHT);
        mCartBadgeView.show();
    }


    //设置徽章上显示的数据
    private void badgeViewNum(){

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
                    mCarFragment = new CartFragment();
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

      /*  //点击 carradiobutton
        mhomeRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "我是购物车112", Toast.LENGTH_SHORT).show();
                CartBadgeView mCartBadgeView = new CartBadgeView(MainActivity.this,mcar_badgebutton);
                mCartBadgeView.setBackgroundResource(R.drawable.badge_ifaux);
                mCartBadgeView.setText("3");
                mCartBadgeView.setBadgePosition(CartBadgeView.POSITION_TOP_RIGHT);
                mCartBadgeView.show();
            }
        });*/
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
                showFragment(USER);
                break;
            default:
                break;
        }

    }
}
