package com.example.eyes38.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
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
import com.example.eyes38.utils.CartDialog;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
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
    private static final int CARTGOODSCOUNT = 308; // 通知mainactivity 改变徽章
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
    Handler goodDetailHandler = (new GoodDetailActivity()).goodDetailHandler; // 向GoodDetailActivity传值 改变徽章
    Handler mHandler;

    
    private Handler httpHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DELETEFINISH:
                    if ((Boolean) msg.obj == true) {
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
    public Cart_GoodsAdapter(List<CartGoods> mCartGoodses, Context context, Handler handler) {
        mList = mCartGoodses;
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
    public void onBindViewHolder(final CartGoodsViewHolder holder, final int position) {
        //设置item Tag
        holder.itemView.setTag(mList.get(position));
        Glide.with(mContext).load(mList.get(position).getPath()).into(holder.mImageView);
        //double类型保留两位小数
        DecimalFormat df = new DecimalFormat("0.00");
        String st = df.format(mList.get(position).getPrice() * mList.get(position).getNum()); //double 保留两位小数
        holder.mPriceTextView.setText(st);
        holder.mTitleTextView.setText(mList.get(position).getTitle());
        holder.mCountTextView.setText(mList.get(position).getNum() + "");
        if (isShowDelete) {
            //如果显示 删除
            holder.mDeleteTextView.setVisibility(View.VISIBLE);
            holder.mDayOrder.setVisibility(View.GONE);
        } else {
            holder.mDeleteTextView.setVisibility(View.GONE);
            holder.mDayOrder.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);

        holder.mCheckBox.setChecked(mList.get(position).isSelected());
        holder.mCheckBox.setOnCheckedChangeListener(new CheckBoxChangedListener());
        holder.addButton.setOnClickListener(new ButtonOnClickListener(position));
        holder.subButton.setOnClickListener(new ButtonOnClickListener(position));
        holder.mDeleteTextView.setOnClickListener(new ButtonOnClickListener(position));


       /* holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先增加1
                mList.get(position).setNum(mList.get(position).getNum() + 1);
                int currcount = mList.get(position).getNum();//获取当前 购物车中的数量
                Log.e("Addbutton", "点击了" + position + "addbutton" + "当前有" + currcount + "商品");
                notifyDataSetChanged();
            }
        });*/
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
        Button addButton, subButton, mDayOrder;

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
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice()));
            //如果商品全部被选中，则全选按钮也被 默认为选中
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEALLSELECTED, isAllSelected()));
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFILIST, mList));
        }
        //mHandler.sendMessage(mHandler.obtainMessage(2, getTotalPrice()));
        //如果商品全部被选中，则全选按钮也被 默认为选中
        //mHandler.sendMessage(mHandler.obtainMessage(1, true));
    }

    //为加减按钮 设置监听器
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
                    String url = "http://api.dev.ilexnet.com/simulate/38eye/cart-api/cart/:";
                    String args = "shoppingCartId";
                    String shoppingCartId = mList.get(position).getShopping_cart_id() + "";
                    getNoHttpMethod(url, args, shoppingCartId, mAddOnResponseListener, PUT);
                    break;
                case R.id.minusbutton:
                    if (mList.get(position).getNum() <= 1) {
                        Toast.makeText(mContext, "商品数量最少为1", Toast.LENGTH_SHORT).show();
                    } else {
                        mList.get(position).setNum(mList.get(position).getNum() - 1);
                        notifyDataSetChanged();
                        mainHandler.sendMessage(mainHandler.obtainMessage(308,getAllGoodsCount()));
                        mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice()));
                    }
                    break;
                case R.id.delete:
                    showDialog();
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
                totalPrice += mCartGoods.getNum() * mCartGoods.getPrice();
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
    private void showDialog() {
        CartDialog.Builder builder = new CartDialog.Builder(mContext);
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
                String url = "http://api.dev.ilexnet.com/simulate/38eye/cart-api/cart/:";
                String args = "shoppingCartIds";
                String shoppingCartIds = mList.get(getPosition()).getShopping_cart_id() + "";
                getNoHttpMethod(url, args, shoppingCartIds, mDeleteOnResponseListener, DELETE);
            }
        });
        builder.create().show();
    }


    private void addMethod() {
        //加法操作
        if (mList.get(getPosition()).getNum() >=  mList.get(getPosition()).getGoods().getGoods_stock()) {
            //如果大于库存
            Toast.makeText(mContext, "库存不足", Toast.LENGTH_SHORT).show();
        } else {
            mList.get(getPosition()).setNum(mList.get(getPosition()).getNum() + 1);
            notifyDataSetChanged();
            mainHandler.sendMessage(mainHandler.obtainMessage(CARTGOODSCOUNT,getAllGoodsCount())); //改变徽章

            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice()));

        }

    }

    private void deleteMethod() {
        //删除操作
        mList.remove(getPosition());
        notifyDataSetChanged();
        //setCartGoodsCount(getAllGoodsCount());
        mainHandler.sendMessage(mainHandler.obtainMessage(308,getAllGoodsCount()));
    }

    // 统计购物车中的数量
    private int getAllGoodsCount (){
        int count = 0;
        for (int i = 0; i < mList.size(); i++) {
            count += mList.get(i).getNum();
        }
        return count;
    }

    /*//查看 购物车中谁被选中了 */


    // 加法操作 请求网络
    private OnResponseListener<String> mAddOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWHAT) {
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
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
        }

        @Override
        public void onFinish(int what) {
        }
    };


    //删除操作请求网络
    private OnResponseListener<String> mDeleteOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWHAT) {
                String result = response.get();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean resultDelete = jsonObject.getBoolean("success");
                    //请求完成返回结果 通知handler 处理结果
                    Log.e("返回结果", resultDelete + "");
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


    //提取Nohttp 代码

    /**
     * @param url                地址
     * @param arg1               请求参数
     * @param arg2               请求参数值
     * @param onResponseListener 发送的 message
     * @ x 请求方式 1.get 2.post，3.delete方式 4是 put方式
     */
    private void getNoHttpMethod(String url, String arg1, String arg2, OnResponseListener<String> onResponseListener, int x) {
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
        request.setRequestFailedReadCache(true);
        request.add(arg1, arg2);
        mRequestQueue.add(mWHAT, request, onResponseListener);
    }
}