package com.example.eyes38.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.adapter.Cart_GoodsAdapter;
import com.example.eyes38.fragment.cart.CartEmptyView;
import com.example.eyes38.fragment.cart.CartGoodsList;


/**
 * Created by jcy on 2016/5/8.
 */
public class CartFragment extends Fragment {

    MainActivity mMainActivity;
    private View view;
    private int cartgoodscount; //购物车中的数量
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    Cart_GoodsAdapter mCart_goodsAdapter;
    SharedPreferences sp;  //偏好设置 看用户登录是否登录


    public int getCartgoodscount() {
        return cartgoodscount;
    }

    public void setCartgoodscount(int cartgoodscount) {
        this.cartgoodscount = cartgoodscount;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        view = inflater.inflate(R.layout.cart, null);
        initViews();
        initData();
        initOnclickListener();
        //判断 购物车是否为空。 是空显示CartEmptyView
        initStates();
        return view;
    }

    private void initViews() {


    }

    private void initData() {
        mFragmentManager = getChildFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mCart_goodsAdapter = new Cart_GoodsAdapter();
        setCartgoodscount(mCart_goodsAdapter.getCartGoodsCount());
        Log.e("getCartgoodscount()", getCartgoodscount() + "");
    }

    private void initStates() {
        //如果用户没有登录 那么显示空
        sp = mMainActivity.getSharedPreferences("userInfo", mMainActivity.MODE_PRIVATE);  // 偏好设置初始化
        int flag = sp.getInt("STATE", 0);  // 取出用户登录状态， 如果为1 代表登录 如果为0 是没有登录
        if (flag == 0) {
            //如果用户没登录  购物车显示空
            CartEmptyView mCartEmptyView = new CartEmptyView();
            mFragmentTransaction.replace(R.id.cartcontent, mCartEmptyView);
            mFragmentTransaction.commit();
        } else {
            CartGoodsList mCartGoodsList = new CartGoodsList();
            mFragmentTransaction.replace(R.id.cartcontent, mCartGoodsList);
            mFragmentTransaction.commit();
        }
    }

    private void initOnclickListener() {

    }




}
