package com.example.eyes38.fragment.search.search_adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eyes38.R;
import com.example.eyes38.activity.GoodDetailActivity;
import com.example.eyes38.beans.Goods;

import java.util.List;

/**
 * Created by weixiao on 2016/5/25.
 */
public class SearchRecycleViewAdapter extends RecyclerView.Adapter<SearchRecycleViewAdapter.EventRecycleViewHolder> implements View.OnClickListener {
    private List<Goods> mList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private Context mContext;

    public SearchRecycleViewAdapter(List<Goods> list, Context context) {
        mList = list;
        mContext = context;
    }

    //实现点击事件
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.OnItemClick(v, (Goods) v.getTag());
        }

    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    //监听接口OnRecyclerViewItemClickListener
    public static interface OnRecyclerViewItemClickListener {
        void OnItemClick(View view, Goods goods);
    }

    @Override
    public EventRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //定义一个view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_menu_item, parent, false);
        EventRecycleViewHolder eventRecycleViewHolder = new EventRecycleViewHolder(view);
        view.setOnClickListener(this);
        return eventRecycleViewHolder;
    }

    @Override
    public void onBindViewHolder(EventRecycleViewHolder holder, final int position) {
        Glide.with(mContext).load(mList.get(position).getPath()).into(holder.pic);
        holder.name.setText(mList.get(position).getGoods_name());
        holder.price.setText(mList.get(position).getGoods_platform_price() + "");
        holder.size.setText(mList.get(position).getGoods_unit());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置搜索物品点击事件
                // 跳转到商品详情页面,传一个goods对象,键值是values,
                Goods mGoods = new Goods(mList.get(position).getGoods_id(), mList.get(position).getGoods_name(), mList.get(position).getPath(), mList.get(position).getGoods_unit(), mList.get(position).getGoods_market_price(), mList.get(position).getGoods_platform_price(), mList.get(position).getGoods_comment_count(), mList.get(position).getGoods_stock(), mList.get(position).getGoods_description());
                Intent intent = new Intent(mContext, GoodDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("values", mGoods);
                intent.putExtra("values", bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //新建一个eventRecycleViewHolder类
    public class EventRecycleViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView name, price, size;
        View mView;

        public EventRecycleViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            pic = (ImageView) itemView.findViewById(R.id.sort_sort_item_imageview);
            name = (TextView) itemView.findViewById(R.id.sort_sort_item_nametextview);
            price = (TextView) itemView.findViewById(R.id.sort_sort_item_pricetextview);
            size = (TextView) itemView.findViewById(R.id.sort_sort_item_unittextview);
        }
    }
}
