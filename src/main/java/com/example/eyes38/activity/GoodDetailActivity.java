package com.example.eyes38.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eyes38.R;
import com.example.eyes38.beans.Goods;
import com.example.eyes38.utils.CartBadgeView;

public class GoodDetailActivity extends AppCompatActivity {
    //数据源
    Goods goods;
    ImageView goodsPicImageView,goodsTxtPicImageView;
    TextView goodsUnitTextView,goodsStockTextView,goodsRemarkTextView,goodsCommentCountTextView;
    LinearLayout linearLayout;
    ImageView backImageView;
    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        initView();
        initData();
        setViewToData();
        initListener();
        setCartBadgeView();
    }

    private void setCartBadgeView() {
        //CartBadgeView这是购物车上的徽章
        CartBadgeView mCartBadgeView = new CartBadgeView(GoodDetailActivity.this,mButton);
        //mCartBadgeView.setBackgroundResource(R.drawable.badge_ifaux);
        mCartBadgeView.setText("1");
        mCartBadgeView.setTextColor(Color.WHITE);
        mCartBadgeView.setTextSize(12);
        //mCartBadgeView.setBadgeMargin(30,30);
        mCartBadgeView.setBadgeMargin(5);//各个边的边隔
        mCartBadgeView.setBadgeBackgroundColor(this.getResources().getColor(R.color.topical));
        mCartBadgeView.setBadgePosition(CartBadgeView.POSITION_TOP_RIGHT);
        mCartBadgeView.show();
    }

    private void initListener() {
        //对评论区域监听
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodDetailActivity.this,CommentActivity.class);
                startActivity(intent);
            }
        });
        //对返回键监听
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setViewToData() {
        //将数据写入各个控件
        Glide.with(this).load(goods.getPath()).into(goodsPicImageView);
        goodsUnitTextView.setText(goods.getGoods_platform_price()+goods.getGoods_unit());
        goodsStockTextView.setText(goods.getGoods_stock()+"");
        goodsRemarkTextView.setText(goods.getGoods_remark());
        goodsCommentCountTextView.setText(goods.getGoods_comment_count()+"");
        Glide.with(this).load(goods.getGoods_specification()).into(goodsTxtPicImageView);
    }

    private void initData() {
        //初始化数据，现在数据是写死的
        getData();
//        goods = new Goods(1,"苹果",null,"水果","100g","10/100g",null,"苹果",11f,10f,0,4,100);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("values");
        goods = (Goods) bundle.get("values");
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
        linearLayout = (LinearLayout) findViewById(R.id.goods_comment_layout);
        backImageView = (ImageView) findViewById(R.id.goods_detail_back);
        mButton = (Button) findViewById(R.id.goods_detail_carbutton);
    }
}
