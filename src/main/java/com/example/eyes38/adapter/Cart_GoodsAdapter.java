package com.example.eyes38.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.activity.GoodDetailActivity;
import com.example.eyes38.beans.CartGoods;
import com.example.eyes38.utils.CartDialogDelete;
import com.example.eyes38.utils.CartDialogSelectDate;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jqchen on 2016/5/20.
 */
public class Cart_GoodsAdapter extends RecyclerView.Adapter<Cart_GoodsAdapter.CartGoodsViewHolder> implements View.OnClickListener {
    public static final int NOTIFICHANGEPRICE = 383;  //通知购物车页面  改变总价格
    public static final int NOTIFICHANGEALLSELECTED = 384; //通知购物车页面 设置为全选
    public static final int mWHAT = 385;
    public static final int DELETEFINISH = 386;  // 删除操作
    public static final int ADDFINISH = 387; // 加法操作
    public static final int NOTIFILIST = 388; //购物车状态改变
    public static final int CARTGOODSCOUNT = 308; // 通知mainactivity 改变徽章
    public static final int GET = 1; //GET 请求方式
    public static final int POST = 2; //POST 请求方式
    public static final int DELETE = 3;//DELETE 请求方式
    public static final int PUT = 4; //PUT 请求方式
    private List<CartGoods> mList;
    private Context mContext;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;//监听事件
    private boolean isShowDelete; // 是否显示删除按钮
    private RequestQueue mRequestQueue; //请求队列
    private int position; // 删除位置
    private int cartGoodsCount;
    Handler mainHandler = (new MainActivity()).mainHandler; // 向MainActivity传值 改变徽章
    Handler mHandler;

    private Handler httpHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DELETEFINISH:
                    if ((Boolean) msg.obj) {
                        // 进行删除操作
                        deleteMethod();
                    } else {
                        Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ADDFINISH:
                    //加法操作
                    if ((Boolean)msg.obj == true) {
                        // 进行加法操作
                        addMethod();
                    } else {
                        Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                    }

            }

        }
    };

    //无参构造方法
    public Cart_GoodsAdapter() {
    }

    //生成构造
    public Cart_GoodsAdapter(List<CartGoods> mCartGoods, Context context, Handler handler) {
        mList = mCartGoods;
        mContext = context;
        mHandler = handler;
        //初始化数据
        initDate();
    }

    public boolean isShowDelete() {
        return isShowDelete;
    }

    public void setShowDelete(boolean showDelete) {
        //是否显示 删除按钮
        isShowDelete = showDelete;
    }



    //购物车总商品数量
    public int getCartGoodsCount() {
        return cartGoodsCount;
    }

    public void setCartGoodsCount(int cartGoodsCount) {
        this.cartGoodsCount = cartGoodsCount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    //监听事件接口 回调
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, CartGoods cartGoods);
    }

    //抽象方法
    @Override
    public void onClick(View v) {
        if (mOnRecyclerViewItemClickListener != null) {
            mOnRecyclerViewItemClickListener.onItemClick(v, (CartGoods) v.getTag());
        }
    }

    @Override
    public CartGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_goods_items, parent, false);
            CartGoodsViewHolder cartGoodsViewHolder = new CartGoodsViewHolder(view);
            view.setOnClickListener(this);
            return cartGoodsViewHolder;
    }
    //初始化 mOnRecyclerViewItemClickListener
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnRecyclerViewItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(CartGoodsViewHolder holder, int position) {
        //设置item Tag
        holder.itemView.setTag(mList.get(position));
        Glide.with(mContext).load(mList.get(position).getGoods().getPath()).into(holder.mImageView);
        //double类型保留两位小数
        DecimalFormat df = new DecimalFormat("0.00");
        String st = df.format(mList.get(position).getPrice() * mList.get(position).getQuantity()); //double 保留两位小数
        holder.mPriceTextView.setText(st);
        holder.mTitleTextView.setText(mList.get(position).getProduct_name());
        holder.mCountTextView.setText(mList.get(position).getQuantity() + "");
        //判断一周菜谱按钮是否显示： 三种状态 extension==null显示两个按钮,==false只显示当日订单,==true或者日期 显示两个按钮
        //默认状态是 当周订单显示灰色 当日订单显示主题色
        String extension = mList.get(position).getExtension1();
        if (extension==null){
            //默认状态
            holder.mDayOrder.setVisibility(View.VISIBLE);
            holder.mWeekOrder.setVisibility(View.VISIBLE);
        }else if(extension.equals("false")){
            holder.mDayOrder.setVisibility(View.VISIBLE);
            holder.mWeekOrder.setVisibility(View.GONE);
        }else if(extension.equals("true")) {
            //true
            holder.mDayOrder.setVisibility(View.VISIBLE);
            holder.mDayOrder.setBackgroundResource(R.color.text); // 将当日订单背景颜色显示为灰色
            holder.mWeekOrder.setVisibility(View.VISIBLE);
            holder.mWeekOrder.setBackgroundResource(R.color.topical); //将当周订单北京颜色显示主题色
        }else{
            holder.mDayOrder.setVisibility(View.VISIBLE);
            holder.mDayOrder.setBackgroundResource(R.color.text); // 将当日订单背景颜色显示为灰色
            holder.mWeekOrder.setVisibility(View.VISIBLE);
            holder.mWeekOrder.setBackgroundResource(R.color.topical); //将当周订单北京颜色显示主题色

        }
        // 做个判断 是否显示删除按钮
        if (isShowDelete) {
            //如果显示 删除
            holder.mDeleteTextView.setVisibility(View.VISIBLE);
            holder.mDayOrder.setVisibility(View.GONE);
            holder.mWeekOrder.setVisibility(View.GONE);
        } else {
            holder.mDeleteTextView.setVisibility(View.GONE);
            holder.mDayOrder.setVisibility(View.VISIBLE);
            holder.mWeekOrder.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);

        holder.mCheckBox.setChecked(mList.get(position).isSelected());
        holder.mCheckBox.setOnCheckedChangeListener(new CheckBoxChangedListener());
        holder.addButton.setOnClickListener(new ButtonOnClickListener(position));
        holder.subButton.setOnClickListener(new ButtonOnClickListener(position));
        holder.mDeleteTextView.setOnClickListener(new ButtonOnClickListener(position));
        holder.mImageView.setOnClickListener(new ButtonOnClickListener(position)); // 点击商品图片跳转
        holder.mTitleTextView.setOnClickListener(new ButtonOnClickListener(position)); // 点击商品标题进行跳转

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //  ViewHolder
    class CartGoodsViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;
        ImageView mImageView;
        TextView mTitleTextView, mPriceTextView, mCountTextView, mDeleteTextView;// 商品名称, 商品价格， 增减数量  ,删除按钮
        Button addButton, subButton, mDayOrder,mWeekOrder;

        public CartGoodsViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        //初始化布局
        private void initViews(View mView) {
            mCheckBox = (CheckBox) mView.findViewById(R.id.checkbox_list); // 选中
            mImageView = (ImageView) mView.findViewById(R.id.goodspicture); // 图片
            mTitleTextView = (TextView) mView.findViewById(R.id.goodstitle); // 商品名称
            mPriceTextView = (TextView) mView.findViewById(R.id.cart_list_price);  // 价钱
            mCountTextView = (TextView) mView.findViewById(R.id.goods_count);// 添加到购物车中商品的数量
            addButton = (Button) mView.findViewById(R.id.addbutton); // 增加商品
            subButton = (Button) mView.findViewById(R.id.minusbutton); // 减少商品
            mDayOrder = (Button) mView.findViewById(R.id.dayOrder); // 当日订单按钮
            mWeekOrder = (Button) mView.findViewById(R.id.weekOrder); // 当周订单按钮
            mDeleteTextView = (TextView) mView.findViewById(R.id.delete); //删除按钮
        }
    }


    /**
     * 对购物车进行操作
     */


    //初始化每一个item 设置为true 选中
    private void initDate() {
        //isSelected = new HashMap<>();
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setSelected(true);
        }
        if (isAllSelected()) {

            //通知改变总价格
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice()));
            //通知改变总数量
            mHandler.sendMessage(mHandler.obtainMessage(CARTGOODSCOUNT, getAllGoodsCount()));
            //如果商品全部被选中，则全选按钮也被 和 顶部全选按钮 默认为选中
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEALLSELECTED, isAllSelected()));
            //通知改变了选中状态 目的是向结算界面中传递选中的list集合
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFILIST, mList));
        }
        //mHandler.sendMessage(mHandler.obtainMessage(2, getTotalPrice()));
        //如果商品全部被选中，则全选按钮也被 默认为选中
        //mHandler.sendMessage(mHandler.obtainMessage(1, true));
    }

    //为加减按钮 删除 跳转到商品详情  设置监听器
    private class ButtonOnClickListener implements View.OnClickListener {

        int position;

        public ButtonOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.addbutton:
                    setPosition(position);
                    int shoppingCartId = mList.get(position).getShopping_cart_id();
                    String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart/"+shoppingCartId;
                    int quantity = mList.get(position).getQuantity();
                    String args = "quantity";
                    getNoHttpMethod(url, args, quantity, PUT,ADDFINISH);
                    break;
                case R.id.minusbutton:
                    //减法操作
                    if (mList.get(position).getQuantity() <= 1) {
                        Toast.makeText(mContext, "商品数量最少为1", Toast.LENGTH_SHORT).show();
                    } else {
                        mList.get(position).setQuantity(mList.get(position).getQuantity() - 1);
                        notifyDataSetChanged();
                        mHandler.sendMessage(mHandler.obtainMessage(CARTGOODSCOUNT, getAllGoodsCount())); // 显示总数量
                        mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice()));
                        //改变购物车上的徽章
                        mainHandler.sendMessage(mainHandler.obtainMessage(CARTGOODSCOUNT,getAllGoodsCount()));
                    }
                    break;
                case R.id.goodspicture:
                    //跳转到商品详情
                    Log.e("跳转到商品详情","点击了商品图片");
                    goGoodDetailActivity(position);
                    break;
                case R.id.goodstitle:
                    // 跳转到商品详情
                    goGoodDetailActivity(position);
                    Log.e("跳转到商品详情","点击了商品标题");
                    break;
                case R.id.weekOrder:
                    //当周订单 弹出dialog选择日期

                    break;
                case R.id.delete:
                    showDeleteDialog();
                    setPosition(position);
                    break;
            }
        }
    }

    //CheckBox 选择改变监听器
    private class CheckBoxChangedListener implements CheckBox.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (int) buttonView.getTag();
            CartGoods mCartGoods = mList.get(position);
            mCartGoods.setSelected(isChecked);

            mHandler.sendMessage(mHandler.obtainMessage(CARTGOODSCOUNT, getAllGoodsCount())); // 显示总数量
            //通知改变总价格 将总价格传给Handler
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice()));
            //如果商品全部被选中，则全选按钮也被 默认为选中
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEALLSELECTED, isAllSelected()));
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFILIST, mList));
        }
    }

    /**
     * 计算选中商品的金额
     *
     * @return
     */
    private float getTotalPrice() {
        CartGoods mCartGoods = null;
        float totalPrice = 0;
        for (int i = 0; i < mList.size(); i++) {
            //Log.e(i+"当前状态",mList.get(i).isSelected()+"");
            mCartGoods = mList.get(i);
            if (mCartGoods.isSelected()) {
                totalPrice += mCartGoods.getQuantity() * mCartGoods.getPrice();
            }
        }
        //Log.e("mlist.size()",totalPrice+""+mList.size());
        return totalPrice;
    }

    /**
     * 判断是否全被选中
     *
     * @return
     */
    private boolean isAllSelected() {
        boolean flag = true;
        for (int i = 0; i < mList.size(); i++) {
            if (!mList.get(i).isSelected()) {
                flag = false;
            }
        }
        return flag;
    }


    //删除的时候二次确认提示框
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
                int shoppingCartIds =mList.get(getPosition()).getShopping_cart_id();
                //进行删除网络请求
                String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart/?shoppingCartIds="+shoppingCartIds;
                String args = "shoppingCartIds ";
                Log.e("我要看看shoppingcartid", mList.get(getPosition()).getShopping_cart_id()+"");
                getNoHttpMethod(url, null, 0, DELETE,DELETEFINISH);
            }
        });
        builder.create().show();
    }

    private void addMethod() {
        //加法操作
        if (mList.get(getPosition()).getQuantity() >=  mList.get(getPosition()).getGoods().getGoods_stock()) {
            //如果大于库存
            Toast.makeText(mContext, "库存不足", Toast.LENGTH_SHORT).show();
        } else {
            mList.get(getPosition()).setQuantity(mList.get(getPosition()).getQuantity() + 1);
            notifyDataSetChanged();
            mainHandler.sendMessage(mainHandler.obtainMessage(CARTGOODSCOUNT,getAllGoodsCount())); //改变徽章
            mHandler.sendMessage(mHandler.obtainMessage(CARTGOODSCOUNT, getAllGoodsCount())); // 显示总数量
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice())); //显示总价格

        }

    }

    private void deleteMethod() {
        //删除操作
        mList.remove(getPosition());
        notifyDataSetChanged();
        //判断购物车是否为空如果为空显示空页面
        mHandler.sendMessage(mHandler.obtainMessage(CARTGOODSCOUNT, getAllGoodsCount())); // 显示总数量
        mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice())); //显示总价格
        mainHandler.sendMessage(mainHandler.obtainMessage(CARTGOODSCOUNT,getAllGoodsCount())); //改变徽章
    }

    // 统计购物车中 选中的 的数量
    private int getAllGoodsCount (){
        int count = 0;
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isSelected()){
                count += mList.get(i).getQuantity();
            }
        }
        return count;
    }
    //***********跳转到商品详情
    private void goGoodDetailActivity(int position){
        Intent intent = new Intent(mContext, GoodDetailActivity.class);
        Bundle bundle = new Bundle();
        Log.e("接收到的Goodsssssssssssssss",mList.get(position).getGoods().toString());
        bundle.putSerializable("values",mList.get(position).getGoods());
        intent.putExtra("values",bundle);
        //intent.putExtra("values",mList.get(position).getGoods());
        mContext.startActivity(intent);
    }


    // *************加法操作  删除操作  请求网络
    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == ADDFINISH) {
                String result = response.get();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean resultADD = jsonObject.getBoolean("success");
                    //请求完成返回结果 通知handler 处理结果
                    Log.e("加法返回结果", resultADD + "");
                    httpHandler.sendMessage(httpHandler.obtainMessage(ADDFINISH, resultADD));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(what == DELETEFINISH){
                String result = response.get();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean resultDelete = jsonObject.getBoolean("success");
                    String  resultString = jsonObject.getString("msg");
                    //请求完成返回结果 通知handler 处理结果
                    Log.e("购物车测试返回结果", resultDelete + "    "+resultString);
                    httpHandler.sendMessage(httpHandler.obtainMessage(DELETEFINISH, resultDelete));
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

    // 显示当周订单 选择日期dialog
    private void  showSelectDateDialog(){
        CartDialogSelectDate.Builder builder = new CartDialogSelectDate.Builder(mContext);
        builder.setMessage("当周订单");
        //设置接下来的一周日期：
        String[] date = setDate();
        builder.setDay1ButtonClick(date[0], new DialogInterface.OnClickListener() {
            //点击了第一天
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

       /* builder.setDay2ButtonClick(date[1],new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int shoppingCartIds =mList.get(getPosition()).getShopping_cart_id();
                //进行删除网络请求
                String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart/"+shoppingCartIds;
                String args = "shoppingCartIds ";
                Log.e("我要看看shoppingcartid", mList.get(getPosition()).getShopping_cart_id()+"");
                getNoHttpMethod(url, "", 8, DELETE,DELETEFINISH);
            }
        });*/


        //显示dialog
        builder.create().show();
    }
    //设置当周订单 日期选择框中的日期 （一周）
    private String[] setDate(){
        String[] date = null;
        Calendar c = Calendar.getInstance();
        for (int i = 0; i <7 ; i++) {
            date[i] =c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+1+"-"+c.get(Calendar.DATE);
            c.add(Calendar.DATE,1);
        }
        return date;
    }


    //提取Nohttp 代码

    /**
     * @param url                地址
     * @param arg1               请求参数
     * @param arg2               请求参数值
     * @param
     * @ x 请求方式 1.get 2.post，3.delete方式 4是 put方式
     */
    private void getNoHttpMethod(String url, String arg1, int arg2, int x,int what) {
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = null;
        switch (x) {
            case GET:
                request = NoHttp.createStringRequest(url, RequestMethod.GET);
                break;
            case POST:
                request = NoHttp.createStringRequest(url, RequestMethod.POST);
                break;
            case DELETE:
                request = NoHttp.createStringRequest(url, RequestMethod.DELETE);
                break;
            case PUT:
                request = NoHttp.createStringRequest(url, RequestMethod.PUT);
                break;
        }
        String username = "13091617887";  // 应该从偏好设置中获取账号密码
        String password = "123456";
        //Basic 账号+':'+密码  BASE64加密
        String addHeader = username + ":" + password;
        String authorization = "Basic " + new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));
        Log.e("authorization的值", authorization);
        request.addHeader("Authorization", authorization); // 添加请求头
        request.addHeader("Content-Type","application/json");
        request.setDefineRequestBodyForJson("{"+arg1+":"+arg2+"}");
        //request.add(arg1, arg2);
       // request.setHeader("Authorization",authorization);
        mRequestQueue.add(what, request, mOnResponseListener);
    }
}