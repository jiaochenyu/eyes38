package com.example.eyes38.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.beans.Comments;

import java.util.List;

/**
 * Created by jqchen on 2016/5/20.
 */
public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.MyViewHolder> {
    List<Comments> mList;
    public Comment_Adapter(List<Comments> mList) {
        this.mList = mList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.comment_usernamTextView.setText(mList.get(position).getId() + "");
        holder.comment_contentTextView.setText(mList.get(position).getContent());
        holder.comment_timeTextView.setText(mList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView comment_usernamTextView, comment_contentTextView, comment_timeTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            comment_usernamTextView = (TextView) itemView.findViewById(R.id.comment_username);
            comment_contentTextView = (TextView) itemView.findViewById(R.id.comment_content);
            comment_timeTextView = (TextView) itemView.findViewById(R.id.comment_time);
        }
    }

    //刷新添加数据
    public void addItem(List<Comments> newDatas) {
        newDatas.addAll(mList);
        mList.removeAll(mList);
        mList.addAll(newDatas);
        notifyDataSetChanged();
    }

    //加载数据
    public void addMoreItem(List<Comments> newDatas) {
        mList.addAll(newDatas);
        notifyDataSetChanged();
    }
}
