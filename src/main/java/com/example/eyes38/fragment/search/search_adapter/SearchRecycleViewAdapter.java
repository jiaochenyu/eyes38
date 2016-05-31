package com.example.eyes38.fragment.search.search_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eyes38.R;
import com.example.eyes38.fragment.search.search_bean.SearchBean;

import java.util.List;

/**
 * Created by weixiao on 2016/5/25.
 */
public class SearchRecycleViewAdapter extends RecyclerView.Adapter<SearchRecycleViewAdapter.EventRecycleViewHolder>implements View.OnClickListener {
    private List<SearchBean> mList;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener=null;
    private Context mContext;

    public SearchRecycleViewAdapter(List<SearchBean> list, Context context) {
        mList = list;
        mContext=context;
    }
    //实现点击事件
    @Override
    public void onClick(View v) {
        if (mOnRecyclerViewItemClickListener!=null){
            mOnRecyclerViewItemClickListener.OnItemClick(v, (SearchBean) v.getTag());
        }

    }
    //实现点击事件


    //监听接口OnRecyclerViewItemClickListener
    public static interface OnRecyclerViewItemClickListener{
        void OnItemClick(View view, SearchBean searchBean);
    }
    @Override
    public EventRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //定义一个view
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_menu_item,parent,false);
       EventRecycleViewHolder eventRecycleViewHolder=new EventRecycleViewHolder(view);

        view.setOnClickListener(this);
        return eventRecycleViewHolder;
    }
    @Override
    public void onBindViewHolder(EventRecycleViewHolder holder, int position) {
        Log.e("ASD",mList.get(position).getName());
        Glide.with(mContext).load(mList.get(position).getPic()).into(holder.pic);
        holder.name.setText(mList.get(position).getName());
       holder.price.setText(mList.get(position).getPrice()+"");
        holder.size.setText(mList.get(position).getSize());
        Log.e("TAG",mList.get(position).getSize());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //新建一个eventRecycleViewHolder类
    public class EventRecycleViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView name,price,size;

        public EventRecycleViewHolder(View itemView) {
            super(itemView);
            pic= (ImageView) itemView.findViewById(R.id.sort_sort_item_imageview);
            name= (TextView) itemView.findViewById(R.id.sort_sort_item_nametextview);
            price= (TextView) itemView.findViewById(R.id.sort_sort_item_pricetextview);
            size= (TextView) itemView.findViewById(R.id.sort_sort_item_unittextview);
        }
    }
}
