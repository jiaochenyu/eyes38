package com.example.eyes38.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eyes38.R;

/**
 * Created by jqchen on 2016/5/17.
 */
public class Home_ContentnoneAdapter extends RecyclerView.Adapter {
    Context mContext;
    private LayoutInflater mInflater;

    public Home_ContentnoneAdapter( Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_zhuanti_none, parent, false);
        RecyclerView.ViewHolder viewHolder = new Home_item_adapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
