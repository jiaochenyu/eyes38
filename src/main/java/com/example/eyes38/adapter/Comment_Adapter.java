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

import com.bumptech.glide.Glide;
import com.example.eyes38.R;
import com.example.eyes38.activity.CommentReplyActivity;
import com.example.eyes38.beans.Comments;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by jqchen on 2016/5/20.
 */
public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.MyViewHolder> {
    private List<Comments> mList;
    private Context context;


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
        if (!mList.get(position).getPath().equals("")){
            Glide.with(context).load(mList.get(position).getPath())
                    .bitmapTransform(new CropCircleTransformation(context))
                    .error(R.mipmap.user_photo)
                    .into(holder.portraitImageView);
        }
        holder.comment_usernamTextView.setText(mList.get(position).getAuthor_name());
        holder.comment_contentTextView.setText(mList.get(position).getComment());
        holder.comment_timeTextView.setText(mList.get(position).getCreate_date());
        holder.mRatingBar.setRating(mList.get(position).getRating());
        holder.replyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentReplyActivity.class);
                intent.putExtra("values",mList.get(position));
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
        ImageView portraitImageView;
        RatingBar mRatingBar;
        TextView comment_usernamTextView, comment_contentTextView, comment_timeTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.comment_ratingbar);
            comment_usernamTextView = (TextView) itemView.findViewById(R.id.comment_username);
            comment_contentTextView = (TextView) itemView.findViewById(R.id.comment_content);
            comment_timeTextView = (TextView) itemView.findViewById(R.id.comment_time);
            replyImageView = (ImageView) itemView.findViewById(R.id.comment_reply);
            portraitImageView = (ImageView) itemView.findViewById(R.id.comment_portrait);
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
