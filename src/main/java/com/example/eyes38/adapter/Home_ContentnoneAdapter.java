package com.example.eyes38.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eyes38.R;
import com.example.eyes38.beans.HomeContentContent;

import java.util.List;

/**
 * Created by jqchen on 2016/5/17.
 */
public class Home_ContentnoneAdapter extends RecyclerView.Adapter<Home_ContentnoneAdapter.MyViewHolder> implements View.OnClickListener {
    private List<HomeContentContent> mList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    Context mContext;


    //定义监听接口
    public static interface OnRecyclerViewItemClickListener {

        void onItemClick(View view, HomeContentContent hcc);
    }

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public Home_ContentnoneAdapter(List<HomeContentContent> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_zhuanti_none, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            //注册监听事件
//            view.setOnClickListener(this);
            return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (HomeContentContent) v.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView mTextView;
//        ImageView mImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
//            mImageView = (ImageView) itemView.findViewById(R.id.home_content_content_image);
//            mTextView = (TextView) itemView.findViewById(R.id.home_content_content_textview);
        }
    }
}
