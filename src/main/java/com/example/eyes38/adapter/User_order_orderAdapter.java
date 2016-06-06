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
import com.example.eyes38.beans.UserOrderGoods;

import java.util.List;

/**
 * Created by weixiao on 2016/6/4.
 */
public class User_order_orderAdapter extends RecyclerView.Adapter<User_order_orderAdapter.MyViewHolder> implements View.OnClickListener {
    private List<UserOrderGoods> mList;
    private Context mContext;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    //定义监听接口
    //定义监听接口
    public static interface OnRecyclerViewItemClickListener {

        void onItemClick(View view, UserOrderGoods userOrderGoods);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public User_order_orderAdapter(List<UserOrderGoods> list, Context context) {
        mList = list;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_add_order_item2, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        //注册监听事件
        view.setOnClickListener(this);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(mContext).load(mList.get(position).getImage()).into(holder.add_order_image1);
        holder.add_order_name1.setText(mList.get(position).getName() + "");
        holder.add_order_price1.setText("¥ "+mList.get(position).getPrice());
        holder.add_order_num1.setText("× "+mList.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (UserOrderGoods) v.getTag());
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView add_order_image1;//物品图片
        TextView add_order_name1;//物品名称
        TextView add_order_price1; //物品单价
        TextView add_order_num1;//物品数量

        public MyViewHolder(View itemView) {
            super(itemView);
            add_order_image1 = (ImageView) itemView.findViewById(R.id.add_order_image);
            add_order_name1 = (TextView) itemView.findViewById(R.id.add_order_name);
            add_order_price1 = (TextView) itemView.findViewById(R.id.add_order_price);
            add_order_num1 = (TextView) itemView.findViewById(R.id.add_order_num);
        }
    }
}
