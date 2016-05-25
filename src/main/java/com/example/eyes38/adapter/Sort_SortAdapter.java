package com.example.eyes38.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * Created by jqchen on 2016/5/18.
 */
public class Sort_SortAdapter extends RecyclerView.Adapter<Sort_SortAdapter.MyViewHolder> implements View.OnClickListener{
    List<Goods> mList;
    Context mContext;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    //定义监听接口
    public static interface OnRecyclerViewItemClickListener{

        void onItemClick(View view, Goods goods);
    }
    public void setmOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
    public Sort_SortAdapter(Context mContext, List<Goods> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sort_sortmenu_item,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //绑定数据
<<<<<<< HEAD
        Glide.with(mContext).load(mList.get(position).getPath()).into(holder.mImageView);
=======
        holder.mImageView.setImageURI(mList.get(position));
>>>>>>> 60a4f3967656157dfb4f23b349000cbcab04a36c
        holder.nameTextView.setText(mList.get(position).getGoods_name());
        holder.priceTextView.setText(mList.get(position).getGoods_platform_price()+"");
        holder.unitTextView.setText(mList.get(position).getGoods_unit());
        holder.itemView.setTag(mList.get(position));
        //对商品中购物车按钮监听
        holder.carImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("jqchen","点击了购物车按钮");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v, (Goods) v.getTag());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView,carImageView;
        TextView nameTextView,priceTextView,unitTextView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.sort_sort_item_imageview);
            carImageView = (ImageView) itemView.findViewById(R.id.sort_sort_item_carimageview);
            nameTextView = (TextView) itemView.findViewById(R.id.sort_sort_item_nametextview);
            priceTextView = (TextView) itemView.findViewById(R.id.sort_sort_item_pricetextview);
            unitTextView = (TextView) itemView.findViewById(R.id.sort_sort_item_unittextview);
        }
    }
}
