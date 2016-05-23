package com.example.eyes38.fragment.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;

/**
 * Created by jqchen on 2016/5/20.
 */
public class CartEmptyView extends Fragment {
    private View mView;
    MainActivity mMainActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        mView = inflater.inflate(R.layout.cart_empty,null);
        initViews();
        initListener();
        return mView;
    }


    private void initViews() {
    }

    private void initListener() {
    }
}
