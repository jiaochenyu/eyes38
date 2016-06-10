package com.example.eyes38.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eyes38.Application.Application;
import com.example.eyes38.R;
import com.example.eyes38.beans.CartGoods;
import com.example.eyes38.beans.Goods;
import com.example.eyes38.utils.CartBadgeView;
import com.example.eyes38.utils.Substring;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GoodDetailActivity extends AppCompatActivity {
    private static final int CARTGOODSCOUNT = 308; // 购物车商品总数（响应码）
    private static final int CREATECART = 309; // 创建购物车（响应吗）
    private static final int ADDCART = 310; // 加入购物车（响应码）
    public static final int GETCOMMENTNUM = 100;
    //数据源
    private Goods goods;
    private ImageView goodsPicImageView, goodsTxtPicImageView;
    private TextView goodsUnitTextView, goodsStockTextView, goodsRemarkTextView, goodsCommentCountTextView;
    private LinearLayout linearLayout;
    private ImageView backImageView;
    private CartBadgeView mCartBadgeView;  //购物车图标徽章
    private Button mButton;
    private RadioButton mConsultButton, mCartButton, mBuynowButton, mAddCartButton;  //咨询按钮 ，购物车按钮 ,立即购买，添加到购物车
<<<<<<< HEAD
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
=======
    private CartGoods mCartGoods;
    private List<CartGoods> mList;
    private SharedPreferences sp;  //偏好设置 获取账号 密码
    private RequestQueue mRequestQueue;
    private int position; // 记录购物车中的位置

    public void setPosition(int position) {
        this.position = position;
    }

>>>>>>> af7e8073a54e52aa49c908129ddffb47d33771ee
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        initView();
        initData();
        setViewToData();
        initListener();
        getCartNoHttp(); //请求购物车 获取购物车中商品数量
        setCartBadgeView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCartNoHttp();
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
        mCartButton = (RadioButton) findViewById(R.id.goods_detail_radio_cart); //购物车
        mBuynowButton = (RadioButton) findViewById(R.id.goods_detail_radio_buynow); // 立即购买
        mAddCartButton = (RadioButton) findViewById(R.id.goods_detail_radio_addcart); // 加入购物车

    }

    private void initData() {
        //初始化数据，现在数据是写死的
        getData();
        //mList = new ArrayList<>();
        sp = getApplication().getSharedPreferences("userInfo", MODE_PRIVATE);  // 偏好设置初始化
        mRequestQueue = NoHttp.newRequestQueue();

    }

    private void initListener() {
        //对评论区域监听
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodDetailActivity.this, CommentActivity.class);
                intent.putExtra("product_id", goods.getGoods_id());
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
        //联系我
        mConsultButton.setOnClickListener(myOnClickLisenter);
        //购物车
        mCartButton.setOnClickListener(myOnClickLisenter);
        //立即购买
        mBuynowButton.setOnClickListener(myOnClickLisenter);
        //加入加入购物车
        mAddCartButton.setOnClickListener(myOnClickLisenter);
    }

    private void setViewToData() {
        //将数据写入各个控件
        Glide.with(this).load(goods.getPath()).into(goodsPicImageView);
        goodsUnitTextView.setText(goods.getGoods_platform_price() + goods.getGoods_unit());
        goodsStockTextView.setText(goods.getGoods_stock() + "");
        goodsRemarkTextView.setText(goods.getGoods_name());
        //截取字符串中的url
        String description = goods.getGoods_description();
        //如果有图文详情
        if (!Substring.getString(description).equals("")) {
            Glide.with(this).load(Substring.getString(description)).into(goodsTxtPicImageView);
        }
    }


    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("values");
        goods = (Goods) bundle.get("values");
        //获取商品评价数量
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/discussion-api/discussions";
        Request<String> mRequest = NoHttp.createStringRequest(url, RequestMethod.GET);
        //添加属性，筛选评论
        mRequest.add("item_id", goods.getGoods_id());
        mRequest.add("parent_id", 0);
        //设置缓存
        mRequest.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        mRequestQueue.add(GETCOMMENTNUM, mRequest, mOnResponseListener);

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
//                    finish();
                    break;
                case R.id.goods_detail_radio_buynow:
                    //立即购买 跳转到购物车
                    List<CartGoods> buynowList = new ArrayList<>();
                    CartGoods cartGoods = new CartGoods();
                    cartGoods.setExtension1(goods.getExtension());
                    cartGoods.setProduct_name(goods.getGoods_name());
                    cartGoods.setGoods(goods);
                    cartGoods.setQuantity(1); // 立即加入1件商品到购物车
                    cartGoods.setPrice(goods.getGoods_platform_price());
                    buynowList.add(cartGoods);
                    Intent intentBuynow = new Intent(GoodDetailActivity.this,PayActivity.class);
                    intentBuynow.putExtra("list", (Serializable) buynowList);
                    startActivity(intentBuynow);
                    break;
                case R.id.goods_detail_radio_addcart:
                    //加入购物车
                    customerStates(); //判断用户登陆状态
                    break;
            }
        }
    }

    //判断用户登陆状态
    private void customerStates() {
        //如果用户没有登录 那么显示空
        // 取出用户登录状态， 如果为1 代表登录 如果为0 是没有登录
        if (!Application.isLogin) {
            //如果用户没登录  购物车显示空
            Toast.makeText(GoodDetailActivity.this, "请登录", Toast.LENGTH_SHORT).show();
        } else {
            //登录了 进行判断购物车中商品是否已经存在
            goodsExist();
        }
    }


    private String authorization() {
        String username = sp.getString("USER_NAME", "");  // 应该从偏好设置中获取账号密码
        String password = sp.getString("PASSWORD", "");
        //Basic 账号+':'+密码  BASE64加密
        String addHeader = username + ":" + password;
        String authorization = "Basic " + new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));
        return authorization;
    }


    //先获取购物车中的商品总数，和该商品是否在购物车中 如果在那么在加入购物车的时候quantity + 1
    //如果不在 那么应该调用 创建购物车的接口

    //添加到购物车
//**************获取购物车信息************
    private void getCartNoHttp() {
        String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.addHeader("Authorization", authorization());
        mRequestQueue.add(CARTGOODSCOUNT, request, mOnResponseListener);
    }

    //添加商品前先判断商品是否已经存在了(一周菜谱和非一周菜谱是两种类型) 判断商品id 和extension1 是否已经存在。
    private void goodsExist() {
        if (mList.size() == 0) {
            postCreateGooodsNoHttp();
        } else {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getProduct_id() == goods.getGoods_id()) {
                    //说明该商品已经存在。接下来判断改商品的extension值和购物车中extension的值是否一样
                    if (mList.get(i).getExtension1().equals(goods.getExtension())) {
                        //说明存在该类型的商品。那么应该进行更新购物车操作（加操作）。先加一
                        if (mList.get(i).getQuantity() >= goods.getGoods_stock()) {
                            Toast.makeText(GoodDetailActivity.this, "库存不足", Toast.LENGTH_SHORT).show();
                        } else {
                            mList.get(i).setQuantity(mList.get(i).getQuantity() + 1);
                            setPosition(i);
                            putAddGooodsNoHttp(mList.get(i).getShopping_cart_id(), mList.get(i).getQuantity(), mList.get(i).getExtension1());
                        }
                    } else {
                        //说明不存在该类型的商品。那么应该进行创建购物车操作
                        postCreateGooodsNoHttp();
                    }
                    break;
                } else if(i == (mList.size()-1)) {
                    //说明不存在该类型的商品。那么应该进行创建购物车操作
                    postCreateGooodsNoHttp();
                }
            }

        }
    }

    //******************更新购物车进行加操作

    private void putAddGooodsNoHttp(int shoppingCartId, int quantity, String extension1) {
        try {
            String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart/" + shoppingCartId;
            Request<String> request = NoHttp.createStringRequest(url, RequestMethod.PUT);
            request.addHeader("Authorization", authorization()); // 添加请求头
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("quantity", quantity);
            jsonObject.put("extension1", extension1);
            request.setDefineRequestBodyForJson(jsonObject);
            mRequestQueue.add(ADDCART, request, mOnResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //******************创建购物车********
    private void postCreateGooodsNoHttp() {
        //增加商品接口
        String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.addHeader("Authorization", authorization());
        request.add("extension1", goods.getExtension());
        request.add("price", goods.getGoods_platform_price());
        request.add("product_id", goods.getGoods_id());
        request.add("quantity", 1);
        mRequestQueue.add(CREATECART, request, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == CARTGOODSCOUNT) {
                try {
                    mList = new ArrayList<>();
                    String result = response.get();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        JSONArray jsonArray2 = jsonObject2.getJSONArray("data");
                        for (int j = 0; j < jsonArray2.length(); j++) {
                            JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
                            int shopping_cart_id = jsonObject3.getInt("shopping_cart_id"); // 购物车id shopping_cart_id
                            int quantity = jsonObject3.getInt("quantity");//数量
                            String extension1 = jsonObject3.getString("extension1"); //是否是一周菜谱
                            int product_id = jsonObject3.getInt("product_id");
                            CartGoods cartGoods = new CartGoods(shopping_cart_id, 0, null, product_id, null, null, quantity, 0, extension1, null,0);
                            mList.add(cartGoods);
                        }
                    }
                    if (mList.size() == 0) {
                        mCartBadgeView.hide();
                    } else {
                        mCartBadgeView.setText(getAllGoodsCount() + "");
                        mCartBadgeView.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (what == ADDCART) {
                try {
                    String result = response.get();
                    JSONObject jsonObject = new JSONObject(result);
                    boolean resultADD = jsonObject.getBoolean("success");
                    //如果返回true 进行加法操作
                    if (resultADD) {
                        Toast.makeText(GoodDetailActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        mCartBadgeView.show();
                        mCartBadgeView.setText(getAllGoodsCount() + "");
                    } else {
                        Toast.makeText(GoodDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        mList.get(position).setQuantity(mList.get(position).getQuantity() - 1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (what == CREATECART) {
                try {
                    String result = response.get();
                    JSONObject jsonObject = new JSONObject(result);
                    boolean resultCreate = jsonObject.getBoolean("success");
                    if (resultCreate) {
                        Toast.makeText(GoodDetailActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                        int shopping_cart_id = jsonObject2.getInt("shopping_cart_id"); // 购物车id shopping_cart_id
                        int quantity = jsonObject2.getInt("quantity");//数量
                        String extension1 = jsonObject2.getString("extension1"); //是否是一周菜谱
                        int product_id = jsonObject2.getInt("product_id");
                        CartGoods cartGoods = new CartGoods(shopping_cart_id, -1, null, product_id, null, null, quantity, 0, extension1, null,-1);
                        mList.add(cartGoods);
                        mCartBadgeView.show();
                        mCartBadgeView.setText(getAllGoodsCount() + "");
                    } else {
                        Toast.makeText(GoodDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (what == GETCOMMENTNUM) {
                String result = response.get();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray array = jsonObject.getJSONArray("data");
                    int comment_num = array.length();
                    goods.setGoods_comment_count(comment_num);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                goodsCommentCountTextView.setText(goods.getGoods_comment_count() + "");
            }

        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

    //返回购物车商品数量
    private int getAllGoodsCount() {
        int count = 0;
        for (int i = 0; i < mList.size(); i++) {
            count += mList.get(i).getQuantity();
        }
        return count;
    }

}
