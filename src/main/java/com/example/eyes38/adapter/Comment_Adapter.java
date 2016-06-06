package com.example.eyes38.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.activity.CommentReplyActivity;
import com.example.eyes38.beans.Comments;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jqchen on 2016/5/20.
 */
public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.MyViewHolder> {
    List<Comments> mList;
    Context context;


    public Comment_Adapter(List<Comments> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.comment_usernamTextView.setText(mList.get(position).getName());
        holder.comment_contentTextView.setText(mList.get(position).getContent());
        holder.comment_timeTextView.setText(mList.get(position).getTime());
        holder.mRatingBar.setRating(mList.get(position).getRatingbar());
        holder.replyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentReplyActivity.class);
                intent.putExtra("values", (Serializable) mList.get(position).getReplyList());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView replyImageView;
        //用户头像，暂时未用到
//        ImageView portraitImageView;
        RatingBar mRatingBar;
        TextView comment_usernamTextView, comment_contentTextView, comment_timeTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.comment_ratingbar);
            comment_usernamTextView = (TextView) itemView.findViewById(R.id.comment_username);
            comment_contentTextView = (TextView) itemView.findViewById(R.id.comment_content);
            comment_timeTextView = (TextView) itemView.findViewById(R.id.comment_time);
            replyImageView = (ImageView) itemView.findViewById(R.id.comment_reply);
        }
    }
    //已经弃用
    /*//刷新添加数据
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
    }*/
}
