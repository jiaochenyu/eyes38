package com.example.eyes38.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.beans.CommentReply;

import java.util.List;

/**
 * Created by jqchen on 2016/5/27.
 */
public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.CommentReplyViewHolder> {
    List<CommentReply> mList;

    public CommentReplyAdapter(List<CommentReply> mList) {
        this.mList = mList;
    }

    @Override
    public CommentReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_reply_item,parent,false);
        CommentReplyViewHolder holder = new CommentReplyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommentReplyViewHolder holder, int position) {
        holder.nameTextView.setText(mList.get(position).getName());
        holder.idTextView.setText(mList.get(position).getId()+"");
        holder.contentTextView.setText(mList.get(position).getContent());
        holder.timeTextView.setText(mList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CommentReplyViewHolder extends RecyclerView.ViewHolder {
        ImageView portraitImageView;//头像
        TextView nameTextView,idTextView,contentTextView,timeTextView;
        public CommentReplyViewHolder(View itemView) {
            super(itemView);
            portraitImageView = (ImageView) itemView.findViewById(R.id.comment_reply_portrait);
            nameTextView = (TextView) itemView.findViewById(R.id.comment_reply_username);
            idTextView = (TextView) itemView.findViewById(R.id.comment_reply_id);
            contentTextView = (TextView) itemView.findViewById(R.id.comment_reply_content);
            timeTextView = (TextView) itemView.findViewById(R.id.comment_reply_time);
        }
    }

}
