package com.example.eyes38.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.beans.ReceiptAddress;

import java.util.List;

/**
 * Created by huangjiechun on 16/6/3.
 */
public class User_receiptaddressAdapter extends BaseAdapter {
    private Context mContext;
    private List<ReceiptAddress> mDatas;
    LayoutInflater mInflater;

    public User_receiptaddressAdapter(Context context, List<ReceiptAddress> datas) {
        mContext = context;
        mDatas = datas;
        mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
        convertView=mInflater.inflate(R.layout.add_address_list_item,null);
         viewHolder = new ViewHolder();
           //初始化
           viewHolder.add_address_name = (TextView) convertView.findViewById(R.id.add_address_name);
           viewHolder.add_address_province = (TextView) convertView.findViewById(R.id.add_address_province);
           viewHolder.add_address_detail = (TextView) convertView.findViewById(R.id.add_address_detail);
           viewHolder.add_address_tel = (TextView) convertView.findViewById(R.id.add_address_tel);
           viewHolder.add_address_modify = (LinearLayout) convertView.findViewById(R.id.add_address_modify);
           //把当前控件缓存到视图中
        ReceiptAddress ra=mDatas.get(position);
        viewHolder.add_address_name.setText(ra.getFirstname());
        viewHolder.add_address_province.setText(ra.getDistrict());
        viewHolder.add_address_tel.setText(ra.getMobile());
        viewHolder.add_address_detail.setText(ra.getAddress_1());
        viewHolder.add_address_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(mContext,)
            }
        });

        return convertView;
    }
class  ViewHolder{
    TextView add_address_name;   //名字
    TextView add_address_province;  //地点
    TextView add_address_detail; //小区名
    TextView add_address_tel;  //电话号码
    LinearLayout add_address_modify;
}
}
