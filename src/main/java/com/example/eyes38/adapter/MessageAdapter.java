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
import com.example.eyes38.beans.MessageBean;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by JCY on 2016/6/14.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> implements View.OnClickListener, View.OnLongClickListener {
    List<MessageBean> mList;
    Context mContext;
    private MyItemLongClickListener mLongClickListener;
    private MyItemClickListener mClickListener = null;


    public MessageAdapter(List<MessageBean> list, Context context) {
        mList = list;
        mContext = context;
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        MessageHolder messageHolder = new MessageHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return messageHolder;
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        //holder.itemView.setTag(mList.get(position));
        holder.itemView.setTag(position);
        Glide.with(mContext).load(mList.get(position).getPath())
                .bitmapTransform(new CropCircleTransformation(mContext))
                .error(R.mipmap.user_photo)
                .into(holder.mImageView);
        holder.mTitle.setText(mList.get(position).getTitle());
        holder.mContent.setText(mList.get(position).getContent());
        holder.mTime.setText(mList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    public interface MyItemLongClickListener {
        //void OnItemLongClick(View view,MessageBean messageBean);
        void OnItemLongClick(View view, int position);
    }

    public interface MyItemClickListener {
        void OnItemClick(View view, MessageBean messageBean);
    }

    public void setLongClickListener(MyItemLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
    }

    public void setClickListener(MyItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        if (mClickListener != null) {
            mClickListener.OnItemClick(v, (MessageBean) v.getTag());
        }

    }

    @Override
    public boolean onLongClick(View v) {
        if (mLongClickListener != null) {
            mLongClickListener.OnItemLongClick(v, (Integer) v.getTag());
        }
        return true;
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTitle, mContent, mTime;

        public MessageHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.message_image);
            mTitle = (TextView) itemView.findViewById(R.id.message_title);
            mContent = (TextView) itemView.findViewById(R.id.message_content);
            mTime = (TextView) itemView.findViewById(R.id.message_time);
        }
    }

}
