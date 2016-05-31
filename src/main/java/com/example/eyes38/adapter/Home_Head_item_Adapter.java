package com.example.eyes38.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.beans.HomeContentContent;

/**
 * Created by jqchen on 2016/5/17.
 */
public class Home_Head_item_Adapter extends RecyclerView.Adapter<Home_Head_item_Adapter.MyViewHolder> implements View.OnClickListener {
    String name;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private OnMoreClickListener mOnMoreClickListener = null;
    Context mContext;


    //定义监听接口
    public static interface OnRecyclerViewItemClickListener {

        void onItemClick(View view, HomeContentContent hcc);
    }

    public static interface OnMoreClickListener {

        void onItemClick(View view, String homeContent);
    }

    public void setOnMoreClickListener(OnMoreClickListener listener) {
        this.mOnMoreClickListener = listener;
    }

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public Home_Head_item_Adapter(String name, Context mContext) {
        this.mContext = mContext;
        this.name = name;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detal_head_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        //注册监听事件
        myViewHolder.mLinearLayout.setOnClickListener(this);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView.setText(name);
        //设置标志的值
        holder.mLinearLayout.setTag(name);
    }

    @Override
    public void onClick(View v) {
        if (mOnMoreClickListener != null) {
            //取出标示处的值
            mOnMoreClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        LinearLayout mLinearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.home_more);
            mTextView = (TextView) itemView.findViewById(R.id.zhuantiname);
        }
    }
}
