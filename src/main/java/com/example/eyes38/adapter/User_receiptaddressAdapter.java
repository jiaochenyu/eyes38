package com.example.eyes38.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.beans.ReceiptAddress;
import com.example.eyes38.utils.CartDialogDelete;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.util.List;

/**
 * Created by huangjiechun on 16/6/3.
 */
public class User_receiptaddressAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private List<ReceiptAddress> mDatas;//存收货地址的list
    LayoutInflater mInflater;
    private RequestQueue mRequestQueue;
    private ReceiptAddress mReceipt;//收货地址的javabean
    private int position; // 删除位置
    private String head;//头信息
    private int mWhat = 123;

    public User_receiptaddressAdapter() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public User_receiptaddressAdapter(Context context, List<ReceiptAddress> datas, String header) {
        mContext = context;
        mDatas = datas;
        head = header;
        mInflater = LayoutInflater.from(mContext);
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
        if (position == 0) {//默认地址时绑定的视图
                final ViewHolder viewHolder;
                convertView = mInflater.inflate(R.layout.add_address_list_default_item, null);
                viewHolder = new ViewHolder();
                //初始化
                viewHolder.address_checkbox_default = (CheckBox) convertView.findViewById(R.id.address_checkbox_default);
                viewHolder.add_address_default_name = (TextView) convertView.findViewById(R.id.add_address_default_name);
                viewHolder.add_address_default_province = (TextView) convertView.findViewById(R.id.add_address_default_province);
                viewHolder.add_address_default_detail = (TextView) convertView.findViewById(R.id.add_address_default_detail);
                viewHolder.add_address_default_tel = (TextView) convertView.findViewById(R.id.add_address_default_tel);
                viewHolder.add_address_default_modify = (LinearLayout) convertView.findViewById(R.id.add_address_default_modify);
                viewHolder.address_default_delete_image = (ImageView) convertView.findViewById(R.id.address_default_delete_image);
                viewHolder.address_default_delete_text = (TextView) convertView.findViewById(R.id.address_default_delete_text);
                //把当前控件缓存到视图中
                mReceipt = mDatas.get(0);
                viewHolder.address_checkbox_default.setChecked(true);//设置默认收货地址的checkbox为选中的状态
                viewHolder.add_address_default_name.setText(mReceipt.getFirstname());
                viewHolder.add_address_default_province.setText(mReceipt.getDistrict());
                viewHolder.add_address_default_tel.setText(mReceipt.getMobile());
                viewHolder.add_address_default_detail.setText(mReceipt.getAddress_1());
                // viewHolder.address_checkbox_default.setOnCheckedChangeListener(new CheckBoxChangedListener());
            viewHolder.address_default_delete_image.setOnClickListener(new ItemOnClickListener(0));
//                viewHolder.address_default_delete_image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showDeleteDialog();
//                    }
//                });
                viewHolder.address_default_delete_text.setOnClickListener(new ItemOnClickListener(0));
//            viewHolder.add_address_default_modify.setOnClickListener(new ItemOnClickListener(0));
                return convertView;

        } else {//非默认地址时绑定的视图
            final ViewHolder viewHolder;
            convertView = mInflater.inflate(R.layout.add_address_list_item, null);
            viewHolder = new ViewHolder();
            //初始化
            viewHolder.add_address_name = (TextView) convertView.findViewById(R.id.add_address_name);
            viewHolder.add_address_province = (TextView) convertView.findViewById(R.id.add_address_province);
            viewHolder.add_address_detail = (TextView) convertView.findViewById(R.id.add_address_detail);
            viewHolder.add_address_tel = (TextView) convertView.findViewById(R.id.add_address_tel);
            viewHolder.add_address_modify = (LinearLayout) convertView.findViewById(R.id.add_address_modify);
            //把当前控件缓存到视图中
            mReceipt = mDatas.get(position);
            viewHolder.add_address_name.setText(mReceipt.getFirstname());
            viewHolder.add_address_province.setText(mReceipt.getDistrict());
            viewHolder.add_address_tel.setText(mReceipt.getMobile());
            viewHolder.add_address_detail.setText(mReceipt.getAddress_1());
            //  viewHolder.address_checkbox.setOnCheckedChangeListener(new CheckBoxChangedListener());
            viewHolder.address_delete_image.setOnClickListener(new ItemOnClickListener(position));
            viewHolder.address_delete_text.setOnClickListener(new ItemOnClickListener(position));
//            viewHolder.add_address_modify.setOnClickListener(new ItemOnClickListener(position));

            return convertView;

        }
    }

    @Override
    public void onClick(View v) {

    }

    class ViewHolder {
        CheckBox address_checkbox_default;//默认地址选择框
        CheckBox address_checkbox;//选择框
        TextView add_address_name;   //名字
        TextView add_address_default_name;   //默认地址名字
        TextView add_address_province;  //地点
        TextView add_address_default_province;  //默认地址地点
        TextView add_address_detail; //小区名
        TextView add_address_default_detail; //默认地址小区名
        TextView add_address_tel;  //电话号码
        TextView add_address_default_tel;  //默认地址电话号码
        ImageView address_default_delete_image;//默认地址删除图标
        TextView address_default_delete_text;//默认地址删除文字
        ImageView address_delete_image;//删除图标
        TextView address_delete_text;//删除文字
        LinearLayout add_address_modify;//列表
        LinearLayout add_address_default_modify;//默认地址列表

    }

    //设置删除按钮的单击事件
    private class ItemOnClickListener implements View.OnClickListener {
        int position;

        public ItemOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.address_default_delete_image:
                    setPosition(position);
                        showDeleteDialog();
                    break;
                case R.id.address_default_delete_text:
                    setPosition(position);
                    showDeleteDialog();
                    break;
                case R.id.address_delete_image:
                    setPosition(position);
                      showDeleteDialog();
                    break;
                case R.id.address_delete_text:
                    setPosition(position);
                     showDeleteDialog();
                    break;
            }
        }
    }

    //CheckBox 选择改变监听器
    private class CheckBoxChangedListener implements CheckBox.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (int) buttonView.getTag();
        }
    }


    //**************删除的时候二次确认提示框
    private void showDeleteDialog() {
        CartDialogDelete.Builder builder = new CartDialogDelete.Builder(mContext);
        builder.setMessage("确认要删除吗");
        builder.setNoButtonClick("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setYesButtonClick("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //进行删除网络请求
                getDeleteNoHttpMethod();
            }
        });
        builder.create().show();
    }

    //******** 删除操作 ***********
    private void getDeleteNoHttpMethod() {
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customer-addresses/" + mDatas.get(getPosition()).getAddress_id();
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.DELETE);
        request.addHeader("Authorization", head); // 添加请求头
        mRequestQueue.add(mWhat, request, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWhat) {
                String result = response.get();
                //如果
                mDatas.remove(getPosition());
                notifyDataSetChanged();
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }

        @Override
        public void onFinish(int what) {

        }
    };
}
