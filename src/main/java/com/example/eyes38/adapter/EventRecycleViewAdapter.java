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
import com.example.eyes38.beans.Goods;

import java.util.List;

/**
 * Created by weixiao on 2016/5/25.
 */
public class EventRecycleViewAdapter extends RecyclerView.Adapter<EventRecycleViewAdapter.EventRecycleViewHolder> implements View.OnClickListener {
    private List<Goods> mList;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;
    private Context mContext;

    public EventRecycleViewAdapter(List<Goods> list, Context context) {
        mList = list;
        mContext = context;
    }

    //实现点击事件
    @Override
    public void onClick(View v) {
        if (mOnRecyclerViewItemClickListener != null) {
            mOnRecyclerViewItemClickListener.OnItemClick(v, (Goods) v.getTag());
        }

    }
    //实现点击事件


    //监听接口OnRecyclerViewItemClickListener
    public interface OnRecyclerViewItemClickListener {
        void OnItemClick(View view, Goods goods);
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }
    @Override
    public EventRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //定义一个view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_content_item, parent, false);
        EventRecycleViewHolder eventRecycleViewHolder = new EventRecycleViewHolder(view);
        view.setOnClickListener(this);
        return eventRecycleViewHolder;
    }

    @Override
    public void onBindViewHolder(EventRecycleViewHolder holder, int position) {
        holder.mTextView.setText(mList.get(position).getGoods_name());
        Glide.with(mContext).load(mList.get(position).getPath()).into(holder.mImageView);
        holder.itemView.setTag(mList.get(position));
        holder.priceTextvew.setText(mList.get(position).getGoods_platform_price()+"");
        holder.danweiTexiview.setText(mList.get(position).getGoods_unit()+"");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //新建一个eventRecycleViewHolder类
    public class EventRecycleViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;//图片
        TextView mTextView;//商品名
        TextView priceTextvew;//价格
        TextView danweiTexiview;//单位

        public EventRecycleViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.event_imageview);
            mTextView = (TextView) view.findViewById(R.id.event_item_nametextview);
            priceTextvew = (TextView) view.findViewById(R.id.event_item_pricetextview);
            danweiTexiview = (TextView) view.findViewById(R.id.event_item_danweitview);
        }
    }
}
