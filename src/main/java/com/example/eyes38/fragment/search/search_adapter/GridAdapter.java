package com.example.eyes38.fragment.search.search_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.fragment.search.search_bean.gridBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weixiao on 2016/5/28.
 */
public class GridAdapter extends BaseAdapter {
    //上下文对象
    private Context mContext;
    List<gridBean> mList;
    LayoutInflater mInflater;

    public GridAdapter(Context context, List<gridBean> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        gridViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new gridViewHolder();
            convertView = mInflater.inflate(R.layout.search_grid_item, null);
            viewHolder.text = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (gridViewHolder) convertView.getTag();
        }
        gridBean gridBean = mList.get(position);
        String content = gridBean.getText();
        viewHolder.text.setText(content);

        return convertView;
    }

    class gridViewHolder {
        TextView text;
    }
}
