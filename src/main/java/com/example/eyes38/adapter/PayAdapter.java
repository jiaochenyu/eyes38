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
import com.example.eyes38.beans.CartGoods;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.RequestQueue;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by jqchen on 2016/5/27.
 */
public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayHolder>  {
    private RequestQueue mRequestQueue;
    List<CartGoods> mList;
    Context mContext;

    public PayAdapter(List<CartGoods> list, Context context) {
        mList = list;
        mContext = context;

    }

    @Override
    public PayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.pay_order_goods_items,parent,false);
        PayHolder payHolder = new PayHolder(view);
        //view.setOnClickListener(this);
        return payHolder;
    }

    @Override
    public void onBindViewHolder(PayHolder holder, int position) {
        Glide.with(mContext).load(mList.get(position).getGoods().getPath()).into(holder.mImageView);
        holder.goodsName.setText(mList.get(position).getProduct_name());
        if(mList.get(position).getExtension1().equals("false")){
            holder.orderType.setText("当日订单");
        }else {
            holder.orderType.setText("当周订单"+mList.get(position).getExtension1());
        }

        holder.goodsNum.setText(mList.get(position).getQuantity()+"");
        //double类型保留两位小数
        DecimalFormat df = new DecimalFormat("0.00");
        String st = df.format(mList.get(position).getPrice() * mList.get(position).getQuantity());
        holder.listPrice.setText(st);
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }
    //***********获取数据 收货信息，配送费用
    private void getNoHttp(){
        mRequestQueue = NoHttp.newRequestQueue();
       // String path = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customer-addresses";

    }

    class PayHolder extends RecyclerView.ViewHolder{
        ImageView mImageView; // 图片
        TextView goodsName , orderType, listPrice, goodsNum ; // 商品名,订单类型，商品价钱，商品数量

        public PayHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.goodspicture);
            goodsName = (TextView) itemView.findViewById(R.id.goodstitle);
            orderType = (TextView) itemView.findViewById(R.id.ordertype);
            listPrice = (TextView) itemView.findViewById(R.id.pay_list_price);
            goodsNum = (TextView) itemView.findViewById(R.id.goodsNum);
        }
    }
}
