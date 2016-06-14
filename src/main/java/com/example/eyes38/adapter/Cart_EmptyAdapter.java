package com.example.eyes38.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eyes38.MainActivity;
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
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("cart",1);
                mContext.startActivity(intent);
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
