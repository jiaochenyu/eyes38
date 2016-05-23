package com.example.eyes38.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.fragment.cart.CartEmptyView;
import com.example.eyes38.fragment.cart.CartGoodsList;


/**
 * Created by jcy on 2016/5/8.
 */
public class CartFragment extends Fragment {

    MainActivity mMainActivity;
    private View view;
    public static int count = 1; //购物车中的数量
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;


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
        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();


    }

    private void initStates() {
        //是空显示CartEmptyView
        if (count == 0) {
            CartEmptyView mCartEmptyView = new CartEmptyView();
            mFragmentTransaction.replace(R.id.cartcontent, mCartEmptyView);
            mFragmentTransaction.commit();
        } else {
            //不为空 初始化适配器
            CartGoodsList mCartGoodsList = new CartGoodsList();
            mFragmentTransaction.replace(R.id.cartcontent, mCartGoodsList);
            mFragmentTransaction.commit();
        }
    }

    private void initOnclickListener() {


    }




}
