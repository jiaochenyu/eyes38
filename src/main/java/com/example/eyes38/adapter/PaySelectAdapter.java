package com.example.eyes38.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.activity.PayActivity;
import com.example.eyes38.beans.Receipt;

import java.util.List;

/**
 * Created by JCY on 2016/6/7.
 */
public class PaySelectAdapter extends RecyclerView.Adapter<PaySelectAdapter.PaySelectHodler> {
    private List<Receipt> mList;
    private Context mContext;

    public PaySelectAdapter(List<Receipt> list, Context context) {
        mList = list;
        mContext = context;
    }


    @Override
    public PaySelectHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_pay_address_item,parent,false);
        PaySelectHodler paySelectHodler = new PaySelectHodler(view);
        return paySelectHodler;
    }

    @Override
    public void onBindViewHolder(PaySelectHodler holder, final int position) {
        holder.firstnameTV.setText(mList.get(position).getReceipt_person());
        holder.phoneTV.setText(mList.get(position).getReceipt_phone());
        if (mList.get(position).isDefaultAddress()){
            holder.addressDefualtTV.setVisibility(View.VISIBLE);
        }
        holder.addressTV.setText(mList.get(position).getDistrict()+" "+mList.get(position).getReceipt_detail());
        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到PayActivity界面
                Intent intent = new Intent(mContext, PayActivity.class);
                intent.putExtra("addressInfo",mList.get(position));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    class PaySelectHodler extends RecyclerView.ViewHolder{
        private RelativeLayout mRelativeLayout;
        private TextView firstnameTV, phoneTV, addressDefualtTV, addressTV;

        public PaySelectHodler(View itemView) {
            super(itemView);
            firstnameTV = (TextView) itemView.findViewById(R.id.firstname);
            phoneTV = (TextView) itemView.findViewById(R.id.phone);
            addressDefualtTV = (TextView) itemView.findViewById(R.id.address_default);
            addressTV = (TextView) itemView.findViewById(R.id.address);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.pay_select_address_rl);
        }
    }
}
