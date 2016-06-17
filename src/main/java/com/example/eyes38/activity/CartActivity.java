package com.example.eyes38.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eyes38.Application.Application;
import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.adapter.Cart_EmptyAdapter;
import com.example.eyes38.adapter.Cart_GoodsAdapter;
import com.example.eyes38.beans.CartGoods;
import com.example.eyes38.beans.Goods;
import com.example.eyes38.utils.LoadMoreFooterView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class CartActivity extends AppCompatActivity {
    private static final int EMPTY = 1;
    private static final int NOTEMPTY = 2;
    private final int IsCartEmptyWhat = 4;
    private final static int mFINFISH = 382; // 数据加载完成
    private static final int CARTGOODSCOUNT = 308; // 通知mainactivity 改变徽章
    //private View mView;
    Toast mToast; // 吐司优化
    private List<CartGoods> mList;
    private List<CartGoods> payList;  // 选中商品
    private PtrClassicFrameLayout ptrFrame;
    LinearLayoutManager linear;  //布局管理器
    RelativeLayout bottomRelativeLayout ;// 底部相对布局  全选 显示总金额。。。。
    private RecyclerView mRecyclerView;
    RequestQueue mRequestQueue;
    private SharedPreferences sp; // 偏好设置
    private CheckBox mCheckBoxAll; //全选商品的 checkbox
    private TextView mJiesuanTV; // 选中商品结算按钮
    private TextView mTotalPriceTV; // 选中的总金额
    private boolean allChecked = false; // 默认全选
    private ImageView mBackImageView;
    Handler mainHandler = (new MainActivity()).mainHandler; //改变主页面的图标
    Cart_GoodsAdapter mCart_goodsAdapter = null;
    Cart_EmptyAdapter mCart_emptyAdapter = null;

    Handler mmHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Cart_GoodsAdapter.NOTIFICHANGEPRICE:
                    //更改购物车中商品总价格
                    float price = (float) msg.obj;
                    DecimalFormat df = new DecimalFormat("0.00");
                    String st = df.format(price); //double 保留两位小数
                    mTotalPriceTV.setText(st + "");
                    break;
                case Cart_GoodsAdapter.NOTIFICHANGEALLSELECTED:
                    //如果都被选中那么全选按钮也要被选中
                    //记录是否被全选
                    allChecked = !(Boolean) msg.obj;
                    mCheckBoxAll.setChecked((Boolean) msg.obj);
                    break;
                case Cart_GoodsAdapter.NOTIFILIST:
                    //通知改变了选中状态 目的是向结算界面中传递选中的list集合
                    payList = (List<CartGoods>) msg.obj;
                    break;
                case Cart_GoodsAdapter.DeleteMethod:
                    initStates();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initViews();
        initData();
        //判断 购物车是否为空。 是空显示CartEmptyView
        initStates();
        initPTRListener();
    }


    private void initViews() {
        ptrFrame = (PtrClassicFrameLayout) findViewById(R.id.car_goods_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.car_goods_list_rv);
        linear = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linear);
        bottomRelativeLayout = (RelativeLayout) findViewById(R.id.cart_bottom_rl);
        mCheckBoxAll = (CheckBox)findViewById(R.id.checkboxAllGoods);  //底部全选框
        mJiesuanTV = (TextView)findViewById(R.id.jiesuanButton); //结算按钮
        mTotalPriceTV = (TextView) findViewById(R.id.allGoodsCountPrice); //总金额
        mBackImageView = (ImageView) findViewById(R.id.cartactivity_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        mRequestQueue = NoHttp.newRequestQueue(); //默认是 3 个 请求
        sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        mList = new ArrayList<>();
        payList = new ArrayList<CartGoods>();
    }

    private void initPTRListener(){
        //利用 pulltorefesh 刷新
        LoadMoreFooterView header = new LoadMoreFooterView(this); //刷新动画效果 自定义
        ptrFrame.setHeaderView(header); //刷新动画效果
        ptrFrame.addPtrUIHandler(header); //刷新动画效果
        //刷新方法
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // getHttpMethod();
                        initStates();
                        ptrFrame.refreshComplete();
                    }
                }, 1800);
            }
        });

    }
    private void initStates() {
        //如果用户没有登录 那么显示空
        if (!Application.isLogin) {
            //如果用户没登录  购物车显示空
            bottomRelativeLayout.setVisibility(View.GONE);
            mCart_emptyAdapter = new Cart_EmptyAdapter(this);
            mRecyclerView.setAdapter(mCart_emptyAdapter);
        } else {
            //登录了 进行网络请求 判断用户购物车是否为空
            getIsCartEmptyHttpMethod();
        }
    }

    private void initOnclickListener() {
        ClickListener mClickListener = new ClickListener();
        mCheckBoxAll.setOnClickListener(mClickListener);
        mJiesuanTV.setOnClickListener(mClickListener);

    }

    //事件监听器  监听优惠信息复选框 编辑按钮 监听全选复选框 结算按钮
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.checkboxAllGoods:
                    //选中全部商品
                    selectedAll();
                    break;
                case R.id.jiesuanButton:
                    //结算购物车
                    goPay();
                    break;
            }
        }
    }


    ///获取用户名和密码
    private String authorization() {
        String username = sp.getString("USER_NAME", "");  // 应该从偏好设置中获取账号密码
        String password = sp.getString("PASSWORD", "");
        //Basic 账号+':'+密码  BASE64加密
        String addHeader = username + ":" + password;
        String authorization = "Basic " + new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));
        return authorization;
    }

    private void getIsCartEmptyHttpMethod(){
        //判断购物车是否为空
        String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.addHeader("Authorization", authorization());
        mRequestQueue.add(IsCartEmptyWhat, request, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == IsCartEmptyWhat) {
                //请求成功
                try {
                    mList = new ArrayList<>();
                    String result = response.get();
                    //JSON解析
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        //不为空显示购物车
                        //显示底部
                        jsonMethod(result);
                        bottomRelativeLayout.setVisibility(View.VISIBLE);
                        mCart_goodsAdapter = new Cart_GoodsAdapter(mList,CartActivity.this,mmHandler);
                        mRecyclerView.setAdapter(mCart_goodsAdapter);
                        mainHandler.sendMessage(mainHandler.obtainMessage(CARTGOODSCOUNT, getAllGoodsCount()));  //通知改变徽章
                        initOnclickListener();
                    } else {
                        //为空
                        bottomRelativeLayout.setVisibility(View.GONE);
                        mCart_emptyAdapter = new Cart_EmptyAdapter(CartActivity.this);
                        mRecyclerView.setAdapter(mCart_emptyAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
        }

        @Override
        public void onFinish(int what) {
        }
    };
    private void jsonMethod(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                JSONArray jsonArray2 = jsonObject2.getJSONArray("data");
                for (int j = 0; j < jsonArray2.length(); j++) {
                    JSONObject jsonObject3 = jsonArray2.getJSONObject(j);
                    String product_name = jsonObject3.getString("product_name"); //商品名
                    int shopping_cart_id = jsonObject3.getInt("shopping_cart_id"); // 购物车id shopping_cart_id
                    int quantity = jsonObject3.getInt("quantity");//数量
                    String extension1 = jsonObject3.getString("extension1"); //是否是一周菜谱
                    int customer_id = jsonObject3.getInt("customer_id");
                    String customer_name = jsonObject3.getString("customer_name");
                    int product_id = jsonObject3.getInt("product_id");
                    String store_name = jsonObject3.getString("store_name");
                    double price = jsonObject3.getDouble("price");
                    /********  Goods  *********/
                    JSONObject jsonObject4 = jsonObject3.getJSONObject("product");
                    int goods_id = jsonObject4.getInt("product_id");
                    String goods_name = jsonObject4.getString("name");
                    String path = jsonObject4.getString("image");
                    String goods_unit = jsonObject4.getString("extension4");//单位
                    float goods_market_price = (float) jsonObject4.getDouble("market_price");
                    String goods_description = jsonObject4.getString("description");  //商品图文描述

                    JSONObject jsonObject5 = jsonObject4.getJSONObject("product_search");
                    int goods_comment_count = jsonObject5.getInt("comment_num");    //评论数量
                    int goods_stock = jsonObject5.getInt("stock_num");   //库存
                    Goods goods = new Goods(goods_id, goods_name, path, goods_unit, goods_market_price, (float) price, goods_comment_count, goods_stock, goods_description);
                    CartGoods cartGoods = new CartGoods(shopping_cart_id, customer_id, customer_name, product_id, product_name, store_name, quantity, price, extension1, goods,2);
                    mList.add(cartGoods);
                }
            }
            //把mlist的0位置处添加viewtype布局类型
            CartGoods cartGoods = new CartGoods();
            cartGoods.setViewType(1);
            mList.add(0,cartGoods);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 全部选中
    private void selectedAll() {
        for (int i = 1; i < mList.size(); i++) {
            //设置beans
            mList.get(i).setSelected(allChecked);
        }
        mCart_goodsAdapter.notifyDataSetChanged();
    }
    //******* 去结算 ************
    private void goPay() {
        //先去判断哪些商品被选中了 然后给PayActivity传一个list
        CartGoods payGoods = new CartGoods();
        List<CartGoods> mmList = new ArrayList<>();
        for (int i = 0; i < payList.size(); i++) {
            if (payList.get(i).isSelected()) {
                payGoods = payList.get(i);
                mmList.add(payGoods);
            }
        }
        if (mmList.size() == 0) {
            showToast("没有选中商品");
        } else {
            Intent mIntent = new Intent(CartActivity.this, PayActivity.class);
            mIntent.putExtra("list", (Serializable) mmList);
            startActivity(mIntent);
        }
    }

    //返回购物车商品数量
    private int getAllGoodsCount() {
        int count = 0;
        for (int i = 0; i < mList.size(); i++) {
            count += mList.get(i).getQuantity();
        }
        return count;
    }


    //显示吐司
    private void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(CartActivity.this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIsCartEmptyHttpMethod();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRequestQueue.stop();
        linear.removeAllViews();
    }
}
