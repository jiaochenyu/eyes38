package com.example.eyes38.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eyes38.R;
import com.example.eyes38.beans.UserOrderBean;
import com.example.eyes38.beans.UserOrderGoods;
import com.example.eyes38.fragment.user.AllFragment;
import com.example.eyes38.fragment.user.PayFragment;
import com.example.eyes38.user_activity.User_order_detailActivity;
import com.example.eyes38.utils.CartDialogDelete;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * .
 * Created by weixiao on 2016/6/4.
 */
public class User_order_AllAdapter extends RecyclerView.Adapter<User_order_AllAdapter.ViewHolder> implements View.OnClickListener {
    //数据源
    List<UserOrderBean> mList;
    Context mContext;
    public static final int DELETEWHAT = 386;  // 删除操作
    private RequestQueue mRequestQueue; //请求队列
    User_order_orderAdapter mSecondAdapter;//内部适配器
    SharedPreferences sp;  //偏好设置 看用户登录是否登录
    private int setPosition;//取消位置
    private OnItemClickListener mOnItemClickListener = null;
    private ViewHolder mViewHolder;
    public User_order_AllAdapter(List<UserOrderBean> list, Context context) {
        mList = list;
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_add_order_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        sp = mContext.getSharedPreferences("userInfo", mContext.MODE_PRIVATE);
        //注册监听事件
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //设置取消订单按钮点击事件
        holder.order_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPosition = position;
                mViewHolder=holder;
                Log.e("setPosition", setPosition + "");
                showDeleteDialog();//点击订单取消

            }
        });
        holder.create_date.setText(mList.get(position).getCreate_date() + "");
        if (mList.get(position).getOrder_status_id() == 16) {
            holder.order_status_id.setText("待付款");
        } else if (mList.get(position).getOrder_status_id() == 7) {
            holder.order_status_id.setText("订单取消");
            holder.order_cancel.setVisibility(View.GONE);
            holder.pay_order.setVisibility(View.GONE);
        }
        holder.total_count.setText(mList.get(position).getTotal_count() + "");
        holder.total.setText("¥ " + mList.get(position).getTotal());
        List<UserOrderGoods> list = mList.get((position)).getmList();//获取内部图片的list集合
        GridLayoutManager manager = new GridLayoutManager(mContext, 1);
        holder.mheadRecyclView.setLayoutManager(manager);
        mSecondAdapter = new User_order_orderAdapter(list, mContext);
        holder.mheadRecyclView.setAdapter(mSecondAdapter);
        mSecondAdapter.setOnItemClickListener(new User_order_orderAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, UserOrderGoods userOrderGoods) {
                //在这里写订单详情点击事件
                Intent intent = new Intent(mContext, User_order_detailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("User_Order_Bean", mList.get(position));
                intent.putExtra("User_Order_Bean", bundle);
                mContext.startActivity(intent);
            }
        });
    }


    //点击删除订单
    private void showDeleteDialog() {
        CartDialogDelete.Builder builder = new CartDialogDelete.Builder(mContext);
        builder.setMessage("你确定要取消此订单吗？");
        builder.setNoButtonClick("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//隐藏弹窗
            }
        });
        builder.setYesButtonClick("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//先隐藏此窗口
                String order_id = mList.get(setPosition).getOrder_id();
                Log.e("order_id", order_id + "");
                //进行删除网络请求
                getDeleteNoHttpMethod(order_id);
            }
        });
        builder.create().show();
    }

    //**********账号密码进行base64加密
    private String authorization() {
        String username = sp.getString("USER_NAME", "");  // 应该从偏好设置中获取账号密码
        String password = sp.getString("PASSWORD", "");
        //Basic 账号+':'+密码  BASE64加密
        String addHeader = username + ":" + password;
        String authorization = "Basic " + new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));
        return authorization;
    }

    //请求网络删除请求
    private void getDeleteNoHttpMethod(String order_id) {
        String url = "http://38eye.test.ilexnet.com/api/mobile/order-api/orders/" + order_id;
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.DELETE);
        request.addHeader("Authorization", authorization()); // 添加请求头
        mRequestQueue.add(DELETEWHAT, request, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {

        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == DELETEWHAT) {
                //请求成功
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    boolean deleteOrder = object.getBoolean("success");
                    if (deleteOrder) {
                        Toast.makeText(mContext, "取消成功", Toast.LENGTH_SHORT).show();
                        mViewHolder.order_status_id.setText("订单取消");
                        mViewHolder.order_cancel.setVisibility(View.GONE);
                        mViewHolder.pay_order.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }

        @Override
        public void onFinish(int what) {

        }
    };


    @Override
    public int getItemCount() {
        return mList.size();
    }

    //定义一个监听接口
    private static interface OnItemClickListener {
        void onItemClick(View view, UserOrderBean userOrderBean);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (UserOrderBean) v.getTag());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView create_date;//下单日期
        TextView order_status_id; //订单状态
        TextView total_count;//总共数量
        TextView total;//总共价格（支付金额）
        RecyclerView mheadRecyclView;
        Button order_cancel, pay_order;//订单取消按钮

        public ViewHolder(View itemView) {
            super(itemView);
            create_date = (TextView) itemView.findViewById(R.id.add_order_date);
            order_status_id = (TextView) itemView.findViewById(R.id.add_order_state);
            total_count = (TextView) itemView.findViewById(R.id.add_order_total_count);
            total = (TextView) itemView.findViewById(R.id.add_order_total);
            mheadRecyclView = (RecyclerView) itemView.findViewById(R.id.order_item_recycle);
            order_cancel = (Button) itemView.findViewById(R.id.cancel_order);
            pay_order = (Button) itemView.findViewById(R.id.pay_order);
        }
    }


}
