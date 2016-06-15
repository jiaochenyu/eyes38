package com.example.eyes38.fragment.search.search_adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.activity.GoodDetailActivity;
import com.example.eyes38.beans.Goods;
import com.example.eyes38.fragment.search.search_bean.gridBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weixiao on 2016/5/28.
 */
public class GridAdapter extends BaseAdapter implements View.OnClickListener {
    private OnViewItemClickListener mOnItemClickListener = null;
    //上下文对象
    private Context mContext;
    List<Goods> mList;
    LayoutInflater mInflater;

    public GridAdapter(Context context, List<Goods> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
    }

    //监听接口OnRecyclerViewItemClickListener
    public static interface OnViewItemClickListener {
        void OnItemClick(View view, Goods goods);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        gridViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new gridViewHolder();
            convertView = mInflater.inflate(R.layout.search_grid_item, null);
            viewHolder.text = (TextView) convertView.findViewById(R.id.textview1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (gridViewHolder) convertView.getTag();
        }
        Goods goods = mList.get(position);
        String content = goods.getGoods_name();
        if (content.length() <= 4) {
            viewHolder.text.setText(content);
        } else {
            viewHolder.text.setText(content.substring(content.length() - 4, content.length() - 1));
        }
        viewHolder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到商品详情页面,传一个goods对象,键值是values,
                Goods mGoods = new Goods(mList.get(position).getGoods_id(), mList.get(position).getGoods_name(), mList.get(position).getPath(), mList.get(position).getGoods_unit(), mList.get(position).getGoods_market_price(), mList.get(position).getGoods_platform_price(), mList.get(position).getGoods_comment_count(), mList.get(position).getGoods_stock(), mList.get(position).getGoods_description());
                Intent intent = new Intent(mContext, GoodDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("values", mGoods);
                intent.putExtra("values", bundle);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.OnItemClick(v, (Goods) v.getTag());
        }
    }

    class gridViewHolder {
        TextView text;
    }
}
