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
import com.example.eyes38.beans.Consult;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by jqchen on 2016/6/14.
 */
public class CustomerServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int ITEM_TYPE_LEFT = 1;
    public static final int ITEM_TYPE_RIGHT = 2;
    //数据源
    List<Consult> mList;
    Context mContext;

    public CustomerServiceAdapter(List<Consult> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        return position%2==0? ITEM_TYPE_RIGHT : ITEM_TYPE_LEFT;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LEFT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customerservice_left,parent,false);
            return new ItemLeftViewHolder(view);
        }else if (viewType == ITEM_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customerservice_right,parent,false);
            return new ItemRightViewHolder(view);
        }
        return null;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemLeftViewHolder){
            ItemLeftViewHolder left = (ItemLeftViewHolder) holder;
            left.mTextView.setText(mList.get(position).getInfo());
        }else if (holder instanceof ItemRightViewHolder){
            ItemRightViewHolder right = (ItemRightViewHolder) holder;
            right.mTextView.setText(mList.get(position).getInfo());
            Glide.with(mContext).load(mList.get(position).getPath())
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(right.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public class ItemLeftViewHolder extends RecyclerView.ViewHolder {
        //机器人的内容
        TextView mTextView;
        public ItemLeftViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.service_serice_content);
        }
    }
    public class ItemRightViewHolder extends RecyclerView.ViewHolder {
        //用户的内容
        ImageView mImageView;
        TextView mTextView;
        public ItemRightViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.service_customer);
            mTextView = (TextView) itemView.findViewById(R.id.service_customer_content);
        }
    }

}
