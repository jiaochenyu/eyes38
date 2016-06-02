package com.example.eyes38.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.eyes38.EventActivity.EventActivity;
import com.example.eyes38.R;
import com.example.eyes38.activity.GoodDetailActivity;
import com.example.eyes38.activity.home.HomexptjActivity;
import com.example.eyes38.activity.home.HomezhuantiActivity;
import com.example.eyes38.beans.Goods;
import com.example.eyes38.beans.HomeContent;
import com.example.eyes38.beans.HomeContentContent;

import java.util.List;

/**
 * Created by jqchen on 2016/5/17.
 */
public class Home_ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    public static final int ITEM_TYPE_ONE = 1;
    public static final int ITEM_TYPE_TWO = 2;
    //数据源
    List<HomeContent> mList;
    Context mContext;
    Home_ContentContentAdapter contentAdapter;
    Home_ContentnoneAdapter mContentnoneAdapter;
    Home_Head_item_Adapter mHome_headAdapter;
    private OnMoreItemClickListener mOnItemClickListener = null;
    LinearLayout mLinearLayout;
    String name;

    public Home_ContentAdapter(Context mContext, List<HomeContent> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (HomeContent) v.getTag());
        }
    }

    //定义监听接口
    public static interface OnMoreItemClickListener {

        void onItemClick(View view, HomeContent hc);
    }

    public void setmOnItemClickListener(OnMoreItemClickListener listener) {
        this.mOnItemClickListener = listener;
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
            //获取专题的名字,数据源
            name = mList.get(position / 2).getName();
            mHome_headAdapter = new Home_Head_item_Adapter(name, mContext);
            one.mheadRecyclView.setLayoutManager(new LinearLayoutManager(mContext));
            one.mheadRecyclView.setAdapter(mHome_headAdapter);
          List<HomeContentContent> list = mList.get(position / 2).getList();
            final int size = list.size();
            //记录这个专题有没有内容
            mHome_headAdapter.setOnMoreClickListener(new Home_Head_item_Adapter.OnMoreClickListener() {
                @Override
                public void onItemClick(View view, String homeContent) {
                    if (size > 0) {
                        //有内容,判断是不是一周菜谱(一周菜谱的布局不一样)
                        if (homeContent.equals("一周菜谱")) {
                            Intent intent = new Intent(mContext, EventActivity.class);
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, HomezhuantiActivity.class);
                            intent.putExtra("values", homeContent);
                            mContext.startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(mContext, HomexptjActivity.class);
                        intent.putExtra("values", homeContent);
                        mContext.startActivity(intent);
                    }
                }
            });


        } else if (holder instanceof ItemTwoViewHolder) {
            ItemTwoViewHolder two = (ItemTwoViewHolder) holder;
            //获得分类详细的内容，list集合
            List<HomeContentContent> list = mList.get((position - 1) / 2).getList();
            //初始化适配器
            //判断是否有商品
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
                        //跳转到商品详情页面,传一个goods对象,键值是values,
                        Goods goods = new Goods(0, hcc.getName(), hcc.getImage(), null, null, null, null, null, 0, 0, 0, 0, 0);
                        Intent intent = new Intent(mContext, GoodDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("values", goods);
                        intent.putExtra("values", bundle);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                //没有商品的情况下,只需要传递一个
                mContentnoneAdapter = new Home_ContentnoneAdapter(mContext);
                //新建布局管理器
                LinearLayoutManager linear = new LinearLayoutManager(mContext);
                //绑定布局器
                two.mRecyclerView.setLayoutManager(linear);
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
        //头部专题的布局
        RecyclerView mheadRecyclView;

        public ItemOneViewHolder(View itemView) {
            super(itemView);
            mheadRecyclView = (RecyclerView) itemView.findViewById(R.id.home_zhuanti_head);

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
