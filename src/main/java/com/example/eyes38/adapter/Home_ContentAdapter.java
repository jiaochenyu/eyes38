package com.example.eyes38.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.activity.SortMenuActivity;
import com.example.eyes38.beans.HomeContent;
import com.example.eyes38.beans.HomeContentContent;

import java.util.List;

/**
 * Created by jqchen on 2016/5/17.
 */
public class Home_ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_ONE = 1;
    public static final int ITEM_TYPE_TWO = 2;
    //数据源
    List<HomeContent> mList;
    Context mContext;
    Home_ContentContentAdapter contentAdapter;
    Home_ContentnoneAdapter mContentnoneAdapter;


    public Home_ContentAdapter(Context mContext, List<HomeContent> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    //获取布局的类型种类，我用了两种

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? ITEM_TYPE_ONE : ITEM_TYPE_TWO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_ONE) {
            //绑定活动头部的视图
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detal_head, parent, false);
            ItemOneViewHolder itemOneViewHolder = new ItemOneViewHolder(view);
            return itemOneViewHolder;
        } else if (viewType == ITEM_TYPE_TWO) {
            //绑定活动的详细信息
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detail_content, parent, false);
            ItemTwoViewHolder itemTwoViewHolder = new ItemTwoViewHolder(view);
            return itemTwoViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemOneViewHolder) {
            ItemOneViewHolder one = (ItemOneViewHolder) holder;
            one.mTextView.setText(mList.get(position / 2).getName());
        } else if (holder instanceof ItemTwoViewHolder) {
            ItemTwoViewHolder two = (ItemTwoViewHolder) holder;
            //获得分类详细的内容，list集合
            List<HomeContentContent> list = mList.get((position - 1) / 2).getList();
            //初始化适配器
            if (list.size() > 0) {
                contentAdapter = new Home_ContentContentAdapter(list, mContext);
                //新建布局管理器
                GridLayoutManager grid = new GridLayoutManager(mContext, 2);
                //绑定布局器
                two.mRecyclerView.setLayoutManager(grid);
                //绑定适配器
                two.mRecyclerView.setAdapter(contentAdapter);
                contentAdapter.setmOnItemClickListener(new Home_ContentContentAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, HomeContentContent hcc) {
                        Intent intent = new Intent(mContext, SortMenuActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("values", hcc);
                        intent.putExtra("values", bundle);
                        mContext.startActivity(intent);
                    }
                });
            } else {
             mContentnoneAdapter = new Home_ContentnoneAdapter(list, mContext);
                //新建布局管理器
                GridLayoutManager grid = new GridLayoutManager(mContext, 2);
                //绑定布局器
                two.mRecyclerView.setLayoutManager(grid);
                //绑定适配器
                two.mRecyclerView.setAdapter(mContentnoneAdapter);
            }

            //对适配器进行监听单击事件

        }
    }

    @Override
    public int getItemCount() {
        return mList.size() * 2;
    }


    public class ItemOneViewHolder extends RecyclerView.ViewHolder {
        //头部专题的名字
        TextView mTextView;

        public ItemOneViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.zhuantiname);
        }
    }

    public class ItemTwoViewHolder extends RecyclerView.ViewHolder {
        //专题内容
        RecyclerView mRecyclerView;

        public ItemTwoViewHolder(View itemView) {
            super(itemView);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.home_content_content);
        }
    }
}
