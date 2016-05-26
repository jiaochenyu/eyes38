package com.example.eyes38.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
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
import com.example.eyes38.R;
import com.example.eyes38.beans.CartGoods;

import java.util.List;

/**
 * Created by jqchen on 2016/5/20.
 */
public class Cart_GoodsAdapter extends RecyclerView.Adapter<Cart_GoodsAdapter.CartGoodsViewHolder> implements View.OnClickListener {
    public static final int NOTIFICHANGEPRICE = 383;  //通知购物车页面  改变总价格
    public static final int NOTIFICHANGEALLSELECTED = 384; //通知购物车页面 设置为全选
    private List<CartGoods> mList;
    private Context mContext;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;//监听事件

    Handler mHandler;


    //生成构造
    public Cart_GoodsAdapter(List<CartGoods> mCartGoodses, Context context, Handler handler) {
        mList = mCartGoodses;
        mContext = context;
        mHandler = handler;
        //初始化数据
        initDate();
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
        holder.mPriceTextView.setText(mList.get(position).getPrice() + "");
        holder.mTitleTextView.setText(mList.get(position).getTitle());
        holder.mCountTextView.setText(mList.get(position).getNum() + "");
        holder.mCheckBox.setTag(position);
        //holder.mCheckBox.setChecked(getIsSelected().get(position)); //isSelected<position,?>
        holder.mCheckBox.setChecked(mList.get(position).isSelected());
        holder.mCheckBox.setOnCheckedChangeListener(new CheckBoxChangedListener());
        holder.addButton.setOnClickListener(new ButtonOnClickListener(position));
        holder.subButton.setOnClickListener(new ButtonOnClickListener(position));

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
        TextView mTitleTextView, mPriceTextView, mCountTextView;// 商品名称, 商品价格， 增减数量
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
            mDayOrder = (android.widget.Button) mView.findViewById(R.id.dayOrder); // 当日订单按钮
        }
    }


    /**
     * 对购物车进行操作
     */


    //初始化每一个item 设置为true 选中
    private void initDate() {
        //isSelected = new HashMap<>();
        for (int i = 0; i < mList.size(); i++) {
            //getIsSelected().put(i, true);
            mList.get(i).setSelected(true);
        }
        if(isAllSelected()){
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice()));
            //如果商品全部被选中，则全选按钮也被 默认为选中
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEALLSELECTED, isAllSelected()));
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
                    if (mList.get(position).getNum() > 10) {
                        Toast.makeText(mContext, "库存不足", Toast.LENGTH_SHORT).show();
                    } else {
                        mList.get(position).setNum(mList.get(position).getNum() + 1);
                        notifyDataSetChanged();
                        mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice()));

                    }
                    break;
                case R.id.minusbutton:
                    if (mList.get(position).getNum() <= 1) {
                        Toast.makeText(mContext, "商品数量最少为1", Toast.LENGTH_SHORT).show();
                    } else {
                        mList.get(position).setNum(mList.get(position).getNum() - 1);
                        notifyDataSetChanged();
                        mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice()));
                    }
                    break;
            }
        }
    }

    //CheckBox 选择改变监听器
    private class CheckBoxChangedListener implements CheckBox.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (int) buttonView.getTag();
            //getIsSelected().put(position, isChecked);
            CartGoods mCartGoods = mList.get(position);
            mCartGoods.setSelected(isChecked);
            //通知改变总价格 将总价格传给Handler
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEPRICE, getTotalPrice()));
            //如果商品全部被选中，则全选按钮也被 默认为选中
            mHandler.sendMessage(mHandler.obtainMessage(NOTIFICHANGEALLSELECTED, isAllSelected()));
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
           /* if (!getIsSelected().get(i)) {
                flag = false;
                break;
            }*/
            if(!mList.get(i).isSelected()){
                flag = false;
            }
        }
        return flag;
    }
}