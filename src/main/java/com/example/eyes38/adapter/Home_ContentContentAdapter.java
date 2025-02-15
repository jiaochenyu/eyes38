package com.example.eyes38.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eyes38.R;
import com.example.eyes38.beans.HomeContentContent;

import java.util.List;

/**
 * Created by jqchen on 2016/5/17.
 */
public class Home_ContentContentAdapter extends RecyclerView.Adapter<Home_ContentContentAdapter.MyViewHolder> implements View.OnClickListener {
    private List<HomeContentContent> mList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    Context mContext;
    private String zhuantiname;//记录是哪一个专题的产品


    //定义监听接口
    public static interface OnRecyclerViewItemClickListener {

        void onItemClick(View view, HomeContentContent hcc);
    }

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public Home_ContentContentAdapter(List<HomeContentContent> mList, Context mContext, String zhuantiname) {
        this.mList = mList;
        this.mContext = mContext;
        this.zhuantiname=zhuantiname;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_content_content_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            //注册监听事件
            view.setOnClickListener(this);
            return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView.setText(mList.get(position).getGoods_name());
        if (!mList.get(position).getPath().equals("")) {
            Glide.with(mContext).load(mList.get(position).getPath()).into(holder.mImageView);
        }
        holder.itemView.setTag(mList.get(position));
        holder.priceTextvew.setText(mList.get(position).getGoods_platform_price()+"");
        holder.danweiTexiview.setText(mList.get(position).getGoods_unit()+"");
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
        TextView mTextView;//商品名
        ImageView mImageView;//图片
        TextView priceTextvew;//价格
        TextView danweiTexiview;//单位

        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.home_imageview);
            mTextView = (TextView) itemView.findViewById(R.id.home_sort_item_nametextview);
            priceTextvew = (TextView) itemView.findViewById(R.id.home_sort_item_pricetextview);
            danweiTexiview = (TextView) itemView.findViewById(R.id.home_sort_item_danweitview);
        }
    }
}
