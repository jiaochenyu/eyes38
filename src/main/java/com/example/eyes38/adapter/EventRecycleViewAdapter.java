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
import com.example.eyes38.beans.EventContentGood;

import java.util.List;

/**
 * Created by weixiao on 2016/5/25.
 */
public class EventRecycleViewAdapter extends RecyclerView.Adapter<EventRecycleViewAdapter.EventRecycleViewHolder>implements View.OnClickListener {
    private List<EventContentGood> mList;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener=null;
    private Context mContext;

    public EventRecycleViewAdapter(List<EventContentGood> list, Context context) {
        mList = list;
        mContext=context;
    }
    //实现点击事件
    @Override
    public void onClick(View v) {
        if (mOnRecyclerViewItemClickListener!=null){
            mOnRecyclerViewItemClickListener.OnItemClick(v, (EventContentGood) v.getTag());
        }

    }
    //实现点击事件


    //监听接口OnRecyclerViewItemClickListener
    public static interface OnRecyclerViewItemClickListener{
        void OnItemClick(View view,EventContentGood eventContentGood);
    }
    @Override
    public EventRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //定义一个view
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_content_item,parent,false);
       EventRecycleViewHolder eventRecycleViewHolder=new EventRecycleViewHolder(view);
        view.setOnClickListener(this);
        return eventRecycleViewHolder;
    }
    @Override
    public void onBindViewHolder(EventRecycleViewHolder holder, int position) {
        Glide.with(mContext).load(mList.get(position).getPic()).into(holder.mImageView);
        holder.mTextView.setText(mList.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //新建一个eventRecycleViewHolder类
    public class EventRecycleViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;

        public EventRecycleViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.event_content_image);
            mTextView = (TextView) itemView.findViewById(R.id.event_content_text);
        }
    }
}
