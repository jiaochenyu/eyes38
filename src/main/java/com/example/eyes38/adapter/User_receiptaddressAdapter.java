package com.example.eyes38.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eyes38.R;
import com.example.eyes38.beans.ReceiptAddress;
import com.example.eyes38.user_activity.AddressInfo.User_modifyAddressActivity;
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
    public static final int DELETERECEIPTADDRESS = 1;  //删除收货地址
    public static final int SETDETAULTRECEIPTADDRESS = 2;  //删除收货地址
    public static final int DeleteMethod = 3;  //通知更新
    private Context mContext;
    private List<ReceiptAddress> mDatas;//存收货地址的list
    LayoutInflater mInflater;
    private RequestQueue mRequestQueue;
    private ReceiptAddress mReceipt;//收货地址的javabean
    private int position; // 删除位置
    private int modifyposition; // 删除位置
    Handler mHandler;
    private Toast toast;//用于快速更新呢的Toast
    private String head;//头信息

    public User_receiptaddressAdapter() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getModifyposition() {
        return modifyposition;
    }

    public void setModifyposition(int modifyposition) {
        this.modifyposition = modifyposition;
    }

    public User_receiptaddressAdapter(Context context, List<ReceiptAddress> datas, String header, Handler handler) {
        mContext = context;
        mDatas = datas;
        head = header;
        mHandler = handler;
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
        if (position == 0) {
            //默认地址时绑定的视图
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
            viewHolder.address_checkbox_default.setTag(0);
            viewHolder.address_checkbox_default.setChecked(true);//设置默认收货地址的checkbox为选中的状态
            viewHolder.add_address_default_name.setText(mReceipt.getFirstname());
            viewHolder.add_address_default_province.setText(mReceipt.getDistrict());
            viewHolder.add_address_default_tel.setText(mReceipt.getMobile());
            viewHolder.add_address_default_detail.setText(mReceipt.getAddress_1());
            viewHolder.address_checkbox_default.setOnCheckedChangeListener(new CheckBoxChangedListener());
            viewHolder.address_default_delete_image.setOnClickListener(new ItemOnClickListener(0));
            viewHolder.address_default_delete_text.setOnClickListener(new ItemOnClickListener(0));
            viewHolder.add_address_default_modify.setOnClickListener(new ItemOnClickListener(0));
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
            viewHolder.address_delete_image = (ImageView) convertView.findViewById(R.id.address_delete_image);
            viewHolder.address_delete_text = (TextView) convertView.findViewById(R.id.address_delete_text);
            viewHolder.add_address_modify = (LinearLayout) convertView.findViewById(R.id.add_address_modify);
            viewHolder.address_checkbox = (CheckBox) convertView.findViewById(R.id.address_checkbox);
            //把当前控件缓存到视图中
            mReceipt = mDatas.get(position);
            viewHolder.add_address_name.setText(mReceipt.getFirstname());
            viewHolder.add_address_province.setText(mReceipt.getDistrict());
            viewHolder.add_address_tel.setText(mReceipt.getMobile());
            viewHolder.add_address_detail.setText(mReceipt.getAddress_1());
            viewHolder.address_checkbox.setTag(position);
            viewHolder.address_checkbox.setOnCheckedChangeListener(new CheckBoxChangedListener());
            viewHolder.address_delete_image.setOnClickListener(new ItemOnClickListener(position));
            viewHolder.address_delete_text.setOnClickListener(new ItemOnClickListener(position));
            viewHolder.add_address_modify.setOnClickListener(new ItemOnClickListener(position));
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
                    //删除操作
                    setPosition(position);
                    showDeleteDialog();
                    break;
                case R.id.address_default_delete_text:
                    //删除操作
                    setPosition(position);
                    showDeleteDialog();
                    break;
                case R.id.address_delete_image:
                    //删除操作
                    setPosition(position);
                    showDeleteDialog();
                    break;
                case R.id.address_delete_text:
                    //删除操作
                    setPosition(position);
                    showDeleteDialog();
                    break;
                case R.id.add_address_default_modify:
                    //修改地址
                    setModifyposition(position);
                    modifyReceiptAddress();
                    break;
                case R.id.add_address_modify:
                    //修改地址
                    setModifyposition(position);
                    modifyReceiptAddress();
                    break;
            }
        }
    }

    //修改收货地址
    private void modifyReceiptAddress() {
        Intent intent = new Intent(mContext, User_modifyAddressActivity.class);
        intent.putExtra("modifyvalues", mDatas.get(getModifyposition()));
        mContext.startActivity(intent);
    }

    //CheckBox 选择改变监听器
    private class CheckBoxChangedListener implements CheckBox.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //取到单击的选中框的位置
            int position = (int) buttonView.getTag();
            //设置默认地址
            SetDefaultNoHttpMethod(position);

//            ReceiptAddress ra1 = mDatas.get(getPosition());
//            mDatas.remove(position);
//            mDatas.add(0, ra1);
//            notifyDataSetChanged();


        }
    }

    //设置默认地址操作
    private void SetDefaultNoHttpMethod(int position1) {
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customers/" + mDatas.get(position1).getCustomer_id() + "/default-address";
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.add("address_id", mDatas.get(position1).getAddress_id());
        Log.e("affaf", mDatas.get(position1).getAddress_id() + "|||" + position1);
        request.addHeader("Authorization", head); // 添加请求头
        mRequestQueue.add(SETDETAULTRECEIPTADDRESS, request, mOnResponseListener);
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
        mRequestQueue.add(DELETERECEIPTADDRESS, request, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == DELETERECEIPTADDRESS) {
                //从列表中移除要删除的元素,并通知刷新
                mDatas.remove(getPosition());
                notifyDataSetChanged();
            } else if (what == SETDETAULTRECEIPTADDRESS) {
                //设置默认地址,网络请求成功之后通知刷新
                String result = response.get();
                mHandler.sendEmptyMessage(DeleteMethod);
                showtoast("设置默认地址成功");

            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

    private void showtoast(String text) {
        if (toast == null) {
            toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

}
