package com.example.eyes38.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eyes38.R;
import com.example.eyes38.beans.CartGoods;

import java.util.List;

/**
 * Created by jqchen on 2016/5/20.
 */
public class Cart_GoodsAdapter extends RecyclerView.Adapter<Cart_GoodsAdapter.CartGoodsViewHolder> implements View.OnClickListener {
    private List<CartGoods> mList;
    private Context mContext;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;//监听事件

    //生成构造
    public Cart_GoodsAdapter(List<CartGoods> mCartGoodses, Context context) {
        mList = mCartGoodses;
        mContext = context;
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


    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnRecyclerViewItemClickListener = listener;
    }


    @Override
    public void onBindViewHolder(final CartGoodsViewHolder holder, final int position) {
        Glide.with(mContext).load(mList.get(position).getPath()).into(holder.mImageView);
        holder.mPriceTextView.setText(mList.get(position).getPrice() + "");
        holder.mTitleTextView.setText(mList.get(position).getTitle());
        holder.mCountTextView.setText(mList.get(position).getNum()+"");
        holder.itemView.setTag(mList.get(position));

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先增加1
                mList.get(position).setNum(mList.get(position).getNum() + 1);
                int currcount = mList.get(position).getNum();//获取当前 购物车中的数量
                Log.e("Addbutton", "点击了" + position + "addbutton"+"当前有"+currcount+"商品");
                notifyDataSetChanged();
            }
        });
    }

    //按钮的事件监听

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class CartGoodsViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;
        ImageView mImageView;
        TextView mTitleTextView, mPriceTextView, mCountTextView;// 商品名称, 商品价格， 增减数量
        Button addButton, subButton, mDayOrder;
        //事件监听
        View mItemView;

        public CartGoodsViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            initViews(itemView);

        }

        private void initViews(View mView) {
            mCheckBox = (CheckBox) mView.findViewById(R.id.checkbox_list); // 选中
            mImageView = (ImageView) mView.findViewById(R.id.goodspicture); // 图片
            mTitleTextView = (TextView) mView.findViewById(R.id.goodstitle); // 商品名称
            mPriceTextView = (TextView) mView.findViewById(R.id.cart_list_price);  // 价钱
            mCountTextView = (TextView) mView.findViewById(R.id.goods_count);// 添加到购物车中商品的数量
            addButton = (Button) mView.findViewById(R.id.addbutton); // 增加商品
            subButton = (Button) mView.findViewById(R.id.minusbutton); // 减少商品
            mDayOrder = (Button) mView.findViewById(R.id.dayOrder); // 当日订单按钮
        }
    }
}
