package com.example.eyes38.fragment.search.search_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.fragment.search.search_bean.bansearchBean;

import java.util.List;

/**
 * Created by weixiao on 2016/5/27.
 */
public class ListAdapter extends BaseAdapter {
    List<bansearchBean> mList;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public ListAdapter(List<bansearchBean> list, Context context) {
        mList = list;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
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
        ViewHoleder holeder=null;
        if (convertView==null){
            holeder=new ViewHoleder();
            convertView=mLayoutInflater.inflate(R.layout.search_list_item,null);
            holeder.text=(TextView) convertView.findViewById(R.id.list_text);
            convertView.setTag(holeder);
        }else {
            holeder= (ViewHoleder) convertView.getTag();
        }

        bansearchBean bansearchBean=mList.get(position);
        String content=bansearchBean.getContent();
        holeder.text.setText(content);
        return convertView;
    }
    //viewHoleder用于缓存文件
    class ViewHoleder{
        TextView text;
    }
}
