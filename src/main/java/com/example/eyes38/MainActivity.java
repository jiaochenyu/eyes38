package com.example.eyes38;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.eyes38.adapter.Cart_GoodsAdapter;
import com.example.eyes38.fragment.CartFragment;
import com.example.eyes38.fragment.HomeFragment;
import com.example.eyes38.fragment.SortFragment;
import com.example.eyes38.fragment.UserFragment;
import com.example.eyes38.user_activity.User_loginActivity;
import com.example.eyes38.utils.CartBadgeView;


public class MainActivity extends AppCompatActivity {
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

    private Cart_GoodsAdapter mCart_goodsAdapter;

    RadioButton  mCarradioButton ;
    RadioButton  mhomeRadioButton;
    Button mcar_badgebutton; //占位按钮 是透明的 为了让 徽章 显示在上面


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
        CartBadgeView mCartBadgeView = new CartBadgeView(MainActivity.this,mcar_badgebutton);

        //mCartBadgeView.setBackgroundResource(R.drawable.badge_ifaux);

        initCartBadge(mCartBadgeView,getCartGoodsCount());
        sp=this.getSharedPreferences("userInfo",MODE_PRIVATE); //偏好设置,记录用户登录信息
    }


    //设置徽章 样式
    private void initCartBadge(CartBadgeView mCartBadgeView, int cartGoodsCount){
        if(cartGoodsCount == 0){
            mCartBadgeView.hide();
        }else{
            mCartBadgeView.setText(cartGoodsCount+"");
            mCartBadgeView.setTextColor(Color.WHITE);
            mCartBadgeView.setTextSize(12);
            //mCartBadgeView.setBadgeMargin(30,30);
            mCartBadgeView.setBadgeMargin(5);//各个边的边隔
            mCartBadgeView.setBadgeBackgroundColor(this.getResources().getColor(R.color.topical));
            mCartBadgeView.setBadgePosition(CartBadgeView.POSITION_TOP_RIGHT);
            mCartBadgeView.show();
        }

    }




    //初始化数据
    private void initData() {
        mCart_goodsAdapter = new Cart_GoodsAdapter();
        setCartGoodsCount(mCart_goodsAdapter.getCartGoodsCount());
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
                /*else{
                    mCarFragment = new CartFragment();
                    mFragmentTransaction.replace(R.id.fragment_container,mCarFragment);
                }*/

                 /*else {
                    mFragmentTransaction.replace(R.id.fragment_container,mCarFragment);
                    mFragmentTransaction.show(mCarFragment);
                }*/
               /* mCarFragment = new CartFragment();
                mFragmentTransaction.replace(R.id.fragment_container,mCarFragment);*/
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
               /* CartFragment mCartFragment = new CartFragment();
                FragmentTransaction mmFragmentTransaction =mFragmentManager.beginTransaction();
                mmFragmentTransaction.replace(R.id.fragment_container,mCartFragment);
                mmFragmentTransaction.commit();*/
                break;
            case R.id.userRadiobutton:
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
