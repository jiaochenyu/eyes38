package com.example.eyes38.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eyes38.R;

/**
 * Created by jqchen on 2016/5/17.
 */
public class Cart_EmptyAdapter extends RecyclerView.Adapter<Cart_EmptyAdapter.CartEmptyHolder> {
    //这是空适配器
    Context mContext;
    public Cart_EmptyAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public CartEmptyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_empty_item, parent, false);
        CartEmptyHolder viewHolder = new CartEmptyHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CartEmptyHolder holder, int position) {
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了随便看看", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class CartEmptyHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;

        public CartEmptyHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.cart_empty_gohome);
        }
    }
}
