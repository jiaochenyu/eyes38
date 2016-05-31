package com.example.eyes38.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.adapter.PayAdapter;
import com.example.eyes38.beans.CartGoods;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PayActivity extends AppCompatActivity {
    private ImageView payBackImag; //返回
    private TextView shouhuoAdress, allGoodsPrice, peisongMoney, totalPrice; //收货地址(显示默认)、总价、运费、总价格(商品价格+运费)
    private TextView goPay;  // 去付款
    private RadioGroup mPayRadioGroup; //支付方式
    private RadioButton mWeChatRadioButton; //微信支付
    private RecyclerView mGoodsRecyclerView; // 显示订单商品
    private List<CartGoods> mList; // 把购物车选中的商品信息传过来
    private float peisong = 0;
    PayAdapter mPayAdapter;


    public float getPeisong() {
        return peisong;
    }

    public void setPeisong(float peisong) {
        this.peisong = peisong;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_jiesuan);
        initViews();
        initData();
        setView();
        initListener();
    }


    private void initViews() {
        payBackImag = (ImageView) findViewById(R.id.pay_back);
        shouhuoAdress = (TextView) findViewById(R.id.pay_adress);   //收货地址
        allGoodsPrice = (TextView) findViewById(R.id.allGoodsPrice);  //商品价格
        peisongMoney = (TextView) findViewById(R.id.peisongmoney); // 配送费用
        totalPrice = (TextView) findViewById(R.id.totalPrice);  // 总价 商品价格+配送费用
        mGoodsRecyclerView = (RecyclerView) findViewById(R.id.orderGoodsInfo);
        mGoodsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }

    private void setView() {
        DecimalFormat df = new DecimalFormat("0.00");
        String st = df.format(allGoodsPrice()); // 商品价格
        allGoodsPrice.setText(st);

        setPeisong(20);
        peisongMoney.setText(getPeisong() + "");  //配送价格

        String st2 = df.format(allGoodsPrice() + getPeisong()); //商品价格 + 配送价格
        totalPrice.setText(st2);
    }

    private void initData() {
        mList = new ArrayList<>();
        Intent intent = getIntent();
        mList = (List<CartGoods>) intent.getSerializableExtra("list");
        Log.e("传过来的集合", mList.size() + "    " + mList.toString());
        mPayAdapter = new PayAdapter(mList, PayActivity.this);
        mGoodsRecyclerView.setAdapter(mPayAdapter);
    }

    private void initListener() {
        ClickListener clickListener = new ClickListener();
       /* mPayRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            }
        });*/
        payBackImag.setOnClickListener(clickListener);
    }

    //时间监听
    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.pay_back:
                    //返回键
                    //onBackPressed();
                    finish();
                    break;
                case R.id.weChatPay:
                    //选择了微信支付方式
                    break;
                case R.id.gopay:
                    //付款之前先判断收货地址 和支付方式
                    break;

            }
        }
    }

    private double allGoodsPrice() {
        double goodsPrice = 0;
        for (int i = 0; i < mList.size(); i++) {
            goodsPrice += mList.get(i).getPrice() * mList.get(i).getNum();
        }
        return goodsPrice;
    }

}
