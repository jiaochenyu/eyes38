package com.example.eyes38.fragment.cart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.activity.PayActivity;
import com.example.eyes38.adapter.Cart_GoodsAdapter;
import com.example.eyes38.beans.CartGoods;
import com.example.eyes38.beans.Goods;
import com.example.eyes38.utils.LoadMoreFooterView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * Created by jqchen on 2016/5/20.
 */
public class CartGoodsList extends Fragment {
    private final static int mWhat = 381; //请求成功
    private final static int mFINFISH = 382; // 数据加载完成
    private static final int CARTGOODSCOUNT = 308; // 通知mainactivity 改变徽章
    private View mView;
    Toast mToast; // 吐司优化
    private List<CartGoods> mList;
    private List<CartGoods> payList;  // 选中商品
    private PtrClassicFrameLayout ptrFrame;
    ;  //刷新
    LinearLayoutManager linear;  //布局管理器
    private RecyclerView mRecyclerView;
    private TextView mCountTopTextView;  // n件商品有包邮优惠
    private TextView mDeleteOperationTV;// 编辑删除操作
    private CheckBox mTopAllGoodsCheckBox; // 全选含有优惠信息商品 的checkbox
    private CheckBox mCheckBoxAll; //全选商品的 checkbox
    private TextView mJiesuanTV; // 选中商品结算按钮
    private TextView mTotalPriceTV; // 选中的总金额
    private boolean allChecked = false; // 默认全选
    private boolean deleteChecked = false; // 点击按钮的状态
    Cart_GoodsAdapter mCart_goodsAdapter = null;
    //采用 NoHttp
    //创建 请求队列成员变量
    private RequestQueue mRequestQueue;
    Handler mainHandler = (new MainActivity()).mainHandler; //改变主页面的图标
    //Handler goodDetailHandler = (new GoodDetailActivity()).goodDetailHandler;// 改变购物商品详情的购物车徽章

    //Handler
    Handler mmHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case mFINFISH:
                    //加载数据完成
                    mCart_goodsAdapter = new Cart_GoodsAdapter(mList, getActivity(), mmHandler);
                    mRecyclerView.setAdapter(mCart_goodsAdapter);
                    mCart_goodsAdapter.notifyDataSetChanged();
                    mainHandler.sendMessage(mainHandler.obtainMessage(CARTGOODSCOUNT, mList.size()));  //通知改变徽章
                    initListener();
                    break;
                case Cart_GoodsAdapter.NOTIFICHANGEPRICE:
                    //更改购物车中商品总价格
                    float price = (float) msg.obj;
                    mTotalPriceTV.setText(price + "");
                    break;
                case Cart_GoodsAdapter.CARTGOODSCOUNT:
                    //更改 顶部 选中数量
                    mCountTopTextView.setText(msg.obj + "");
                    break;
                case Cart_GoodsAdapter.NOTIFICHANGEALLSELECTED:
                    //如果都被选中那么全选按钮也要被选中
                    //记录是否被全选
                    allChecked = !(Boolean) msg.obj;
                    mCheckBoxAll.setChecked((Boolean) msg.obj);
                    mTopAllGoodsCheckBox.setChecked((Boolean) msg.obj);
                    break;
                case Cart_GoodsAdapter.NOTIFILIST:
                    //通知改变了选中状态 目的是向结算界面中传递选中的list集合
                    payList = (List<CartGoods>) msg.obj;
                    break;

            }
            super.handleMessage(msg);
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.cart_goods, null);
        initViews();
        initData();
        getHttpMethod();
        return mView;
    }

    // 初始化界面
    private void initViews() {
        ptrFrame = (PtrClassicFrameLayout) mView.findViewById(R.id.car_goods_refresh);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.car_goods_list_rv);
        linear = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linear);
        mCountTopTextView = (TextView) mView.findViewById(R.id.checkedCountTop); // 顶部显示选中数量文本框
        mDeleteOperationTV = (TextView) mView.findViewById(R.id.DeleteOperationTV);  //编辑按钮
        mCheckBoxAll = (CheckBox) mView.findViewById(R.id.checkboxAllGoods);  //底部全选框
        mTopAllGoodsCheckBox = (CheckBox) mView.findViewById(R.id.allGoodsCheckboxTop); // 顶部全选框
        mJiesuanTV = (TextView) mView.findViewById(R.id.jiesuanButton);
        mTotalPriceTV = (TextView) mView.findViewById(R.id.allGoodsCountPrice);
    }

    private void initData() {
        mList = new ArrayList<>();
        payList = new ArrayList<CartGoods>();
    }

    private void initListener() {
        //测试 点击item 获取 Bean 对象
       /* mCart_goodsAdapter.setOnItemClickListener(new Cart_GoodsAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, CartGoods cartGoods) {
                Toast.makeText(mMainActivity, cartGoods.toString(), Toast.LENGTH_SHORT).show();
            }
        });*/
        ClickListener mClickListener = new ClickListener();
        mCheckBoxAll.setOnClickListener(mClickListener);
        mTopAllGoodsCheckBox.setOnClickListener(mClickListener);
        mDeleteOperationTV.setOnClickListener(mClickListener);
        mJiesuanTV.setOnClickListener(mClickListener);
        //利用 pulltorefesh 刷新
        LoadMoreFooterView header = new LoadMoreFooterView(mView.getContext()); //刷新动画效果 自定义
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
                        getHttpMethod();
                        ptrFrame.refreshComplete();
                    }
                }, 1800);
            }
        });
    }


    //请求http 获取购物车信息
    private void getHttpMethod() {
        mRequestQueue = NoHttp.newRequestQueue(); //默认是 3 个 请求
        String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.setRequestFailedReadCache(true); //缓存
        String username = "13091617887";  // 应该从偏好设置中获取账号密码
        String password = "jiao3992380";
        //Basic 账号+':'+密码  BASE64加密
        String addHeader = username + ":" + password;
        String authorization = "Basic " + new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));
        Log.e("authorization的值", authorization);
        request.addHeader("Authorization", authorization); // 添加请求头
        //request.add("shoppingCartIds", "");
        mRequestQueue.add(mWhat, request, mOnResponseListener);
    }

    /**
     * 请求http结果  回调对象，接受请求结果
     */
    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWhat) {

                mList = new ArrayList<>();
                //请求成功
                String result = response.get();
                //JSON解析
                jsonMethod(result);

                Message message = new Message();
                message.what = mFINFISH;
                //Log.e("请求完成", "66666666666666");
                mmHandler.sendMessage(message);
                //mCountTopTextView.setText(mList.size() + "");
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
        }

        @Override
        public void onFinish(int what) {
            //请求结束。 通知初始化 适配器
        }
    };


    //事件监听器  监听优惠信息复选框 编辑按钮 监听全选复选框 结算按钮
    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.allGoodsCheckboxTop:
                    selectedAll();
                    // 选中全部商品的商品
                    break;
                case R.id.DeleteOperationTV:
                    //编辑按钮删除操作  将删除按钮显示出来
                    showDelete();
                    break;
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


    private void showDelete() {
        // 将删除按钮显示出来
        mCart_goodsAdapter.setShowDelete(!deleteChecked);
        deleteChecked = !deleteChecked;
        mCart_goodsAdapter.notifyDataSetChanged();
    }

    // 全部选中
    private void selectedAll() {
        //Log.e("selectedAll","allchecked"+allChecked);
        for (int i = 0; i < mList.size(); i++) {
            //Cart_GoodsAdapter.getIsSelected().put(i,allChecked);
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
            Intent mIntent = new Intent(getActivity(), PayActivity.class);
            mIntent.putExtra("list", (Serializable) mmList);
            startActivity(mIntent);
        }
    }

    /*//json 解析
    private void jsonMethod(String result) {
        try {
            JSONObject object = new JSONObject(result);
            JSONObject object2 = object.getJSONObject("data");
            JSONArray mJsonArray = object2.getJSONArray("data");
            for (int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonObject = mJsonArray.getJSONObject(i);
                JSONArray mJsonArray2 = jsonObject.getJSONArray("data");
                for (int j = 0; i < mJsonArray2.length(); j++) {
                    JSONObject jsonObject2 = mJsonArray2.getJSONObject(j);
                    String product_name = jsonObject2.getString("product_name"); //商品名
                    int shopping_cart_id = jsonObject2.getInt("shopping_cart_id"); // 购物车id shopping_cart_id
                    int quantity = jsonObject2.getInt("quantity");//数量
                    boolean discount = jsonObject2.getBoolean("extension1") ; // 是否有优惠
                    Log.e("quantity", quantity + "");
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("product");
                    String path = jsonObject3.getString("image"); //获取图片路径
                    double price = jsonObject3.getDouble("price"); // 商品价格
                    //需要传到 商品详情(Goods) Bean
                    int goods_id = jsonObject3.getInt("product_id"); //ID
                    String goods_name = jsonObject3.getString("name");
                    String uri = jsonObject3.getString("image"); //图片
                    String goods_brand = null; //品牌
                    String goods_specification = null;
                    String goods_unit = jsonObject3.getString("extension4"); //单位
                    String goods_shengben = null;  //成本
                    String goods_remark = null; //备注 图文详情 是 description字段
                    float goods_market_price = (float) jsonObject3.getDouble("market_price"); //超市价格
                    float goods_platform_price = (float) jsonObject3.getDouble("price"); //平台价格
                    float goods_discount = 0;  //打折
                    JSONObject jsonObject4 = jsonObject3.getJSONObject("product_search");
                    int goods_comment_count = jsonObject4.getInt("comment_num");//评论数量
                    int goods_sales = jsonObject4.getInt("sales");   //销量
                    int goods_stock = jsonObject4.getInt("stock_num");

                    Goods mGoods = new Goods(goods_id, goods_name, uri,goods_unit, goods_market_price, goods_platform_price, goods_comment_count, goods_stock,goods_remark);


                    //mCartGoods.setPath("http://hz-ifs.ilexnet.com/eyes38/599334_1_pic500_120.jpg");
                    //CartGoods cartGoods = new CartGoods(path, product_name, price, quantity, goods_id, mGoods);

                    CartGoods cartGoods = new CartGoods();
                    cartGoods.setGoods(mGoods);  //商品信息 （bean）
                    cartGoods.setPath(path);
                    cartGoods.setTitle(product_name);
                    cartGoods.setNum(quantity);
                    cartGoods.setPrice(price);
                    cartGoods.setShopping_cart_id(shopping_cart_id);
                    cartGoods.setDiscount(discount);
                    mList.add(cartGoods);
                    Log.e("请求成功", mList.get(i).getTitle());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

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
                 /*   boolean extension1 = false;// 是否是一周菜谱
                    if (!jsonObject3.isNull("extension1")) {
                        extension1 = jsonObject3.getBoolean("extension1"); // 是否是一周菜谱
                    } else {
                        extension1 = false;
                    }*/
                    String extension1 = jsonObject3.getString("extension1");

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
                    CartGoods cartGoods = new CartGoods(shopping_cart_id, customer_id, customer_name, product_id, product_name, store_name, quantity, price, extension1, goods);
                    mList.add(cartGoods);
                    Log.e("mlist中的数据", mList.get(j).getProduct_name());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //显示吐司
    private void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRequestQueue.stop();
        linear.removeAllViews();
    }
}
