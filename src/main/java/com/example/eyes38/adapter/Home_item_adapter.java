package com.example.eyes38.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.eyes38.R;
import com.example.eyes38.beans.HomeGrid;

import java.util.List;

/**
 * Created by huangjiechun on 16/5/23.
 */
public class Home_item_adapter extends RecyclerView.Adapter<Home_item_adapter.ViewHolder> {
public List<HomeGrid> datas = null;
public Home_item_adapter(List<HomeGrid> datas) {
        this.datas = datas;
        }
//创建新View，被LayoutManager所调用
@Override
public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_detal_head_item,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
        }
//将数据与界面进行绑定的操作
@Override
public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mImageView.setImageResource(datas.get(position).getPic());
        }
//获取数据的数量
@Override
public int getItemCount() {
        return datas.size();
        }
//自定义的ViewHolder，持有每个Item的的所有界面元素
public static class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView mImageView;
    public ViewHolder(View view){
        super(view);
        mImageView = (ImageView) view.findViewById(R.id.home_grid_iv);
    }
}
}