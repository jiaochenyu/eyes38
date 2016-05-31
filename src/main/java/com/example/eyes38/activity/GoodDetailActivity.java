package com.example.eyes38.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eyes38.R;
import com.example.eyes38.beans.Goods;
import com.example.eyes38.utils.CartBadgeView;

public class GoodDetailActivity extends AppCompatActivity {
    private static final int CARTGOODSCOUNT = 308;
    //数据源
    Goods goods;
    ImageView goodsPicImageView, goodsTxtPicImageView;
    TextView goodsUnitTextView, goodsStockTextView, goodsRemarkTextView, goodsCommentCountTextView;
    LinearLayout linearLayout;
    ImageView backImageView;
    CartBadgeView mCartBadgeView;  //购物车图标徽章
    Button mButton;
    RadioGroup mRadioGroup; //
    RadioButton mConsultButton, mCartButton, mBuynowButton, mAddCartButton;  //咨询按钮 ，购物车按钮 ,立即购买，添加到购物车

    public Handler goodDetailHandler = new Handler() {  //购物车图标上的徽章改变值
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //从购物车中接收数据
            switch (msg.what) {
                case CARTGOODSCOUNT:
                    if (((Integer) msg.obj) != 0) {
                        mCartBadgeView.setText(msg.obj + "");
                        mCartBadgeView.show();
                    } else {
                        mCartBadgeView.hide();
                    }
            }
        }
    };

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
        mCartBadgeView = new CartBadgeView(GoodDetailActivity.this, mButton);
        //mCartBadgeView.setBackgroundResource(R.drawable.badge_ifaux);
        mCartBadgeView.setTextColor(Color.WHITE);
        mCartBadgeView.setTextSize(12);
        //mCartBadgeView.setBadgeMargin(30,30);
        mCartBadgeView.setBadgeMargin(5);//各个边的边隔
        mCartBadgeView.setBadgeBackgroundColor(this.getResources().getColor(R.color.topical));
        mCartBadgeView.setBadgePosition(CartBadgeView.POSITION_TOP_RIGHT);

    }

    private void initListener() {
        //对评论区域监听
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodDetailActivity.this, CommentActivity.class);
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


        MyOnClickLisenter myOnClickLisenter = new MyOnClickLisenter();

        //购物车
        mCartButton.setOnClickListener(myOnClickLisenter);

    }

    private void setViewToData() {
        //将数据写入各个控件
        Glide.with(this).load(goods.getPath()).into(goodsPicImageView);
        goodsUnitTextView.setText(goods.getGoods_platform_price() + goods.getGoods_unit());
        goodsStockTextView.setText(goods.getGoods_stock() + "");
        goodsRemarkTextView.setText(goods.getGoods_remark());
        goodsCommentCountTextView.setText(goods.getGoods_comment_count() + "");
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
        mConsultButton = (RadioButton) findViewById(R.id.goods_detail_radio_consult);
        mRadioGroup = (RadioGroup) findViewById(R.id.goods_detail_radiogroup); //
        mCartButton = (RadioButton) findViewById(R.id.goods_detail_radio_cart); //购物车
        mBuynowButton = (RadioButton) findViewById(R.id.goods_detail_radio_buynow); // 立即购买
        mAddCartButton = (RadioButton) findViewById(R.id.goods_detail_radio_addcart); // 加入购物车

    }

    class MyOnClickLisenter implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int i = v.getId();
            switch (i) {
                case R.id.goods_detail_radio_consult:
                    //联系我
                    break;
                case R.id.goods_detail_radio_cart:
                    //跳转到购物车
                    Intent intent = new Intent(GoodDetailActivity.this, CartActivity.class);
                    startActivity(intent);
                    break;
                case R.id.goods_detail_radio_buynow:
                    //立即购买
                    break;
                case R.id.goods_detail_radio_addcart:
                    //加入购物车
                    /**
                     * 先判断用户是否登陆
                     * 没登录将商品添加到本地购物车（数据库）。
                     * 登陆了将本地购物车添加到用户购物车。
                     */

                    break;
            }
        }
    }
}
