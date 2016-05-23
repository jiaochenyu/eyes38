package com.example.eyes38.fragment;


<<<<<<< HEAD
=======
import android.content.Intent;
import android.content.SharedPreferences;
>>>>>>> 1c26917280a48cf6bddab8c263caca7a8662f7c5
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;


/**
 * Created by jcy on 2016/5/8.
 */
public class UserFragment extends Fragment {
    private  SharedPreferences sp;
    MainActivity mMainActivity;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
<<<<<<< HEAD
        view = inflater.inflate(R.layout.user,null);
=======
        view = inflater.inflate(R.layout.user, null);
        initViews();
        toUserSet();
        toUserMessage();
        toUserPersonalMessage();
        toMyCredits();
        toMyAddress();
        //监听并传值
        ListenerValues();
        //向我的订单页面传值，告诉其应该默认页
>>>>>>> 1c26917280a48cf6bddab8c263caca7a8662f7c5
        return view;
    }
}
