package com.example.eyes38.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.beans.Community;

import java.util.List;

/**
 * Created by jqchen on 2016/6/3.
 */
public class SpinnerAdapter extends BaseAdapter {
    private List<Community> mList;
    private Context mContext;
    LayoutInflater inflater;


    public SpinnerAdapter(List<Community> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.community_spinner_item,null);
        if (convertView != null){
            TextView mTextView = (TextView) convertView.findViewById(R.id.community_name);
            mTextView.setText(mList.get(position).getName());
        }
        return convertView;
    }
}
