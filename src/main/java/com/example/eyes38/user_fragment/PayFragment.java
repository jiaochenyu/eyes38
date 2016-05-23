package com.example.eyes38.user_fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eyes38.R;
import com.example.eyes38.user_activity.User_orderActivity;


/**
 * Created by cfj on 2016/5/8.
 */
public class PayFragment extends Fragment {
    User_orderActivity mMainActivity;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainActivity = ( User_orderActivity) getActivity();
        view = inflater.inflate(R.layout.activity_user_pay,null);
        return view;
    }
}
