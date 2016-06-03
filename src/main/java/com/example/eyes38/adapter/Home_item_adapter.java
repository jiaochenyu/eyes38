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
import com.example.eyes38.beans.HomeContent;
import com.example.eyes38.beans.HomeContentContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjiechun on 16/5/23.
 */
public class Home_item_adapter extends RecyclerView.Adapter<Home_item_adapter.ViewHolder> implements View.OnClickListener{
    public List<HomeContent> datas = null;
    Context mContext;
    String sortname;
    List<HomeContentContent> mlist;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (HomeContentContent) v.getTag());
        }
    }

    //定义监听接口
    public static interface OnRecyclerViewItemClickListener {

        void onItemClick(View view, HomeContentContent hcc);
    }


    public void setmOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public Home_item_adapter(List<HomeContent> datas, Context context, String sortname) {
        this.datas = datas;
        this.mContext = context;
        this.sortname = sortname;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_sort_content_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        initData();

        holder.mTextView.setText(mlist.get(position).getName());
        Glide.with(mContext).load(mlist.get(position).getImage()).into(holder.mImageView);
        holder.itemView.setTag(mlist.get(position));
        holder.priceTextvew.setText(mlist.get(position).getPrice()+"");
        holder.danweiTexiview.setText(mlist.get(position).getExtension4()+"");
    }

    private void initData() {
        mlist = new ArrayList<>();
        for (int i = 0; i <datas.size() ; i++) {
            if (datas.get(i).getName().equals(sortname)){
                mlist=datas.get(i).getList();
            }
        }

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.get(0).getList().size();
    }

    //自定义的ViewHolder，里面有每个Item的的所有界面元素,home_content_content_item这个xml文件中
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;//图片
        TextView mTextView;//商品名
        TextView priceTextvew;//价格
        TextView danweiTexiview;//单位

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.home_imageview);
            mTextView = (TextView) view.findViewById(R.id.home_sort_item_nametextview);
            priceTextvew = (TextView) view.findViewById(R.id.home_sort_item_pricetextview);
            danweiTexiview = (TextView) view.findViewById(R.id.home_sort_item_danweitview);
        }
    }
}