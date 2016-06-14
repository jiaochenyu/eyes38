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
import com.example.eyes38.beans.SortTitle;

import java.util.List;

/**
 * Created by jqchen on 2016/5/16.
 */
public class Sort_TitleAdapter extends RecyclerView.Adapter<Sort_TitleAdapter.MyViewHolder> implements View.OnClickListener{
    //数据源
    private List<SortTitle> mList;
    Context mContext;
    //记录上一次选中的位置,默认为0，即点击第一行
    private int preid = 0;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    //定义监听接口
    public interface OnRecyclerViewItemClickListener{
        void onItemClick(View view, SortTitle sortTitle);
    }
    public void setmOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public Sort_TitleAdapter(List<SortTitle> mList,Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_title_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        //注册监听事件
        view.setOnClickListener(this);
        return myViewHolder;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView.setText(mList.get(position).getContent());
        Glide.with(mContext).load(mList.get(position).getPath()).into(holder.mImageView);

        //将对象保存在itemview的tag中，以便点击时进行获取
        holder.itemView.setTag(mList.get(position));
        //如果选中，背景颜色修改为content的背景颜色
        if (mList.get(position).isSeltcted()){
            //选中
            holder.itemView.setBackgroundResource(R.color.sort_content_background);
        }else {
            //未选中
            holder.itemView.setBackgroundResource(R.color.sort_title_background);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null){
            //getTag获取数据
            mOnItemClickListener.onItemClick(v, (SortTitle) v.getTag());
            int currid = ((SortTitle) v.getTag()).getId();
            if (currid != preid){
                //设置当前行选中
                mList.get(currid).setSeltcted(true);
                //设置上一次点击的行未选中
                mList.get(preid).setSeltcted(false);
                //刷新界面
                notifyItemChanged(currid);
                notifyItemChanged(preid);
                //刷新priid
                preid = currid;
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        //布局只有一行数据和图片
        TextView mTextView;
        ImageView mImageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            //使用自定义视图
            mTextView = (TextView) itemView.findViewById(R.id.tite_item);
            mImageView = (ImageView) itemView.findViewById(R.id.sort_title_image);
        }
    }
}
