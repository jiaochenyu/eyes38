package com.example.eyes38.user_fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.user_activity.User_creditsActivity;
import com.example.eyes38.user_activity.User_message_setActivity;
import com.example.eyes38.user_activity.User_orderActivity;
import com.example.eyes38.user_activity.User_personal_centerActivity;
import com.example.eyes38.user_activity.User_phone_setActivity;
import com.example.eyes38.user_activity.User_take_addressActivity;


/**
 * Created by cfj on 2016/5/21.
 */
public class AllFragment extends Fragment {
    User_orderActivity mMainActivity;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainActivity = ( User_orderActivity) getActivity();
        view = inflater.inflate(R.layout.activity_user_all,null);
        return view;
    }



}
