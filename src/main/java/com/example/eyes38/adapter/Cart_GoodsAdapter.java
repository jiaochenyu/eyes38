package com.example.eyes38.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
public class Cart_GoodsAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    public static final int NOTIFICHANGEPRICE = 383;  //通知购物车页面  改变总价格
    public static final int NOTIFICHANGEALLSELECTED = 384; //通知购物车页面 设置为全选
    public static final int DELETEFINISH = 386;  // 删除操作
    public static final int ADDFINISH = 387; // 加法操作
    public static final int MINUSFINISH = 389; // 减法操作
    public static final int NOTIFILIST = 388; //购物车状态改变
    public static final int CARTGOODSCOUNT = 308; // 通知mainactivity 改变徽章
    public static final int UPDATEWEEKFINISH = 390; //更新购物车操作 设置当周订单的日期
    public static final int UPDATDAYFINISH = 391; //更新购物车操作 设置为当日订单（将extension设置为true）
    public static final int DeleteMethod = 520;
    Toast mToast; //优化吐司
    private List<CartGoods> mList;
    private Context mContext;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;//监听事件
    private boolean isShowDelete; // 是否显示删除按钮
    private RequestQueue mRequestQueue; //请求队列
    private int position; // 删除位置
    private int cartGoodsCount; //购物车总量
    SharedPreferences sp;  //偏好设置 看用户登录是否登录
    Handler mainHandler = (new MainActivity()).mainHandler; // 向MainActivity传值 改变徽章
    Handler mHandler;

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
    public int getItemViewType(int position) {
        return mList.get(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_goods_items, parent, false);
                holder = new CartGoodsViewHolder(view);
                view.setOnClickListener(this);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_goods_top_items, parent, false);
                holder = new CartTopViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CartTopViewHolder) {
            CartTopViewHolder cartTopViewHolder = (CartTopViewHolder) viewHolder;

            cartTopViewHolder.mCountTopTextView.setText(getAllGoodsCount() + "");
            cartTopViewHolder.mTopAllGoodsCheckBox.setChecked(isAllSelected());

            cartTopViewHolder.mTopAllGoodsCheckBox.setOnClickListener(new ButtonOnClickListener(position));
            cartTopViewHolder.mDeleteOperationTV.setOnClickListener(new ButtonOnClickListener(position));
        }
        if (viewHolder instanceof CartGoodsViewHolder) {
            CartGoodsViewHolder holder = (CartGoodsViewHolder) viewHolder;
            //设置item Tag
            holder.itemView.setTag(mList.get(position));
            Glide.with(mContext).load(mList.get(position).getGoods().getPath()).into(holder.mImageView);
            //double类型保留两位小数
            DecimalFormat df = new DecimalFormat("0.00");
            String st = df.format(mList.get(position).getPrice() * mList.get(position).getQuantity()); //double 保留两位小数
            holder.mPriceTextView.setText(st);
            holder.mTitleTextView.setText(mList.get(position).getProduct_name());
            holder.mCountTextView.setText(mList.get(position).getQuantity() + "");
            String st2 = df.format(mList.get(position).getPrice());
            holder.mItemPrice.setText(st2);
            /**
             *  判断一周菜谱按钮是否显示：四种状态
             *  extension==null显示两个按钮,
             *  ==false只显示当日订单,
             *  ==true 显示两个按钮  一周订单背景为灰色 当日订单背景为主题色
             *  如果为日期 那么一周菜谱按钮显示日期  当日订单背景为灰色
             */
            //默认状态是 当周订单显示灰色 当日订单显示主题色
            String extension = mList.get(position).getExtension1();
            if (extension.equals("false")) {
                //false
                holder.mDayOrder.setVisibility(View.VISIBLE);
                holder.mWeekOrder.setVisibility(View.GONE);
            } else if (extension.equals("")) {
                holder.mDayOrder.setVisibility(View.VISIBLE);
                holder.mDayOrder.setBackgroundResource(R.color.border_color); //将当日订单背景颜色显示为灰色
                holder.mWeekOrder.setVisibility(View.VISIBLE);
                holder.mWeekOrder.setBackgroundResource(R.color.topical); //将当周订单北京颜色显示主题色
            } else if (extension.equals("true")) {
                holder.mDayOrder.setVisibility(View.VISIBLE);
                holder.mDayOrder.setBackgroundResource(R.color.topical); //将当日订单背景颜色显示为灰色
                holder.mWeekOrder.setVisibility(View.VISIBLE);
                holder.mWeekOrder.setBackgroundResource(R.color.border_color); //将当周订单北京颜色显示主题色
                holder.mWeekOrder.setText("当周订单");
            } else {
                // 一周菜谱按钮显示日期
                holder.mDayOrder.setVisibility(View.VISIBLE);
                holder.mDayOrder.setBackgroundResource(R.color.border_color); //将当日订单背景颜色显示为灰色
                holder.mWeekOrder.setVisibility(View.VISIBLE);
                holder.mWeekOrder.setBackgroundResource(R.color.topical); //将当周订单北京颜色显示主题色
                holder.mWeekOrder.setText(extension);
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
            }
            holder.mCheckBox.setTag(position);

            holder.mCheckBox.setChecked(mList.get(position).isSelected());
            holder.mCheckBox.setOnCheckedChangeListener(new CheckBoxChangedListener());
            holder.mCheckBox.setOnClickListener(new ButtonOnClickListener(position));
            holder.addButton.setOnClickListener(new ButtonOnClickListener(position));
            holder.subButton.setOnClickListener(new ButtonOnClickListener(position));
            holder.mDeleteTextView.setOnClickListener(new ButtonOnClickListener(position));
            holder.mImageView.setOnClickListener(new ButtonOnClickListener(position)); // 点击商品图片跳转
            holder.mTitleTextView.setOnClickListener(new ButtonOnClickListener(position)); // 点击商品标题进行跳转
            holder.mWeekOrder.setOnClickListener(new ButtonOnClickListener(position)); // 点击一周菜谱
            holder.mDayOrder.setOnClickListener(new ButtonOnClickListener(position)); // 点击了当日订单
        }
    }

    //初始化 mOnRecyclerViewItemClickListener
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnRecyclerViewItemClickListener = listener;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    //  ViewHolder
    class CartGoodsViewHolder extends RecyclerView.ViewHolder {
        //显示商品item
        CheckBox mCheckBox;
        ImageView mImageView;
        TextView mTitleTextView, mPriceTextView, mCountTextView, mDeleteTextView,mItemPrice;// 商品名称, 商品价格， 增减数量  ,删除按钮,单价
        Button addButton, subButton, mDayOrder, mWeekOrder;

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
            mItemPrice  = (TextView) mView.findViewById(R.id.item_price); // 单价
        }
    }

    //显示顶部item  全选 编辑
    class CartTopViewHolder extends RecyclerView.ViewHolder {
        TextView mCountTopTextView;  // n件商品
        TextView mDeleteOperationTV;// 编辑删除操作
        CheckBox mTopAllGoodsCheckBox; // 全选含有优惠信息商品 的checkbox

        public CartTopViewHolder(View itemView) {
            super(itemView);
            mCountTopTextView = (TextView) itemView.findViewById(R.id.checkedCountTop); // 顶部显示选中数量文本框
            mDeleteOperationTV = (TextView) itemView.findViewById(R.id.DeleteOperationTV);  //编辑按钮
            mTopAllGoodsCheckBox = (CheckBox) itemView.findViewById(R.id.allGoodsCheckboxTop); // 顶部全选框
        }
    }


    /**
     * 对购物车进行操作
     */

    //初始化每一个item 设置为true 选中
    private void initDate() {
        sp = mContext.getSharedPreferences("userInfo", mContext.MODE_PRIVATE);
        for (int i = 1; i < mList.size(); i++) {
            mList.get(i).setSelected(true);
        }
        if (isAllSelected()) {
            //通知改变总价格
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice()));
            //通知改变总数量
            //mHandler.sendMessage(mHandler.obtainMessage(CARTGOODSCOUNT, getAllGoodsCount()));
            //如果商品全部被选中，则全选按钮也被 和 顶部全选按钮 默认为选中
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEALLSELECTED, isAllSelected()));
            //通知改变了选中状态 目的是向结算界面中传递选中的list集合
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFILIST, mList));
        }

    }

    //为加减按钮 删除 跳转到商品详情  设置监听器
    private class ButtonOnClickListener implements View.OnClickListener {
        int position;

        public ButtonOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            int shoppingCartId;
            switch (v.getId()) {
                case R.id.addbutton:
                    setPosition(position);
                    if (mList.get(getPosition()).getQuantity() >= mList.get(getPosition()).getGoods().getGoods_stock()) {
                        //如果大于库存
                        showToast("库存不足");
                        //Toast.makeText(mContext, "库存不足", Toast.LENGTH_SHORT).show();
                    } else {
                        mList.get(getPosition()).setQuantity(mList.get(getPosition()).getQuantity() + 1);
                        shoppingCartId = mList.get(position).getShopping_cart_id();
                        getAddNoHttpMethod(shoppingCartId);
                    }
                    break;
                case R.id.minusbutton:
                    setPosition(position);
                    //减法操作
                    if (mList.get(position).getQuantity() <= 1) {
                        showToast("商品数量最少为1");
                        //Toast.makeText(mContext, "商品数量最少为1", Toast.LENGTH_SHORT).show();
                    } else {
                        mList.get(getPosition()).setQuantity(mList.get(getPosition()).getQuantity() - 1);
                        shoppingCartId = mList.get(position).getShopping_cart_id();
                        getMinusNoHttpMethod(shoppingCartId);
                    }
                    break;
                case R.id.goodspicture:
                    //*******跳转到商品详情
                    goGoodDetailActivity(position);
                    break;
                case R.id.goodstitle:
                    // *******跳转到商品详情
                    goGoodDetailActivity(position);
                    break;
                case R.id.dayOrder:
                    //将当周订单的按钮背景颜色换成灰色，将当日订单的按钮背景颜色换成橙色；
                    setPosition(position);
                    int shoppingCartID = mList.get(position).getShopping_cart_id();
                    getDayOrderNoHttpMethod(shoppingCartID, "true");
                    break;
                case R.id.weekOrder:
                    //当周订单 弹出dialog选择日期
                    setPosition(position);
                    showSelectDateDialog();
                    break;
                case R.id.delete:
                    showDeleteDialog();
                    setPosition(position);
                    break;
                case R.id.allGoodsCheckboxTop:
                    // 头部 选中全部商品的商品
                    selectedAll();
                    notifyDataSetChanged();
                    break;
                case R.id.DeleteOperationTV:
                    //编辑按钮删除操作  将删除按钮显示出来
                    showDelete();
                    break;
                case R.id.checkbox_list:
                    setPosition(position);
                    notifyDataSetChanged();
                    break;
            }
        }
    }

    //CheckBox 选择改变监听器
    private class CheckBoxChangedListener implements CheckBox.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (int) buttonView.getTag();
            mList.get(position).setSelected(isChecked);
            mHandler.sendMessage(mHandler.obtainMessage(CARTGOODSCOUNT, getAllGoodsCount())); // 显示总数量
            //通知改变总价格 将总价格传给Handler
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice()));
            //如果商品全部被选中，则全选按钮也被 默认为选中
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEALLSELECTED, isAllSelected()));
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFILIST, mList)); // 改变了list 结算页面的list
            mainHandler.sendMessage(mainHandler.obtainMessage(CARTGOODSCOUNT, getAllGoodsCount())); //改变徽章
        }
    }

    /**
     * 计算选中商品的金额
     *
     * @return
     */
    private float getTotalPrice() {
        /*DecimalFormat df = new DecimalFormat("0.00");
        String st = df.format(); //double 保留两位小数*/
        CartGoods mCartGoods = null;
        float totalPrice = 0;
        for (int i = 1; i < mList.size(); i++) {
            mCartGoods = mList.get(i);
            if (mCartGoods.isSelected()) {
                totalPrice += mCartGoods.getQuantity() * mCartGoods.getPrice();
            }
        }
        return totalPrice;
    }

    /**
     * 判断是否全被选中
     *
     * @return
     */
    private boolean isAllSelected() {
        boolean flag = true;
        for (int i = 1; i < mList.size(); i++) {
            if (!mList.get(i).isSelected()) {
                flag = false;
            }
        }
        return flag;
    }

    // 全部选中
    private void selectedAll() {
        if(isAllSelected() == true){
            for (int i = 1; i < mList.size(); i++) {
                //设置beans
                mList.get(i).setSelected(false);
            }
        }else {
            for (int i = 1; i < mList.size(); i++) {
                //设置beans
                mList.get(i).setSelected(true);
            }
        }

    }

    private void showDelete() {
        // 将删除按钮显示出来
        setShowDelete(!isShowDelete);
        notifyDataSetChanged();
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
                int shoppingCartIds = mList.get(getPosition()).getShopping_cart_id();
                //进行删除网络请求
                getDeleteNoHttpMethod(shoppingCartIds);
            }
        });
        builder.create().show();
    }

    //********** 显示当周订单 选择日期dialog
    private void showSelectDateDialog() {
        CartDialogSelectDate.Builder builder = new CartDialogSelectDate.Builder(mContext);
        builder.setMessage("当周订单");
        //设置接下来的一周日期：
        final String[] date = setDate();
        builder.setDay1ButtonClick(date[0], new DialogInterface.OnClickListener() {
            //点击了第一天
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //请求网络 更新购物车 将extension1 的值传到接口中
                int shoppingCartIds = mList.get(getPosition()).getShopping_cart_id();
                getWeekDayOrderNoHttpMethod(shoppingCartIds, date[0]);
            }
        });
        builder.setDay2ButtonClick(date[1], new DialogInterface.OnClickListener() {
            //点击了第二天
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int shoppingCartIds = mList.get(getPosition()).getShopping_cart_id();
                getWeekDayOrderNoHttpMethod(shoppingCartIds, date[1]);
            }
        });
        builder.setDay3ButtonClick(date[2], new DialogInterface.OnClickListener() {
            //点击了第三天
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int shoppingCartIds = mList.get(getPosition()).getShopping_cart_id();
                getWeekDayOrderNoHttpMethod(shoppingCartIds, date[2]);
            }
        });
        builder.setDay4ButtonClick(date[3], new DialogInterface.OnClickListener() {
            //点击了第四天
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int shoppingCartIds = mList.get(getPosition()).getShopping_cart_id();
                getWeekDayOrderNoHttpMethod(shoppingCartIds, date[3]);
            }
        });
        builder.setDay5ButtonClick(date[4], new DialogInterface.OnClickListener() {
            //点击了第五天
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int shoppingCartIds = mList.get(getPosition()).getShopping_cart_id();
                getWeekDayOrderNoHttpMethod(shoppingCartIds, date[4]);
            }
        });

        builder.setDay6ButtonClick(date[5], new DialogInterface.OnClickListener() {
            //点击了第六天
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int shoppingCartIds = mList.get(getPosition()).getShopping_cart_id();
                getWeekDayOrderNoHttpMethod(shoppingCartIds, date[5]);
            }
        });
        builder.setDay7ButtonClick(date[6], new DialogInterface.OnClickListener() {
            //点击了第七天
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int shoppingCartIds = mList.get(getPosition()).getShopping_cart_id();
                getWeekDayOrderNoHttpMethod(shoppingCartIds, date[6]);
            }
        });
        //显示dialog
        builder.create().show();
    }

    //设置当周订单 日期选择框中的日期 （一周）
    private String[] setDate() {
        String[] date = new String[7];
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            date[i] = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE);
            c.add(Calendar.DATE, 1);
        }
        return date;
    }


    // **************统计购物车中 选中的 的数量
    private int getAllGoodsCount() {
        int count = 0;
        for (int i = 1; i < mList.size(); i++) {
            if (mList.get(i).isSelected()) {
                count += mList.get(i).getQuantity();
            }
        }
        return count;
    }

    //*********** 跳转到商品详情
    private void goGoodDetailActivity(int position) {
        Intent intent = new Intent(mContext, GoodDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("values", mList.get(position).getGoods());
        intent.putExtra("values", bundle);
        mContext.startActivity(intent);
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

    ///************加操作***********
    private void getAddNoHttpMethod(int shoppingCartId) {
        try {
            String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart/" + shoppingCartId;
            mRequestQueue = NoHttp.newRequestQueue();
            Request<String> request = NoHttp.createStringRequest(url, RequestMethod.PUT);
            request.addHeader("Authorization", authorization()); // 添加请求头
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("quantity", mList.get(position).getQuantity());
            jsonObject.put("extension1", mList.get(position).getExtension1());
            request.setDefineRequestBodyForJson(jsonObject);
            mRequestQueue.add(ADDFINISH, request, mOnResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /// ************减操作 ***************
    private void getMinusNoHttpMethod(int shoppingCartId) {
        try {
            String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart/" + shoppingCartId;
            mRequestQueue = NoHttp.newRequestQueue();
            Request<String> request = NoHttp.createStringRequest(url, RequestMethod.PUT);
            request.addHeader("Authorization", authorization()); // 添加请求头
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("quantity", mList.get(position).getQuantity());
            jsonObject.put("extension1", mList.get(position).getExtension1());
            request.setDefineRequestBodyForJson(jsonObject);
            mRequestQueue.add(MINUSFINISH, request, mOnResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //******** 删除操作 ***********
    private void getDeleteNoHttpMethod(int shoppingCartIds) {
        String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart/" + shoppingCartIds;
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.DELETE);
        request.addHeader("Authorization", authorization()); // 添加请求头
        mRequestQueue.add(DELETEFINISH, request, mOnResponseListener);
    }

    //******** 设置当周订单的日期 ***********
    private void getWeekDayOrderNoHttpMethod(int shoppingCartIds, String extension) {
        try {
            String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart/" + shoppingCartIds;
            mRequestQueue = NoHttp.newRequestQueue();
            Request<String> request = NoHttp.createStringRequest(url, RequestMethod.PUT);
            request.addHeader("Authorization", authorization()); // 添加请求头
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("quantity", mList.get(position).getQuantity());
            jsonObject.put("extension1", extension);
            request.setDefineRequestBodyForJson(jsonObject);
            mRequestQueue.add(UPDATEWEEKFINISH, request, mOnResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //******** 设置当日订单的日期 ***********
    private void getDayOrderNoHttpMethod(int shoppingCartIds, String extension) {
        try {
            String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart/" + shoppingCartIds;
            mRequestQueue = NoHttp.newRequestQueue();
            Request<String> request = NoHttp.createStringRequest(url, RequestMethod.PUT);
            request.addHeader("Authorization", authorization()); // 添加请求头
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("quantity", mList.get(position).getQuantity());
            jsonObject.put("extension1", extension);
            request.setDefineRequestBodyForJson(jsonObject);
            mRequestQueue.add(UPDATDAYFINISH, request, mOnResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    // *************加法操作 减法操作  删除操作  请求网络
    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            //加法
            if (what == ADDFINISH) {
                String result = response.get();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean resultADD = jsonObject.getBoolean("success");
                    //如果返回true 进行加法操作
                    if (resultADD) {
                        addMethod();
                    } else {
                        showToast("请求失败");
                       // Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (what == MINUSFINISH) {
                String result = response.get();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean resultMINUS = jsonObject.getBoolean("success");
                    //如果返回true 进行减法操作
                    if (resultMINUS) {
                        minusMethod();
                    } else {
                        showToast("请求失败");
                        //Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (what == DELETEFINISH) {
                String result = response.get();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean resultDelete = jsonObject.getBoolean("success");
                    if (resultDelete) {
                        deleteMethod();
                    } else {
                        showToast("请求失败");
                        //Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //点击当周订单后的请求网络操作
            if (what == UPDATEWEEKFINISH) {
                String result = response.get();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean resultUpdate = jsonObject.getBoolean("success");
                    if (resultUpdate) {
                        //mList.get(getPosition()).setExtension1();
                        //解析extension1
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        String extension1 = jsonObjectData.getString("extension1");
                        mList.get(getPosition()).setExtension1(extension1);
                        notifyDataSetChanged();
                    } else {
                        showToast("请求失败");
                       // Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //点击当日订单后的请求网络操作
            if (what == UPDATDAYFINISH) {
                String result = response.get();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean resultUpdate = jsonObject.getBoolean("success");
                    if (resultUpdate) {
                        //mList.get(getPosition()).setExtension1();
                        //解析extension1
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        String extension1 = jsonObjectData.getString("extension1");
                        mList.get(getPosition()).setExtension1(extension1);
                        notifyDataSetChanged();
                    } else {
                        showToast("请求失败");
                        //Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
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

    private void addMethod() {
        //请求结果为true 那么进行刷新界面
        notifyDataSetChanged();
        mainHandler.sendMessage(mainHandler.obtainMessage(CARTGOODSCOUNT, getAllGoodsCount())); //改变徽章
        mHandler.sendMessage(mHandler.obtainMessage(CARTGOODSCOUNT, getAllGoodsCount())); // 显示总数量
        mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice())); //显示总价格
    }

    //****************减法操作
    private void minusMethod() {
        //请求结果为true 那么进行刷新界面
        notifyDataSetChanged();
        mHandler.sendMessage(mHandler.obtainMessage(CARTGOODSCOUNT, getAllGoodsCount())); // 显示总数量
        mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice()));
        //改变购物车上的徽章
        mainHandler.sendMessage(mainHandler.obtainMessage(CARTGOODSCOUNT, getAllGoodsCount()));
    }

    //删除操作
    private void deleteMethod() {
        //删除操作
        mList.remove(getPosition());
        notifyDataSetChanged();
        //判断购物车是否为空如果为空显示空页面
        if (mList.size() == 1) {
            //通知cartFragment 显示空界面。
            mHandler.sendEmptyMessage(DeleteMethod);
        }
        mHandler.sendMessage(mHandler.obtainMessage(CARTGOODSCOUNT, getAllGoodsCount())); // 显示总数量
        mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice())); //显示总价格
        mainHandler.sendMessage(mainHandler.obtainMessage(CARTGOODSCOUNT, getAllGoodsCount())); //改变徽章
    }

    //显示吐司
    private void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }
}
