package com.example.eyes38.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.beans.Goods;

public class GoodDetailActivity extends AppCompatActivity {
    //数据源
    Goods goods;
    ImageView goodsPicImageView,goodsTxtPicImageView;
    TextView goodsUnitTextView,goodsStockTextView,goodsRemarkTextView,goodsCommentCountTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        initView();
        initData();
        setViewToData();
//        initListener();
    }

    private void initListener() {

    }

    private void setViewToData() {
        //将数据写入各个控件
        goodsUnitTextView.setText(goods.getGoods_unit());
        goodsStockTextView.setText(goods.getGoods_stock()+"");
        goodsRemarkTextView.setText(goods.getGoods_remark());
        goodsCommentCountTextView.setText(goods.getGoods_comment_count()+"");
    }

    private void initData() {
        //初始化数据，现在数据是写死的
        goods = new Goods(1,"苹果",null,"水果","100g","10/100g",null,"苹果",11f,10f,0,4,100);
    }

    private void initView() {
        //初始化控件
        //商品图片，图文详情
        goodsPicImageView = (ImageView) findViewById(R.id.goods_detail_pic);
        goodsTxtPicImageView = (ImageView) findViewById(R.id.goods_detail_txt_pic);
        //商品规格，库存量，备注，评论数量
        goodsUnitTextView = (TextView) findViewById(R.id.goods_detail_unit);
        goodsStockTextView = (TextView) findViewById(R.id.goods_detail_stock);
        goodsRemarkTextView = (TextView) findViewById(R.id.goods_detai_remark);
        goodsCommentCountTextView = (TextView) findViewById(R.id.goods_detail_commentcount);
    }
}
