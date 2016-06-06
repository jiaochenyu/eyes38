package com.example.eyes38.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.beans.UserOrderBean;
import com.example.eyes38.beans.UserOrderGoods;
import com.example.eyes38.fragment.user.AllFragment;
import com.example.eyes38.user_activity.User_order_detailActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by weixiao on 2016/6/4.
 */
public class User_order_AllAdapter extends RecyclerView.Adapter<User_order_AllAdapter.ViewHolder> implements View.OnClickListener {
    //数据源
    List<UserOrderBean> mList;
    Context mContext;
    User_order_orderAdapter mSecondAdapter;//内部适配器
    private OnItemClickListener mOnItemClickListener = null;

    public User_order_AllAdapter(List<UserOrderBean> list, Context context) {
        mList = list;
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_add_order_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        //注册监听事件
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.create_date.setText(mList.get(position).getCreate_date() + "");
        if (mList.get(position).getOrder_status_id() == 16) {
            holder.order_status_id.setText("待付款");
        } else if (mList.get(position).getOrder_status_id() == 7) {
            holder.order_status_id.setText("订单取消");
        }
        holder.total_count.setText(mList.get(position).getTotal_count() + "");
        holder.total.setText("¥ " + mList.get(position).getTotal());
        List<UserOrderGoods> list = mList.get((position)).getmList();//获取内部图片的list集合
        GridLayoutManager manager = new GridLayoutManager(mContext, 1);
        holder.mheadRecyclView.setLayoutManager(manager);
        mSecondAdapter = new User_order_orderAdapter(list, mContext);
        holder.mheadRecyclView.setAdapter(mSecondAdapter);
        mSecondAdapter.setOnItemClickListener(new User_order_orderAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, UserOrderGoods userOrderGoods) {
                //在这里写订单详情点击事件
                Intent intent = new Intent(mContext, User_order_detailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("User_Order_Bean", mList.get(position));
                intent.putExtra("User_Order_Bean", bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //定义一个监听接口
    private static interface OnItemClickListener {
        void onItemClick(View view, UserOrderBean userOrderBean);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (UserOrderBean) v.getTag());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView create_date;//下单日期
        TextView order_status_id; //订单状态
        TextView total_count;//总共数量
        TextView total;//总共价格（支付金额）
        RecyclerView mheadRecyclView;

        public ViewHolder(View itemView) {
            super(itemView);
            create_date = (TextView) itemView.findViewById(R.id.add_order_date);
            order_status_id = (TextView) itemView.findViewById(R.id.add_order_state);
            total_count = (TextView) itemView.findViewById(R.id.add_order_total_count);
            total = (TextView) itemView.findViewById(R.id.add_order_total);
            mheadRecyclView = (RecyclerView) itemView.findViewById(R.id.order_item_recycle);
        }
    }

}
