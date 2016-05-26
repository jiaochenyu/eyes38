package com.example.eyes38.fragment.cart;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.adapter.Cart_GoodsAdapter;
import com.example.eyes38.beans.CartGoods;
import com.example.eyes38.beans.Goods;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jqchen on 2016/5/20.
 */
public class CartGoodsList extends Fragment {
    private final static int mWhat = 381; //请求成功
    private final static int mFINFISH = 382; // 数据加载完成
    private View mView;
    MainActivity mMainActivity;
    private List<CartGoods> mList;
    private RecyclerView mRecyclerView;
    private TextView mCountTopTextView;  // n件商品有包邮优惠
    private TextView mDeleteOperationTV;// 编辑删除操作
    private CheckBox mDiscountCheckBox; // 全选含有优惠信息商品 的checkbox
    private CheckBox mCheckBoxAll; //全选商品的 checkbox
    private TextView mJiesuanTV; // 选中商品结算按钮
    private TextView mTotalPriceTV; // 选中的总金额
    private boolean allChecked = false; // 默认全选
    private boolean deleteChecked = false ; // 点击按钮的状态
    Cart_GoodsAdapter mCart_goodsAdapter = null;
    //采用 NoHttp
    //创建 请求队列成员变量
    private RequestQueue mRequestQueue;
    //Handler
    private Handler mmHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case mFINFISH:
                    //加载数据完成
                    //mCart_goodsAdapter.notifyDataSetChanged();
                    //selectedAll();

                    initListener();
                    break;
                case Cart_GoodsAdapter.NOTIFICHANGEPRICE:
                    //更改购物车中商品总价格
                    float price = (float) msg.obj;
                    mTotalPriceTV.setText(price + "");
                    break;
                case Cart_GoodsAdapter.NOTIFICHANGEALLSELECTED:
                    //如果都被选中那么全选按钮也要被选中
                    //记录是否被全选
                    allChecked = !(Boolean) msg.obj;
                    mCheckBoxAll.setChecked((Boolean) msg.obj);
                    break;

            }
            super.handleMessage(msg);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        mView = inflater.inflate(R.layout.cart_goods, null);
        initViews();
        initData();
        getHttpMethod();
        // initListener();
        return mView;
    }

    // 初始化界面
    private void initViews() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.car_goods_list_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCountTopTextView = (TextView) mView.findViewById(R.id.checkedCountTop);
        mDeleteOperationTV = (TextView) mView.findViewById(R.id.DeleteOperationTV);  //编辑按钮
        mCheckBoxAll = (CheckBox) mView.findViewById(R.id.checkboxAllGoods);
        mDiscountCheckBox = (CheckBox) mView.findViewById(R.id.DiscountCheckbox);
        mJiesuanTV = (TextView) mView.findViewById(R.id.jiesuanButton);
        mTotalPriceTV = (TextView) mView.findViewById(R.id.allGoodsCountPrice);
    }

    private void initData() {
        mList = new ArrayList<>();
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
        mDeleteOperationTV.setOnClickListener(mClickListener);

    }


    //请求http 获取购物车信息
    private void getHttpMethod() {
        mRequestQueue = NoHttp.newRequestQueue(); //默认是 3 个 请求
        String url = "http://api.dev.ilexnet.com/simulate/38eye/cart-api/cart";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.setRequestFailedReadCache(true);
        //request.add("shoppingCartIds", "219");
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
                Log.e("请求成功", "success");
                mList = new ArrayList<>();
                //请求成功
                String result = response.get();
                //JSON解析
                jsonMethod(result);
                mCart_goodsAdapter = new Cart_GoodsAdapter(mList, mMainActivity, mmHandler);
                mRecyclerView.setAdapter(mCart_goodsAdapter);
                mCart_goodsAdapter.notifyDataSetChanged();
                Message message = new Message();
                message.what = mFINFISH;
                //Log.e("请求完成", "66666666666666");
                mmHandler.sendMessage(message);
                mCountTopTextView.setText(mList.size() + "");
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
                case R.id.DiscountCheckbox:
                    // 选中含有优惠信息的商品
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

    //json 解析
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
                    int quantity = jsonObject2.getInt("quantity"); //数量
                    Log.e("quantity",quantity+"");
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

                    Goods mGoods = new Goods(goods_id, goods_name, uri, goods_brand, goods_specification, goods_unit, goods_shengben, goods_remark, goods_market_price, goods_platform_price, goods_discount, goods_comment_count, goods_stock);


                    //mCartGoods.setPath("http://hz-ifs.ilexnet.com/eyes38/599334_1_pic500_120.jpg");
                    //CartGoods cartGoods = new CartGoods(path, product_name, price, quantity, goods_id, mGoods);

                    CartGoods cartGoods = new CartGoods();
                    cartGoods.setPath(path);
                    cartGoods.setTitle(product_name);
                    cartGoods.setNum(quantity);
                    cartGoods.setPrice(price);
                    cartGoods.setGoods(mGoods);
                    cartGoods.setShopping_cart_id(shopping_cart_id);
                    mList.add(cartGoods);
                    //Log.e("mlist", mList.size()+""+mList.toString());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
