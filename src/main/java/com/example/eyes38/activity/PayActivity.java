package com.example.eyes38.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eyes38.R;
import com.example.eyes38.adapter.PayAdapter;
import com.example.eyes38.beans.CartGoods;
import com.example.eyes38.beans.Receipt;
import com.example.eyes38.user_activity.User_orderActivity;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import c.b.BP;
import c.b.PListener;


public class PayActivity extends AppCompatActivity {
    public final static int mAdressIDWhat = 1;
    public final static int mDefaultAdressWhat = 2;
    public final static int mPeiSong = 3;
    public final static int mJPUSHWhat = 4;
    public final static int mDELETEFINISH = 5; //删除购物车
    private ImageView payBackImag; //返回
    private RelativeLayout mPayAddressRl, mNotEmptyRl;
    private TextView allGoodsPrice, peisongMoney, totalPrice; //收货地址(显示默认)、总价、运费、总价格(商品价格+运费)
    private TextView emptyTV, firstNameTV, phoneTV, districtTV;
    private TextView goPay;  // 去付款
    private RadioGroup mPayRadioGroup; //支付方式
    private RadioButton mWeChatRadioButton; //微信支付
    private RadioButton alipayRadioButton; //微信支付
    private RecyclerView mGoodsRecyclerView; // 显示订单商品
    private List<CartGoods> mList; // 把购物车选中的商品信息传过来
    private float peisong = 0;
    private RequestQueue mRequestQueue;
    private SharedPreferences sp; //偏好设置 获取密码和用户id
    PayAdapter mPayAdapter;
    private List<Receipt> mReceiptList;  // 默认地址
    private Receipt mReceipt;
    private String address_id; //收货地址id
    private float jiesuanMoney;  //总金额 商品金额+配送费用
    private int PLUGINVERSION = 7;
    private boolean payStyle = false;
    private Toast mToast;

    public float getPeisong() {
        return peisong;
    }

    public void setPeisong(float peisong) {
        this.peisong = peisong;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public float getJiesuanMoney() {
        return jiesuanMoney;
    }

    public void setJiesuanMoney(float jiesuanMoney) {
        this.jiesuanMoney = jiesuanMoney;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_jiesuan);
        initViews();
        initData();
        setView();
        initListener();
        getAdressIDNoHttp();
        getPeiSongMoneyNoHttp();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mReceipt = (Receipt) intent.getSerializableExtra("addressInfo");
        firstNameTV.setText(mReceipt.getReceipt_person());
        phoneTV.setText(mReceipt.getReceipt_phone());
        districtTV.setText(mReceipt.getDistrict() + " " + mReceipt.getReceipt_detail());
    }


    private void initViews() {
        mWeChatRadioButton = (RadioButton) findViewById(R.id.weChatPay);
        alipayRadioButton = (RadioButton) findViewById(R.id.alipayPay);
        goPay = (TextView) findViewById(R.id.gopay);
        payBackImag = (ImageView) findViewById(R.id.pay_back);
        mPayAddressRl = (RelativeLayout) findViewById(R.id.pay_adress);
        mNotEmptyRl = (RelativeLayout) findViewById(R.id.gopay_address_notempty);
        emptyTV = (TextView) findViewById(R.id.gopay_address_empty);   //为空收货地址
        firstNameTV = (TextView) findViewById(R.id.firstname);
        phoneTV = (TextView) findViewById(R.id.phone);
        districtTV = (TextView) findViewById(R.id.district);
        allGoodsPrice = (TextView) findViewById(R.id.allGoodsPrice);  //商品价格
        peisongMoney = (TextView) findViewById(R.id.peisongmoney); // 配送费用
        totalPrice = (TextView) findViewById(R.id.totalPrice);  // 总价 商品价格+配送费用
        mGoodsRecyclerView = (RecyclerView) findViewById(R.id.orderGoodsInfo);
        mGoodsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void setView() {
        peisongMoney.setText(getPeisong() + "");  //配送价格
        DecimalFormat df = new DecimalFormat("0.00");
        String st = df.format(allGoodsPrice()); // 商品价格
        allGoodsPrice.setText(st);
    }

    private void initData() {
        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE); //初始化偏好设置
        mList = new ArrayList<>();
        mReceiptList = new ArrayList<>();
        mReceipt = new Receipt();

        Intent intent = getIntent();
        mList = (List<CartGoods>) intent.getSerializableExtra("list"); // 从购物车中获取list

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
        mPayAddressRl.setOnClickListener(clickListener);
        goPay.setOnClickListener(clickListener);
        mWeChatRadioButton.setOnClickListener(clickListener);
        alipayRadioButton.setOnClickListener(clickListener);
    }

    private void gopay(boolean style) {
        //第三方支付
        int pluginVersion = BP.getPluginVersion();
        if (pluginVersion < PLUGINVERSION) {// 为0说明未安装支付插件, 否则就是支付插件的版本低于官方最新版
            Show(pluginVersion == 0 ? "监测到本机尚未安装支付插件,无法进行支付,请先安装插件(无流量消耗)"
                    : "监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)");
            installBmobPayPlugin("bp.db");
        }
        pay(style, jiesuanMoney);
    }


    //**********账号密码进行base64加密
    private String authorization() {
        String username = sp.getString("USER_NAME", "");  // 应该从偏好设置中获取账号密码
        String password = sp.getString("PASSWORD", "");
        //Basic 账号+':'+密码  BASE64加密
        String addHeader = username + ":" + password;
        String authorization = "Basic " + new String(Base64.encode(addHeader.getBytes(), Base64.DEFAULT));
        return authorization;
    }

    // -*****获取默认收货地址 首先获取用户id根据用户信息获取默认地址id
    private void getAdressIDNoHttp() {
        String customerID = sp.getString("CUSTOMER_ID", "");
        String path = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customers/" + customerID;
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(path, RequestMethod.GET);
        request.addHeader("Authorization", authorization());
        mRequestQueue.add(mAdressIDWhat, request, mOnResponseListener);
    }

    //根据收货地址id获取District(省市区)详细信息
    private void getDistrictNoHttp() {
        String customerID = sp.getString("CUSTOMER_ID", "");
        String path = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customer-addresses";
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(path, RequestMethod.GET);
        request.addHeader("Authorization", authorization());
        request.add("customer_id", customerID);
        mRequestQueue.add(mDefaultAdressWhat, request, mOnResponseListener);

    }

    //获取配送费用
    private void getPeiSongMoneyNoHttp() {
        String path = "http://38eye.test.ilexnet.com/api/mobile/setting/getShippingProperties";
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(path, RequestMethod.GET);
        request.addHeader("Authorization", authorization());
        request.add("district_id", "2145");
        mRequestQueue.add(mPeiSong, request, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mAdressIDWhat) {
                try {
                    String result = response.get();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    String address_id = jsonData.getString("address_id"); //默认收货地址
                    setAddress_id(address_id);
                    //执行获取
                    getDistrictNoHttp();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (what == mDefaultAdressWhat) {
                try {
                    String result = response.get();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        String addressid = jsonData.getString("address_id");
                        if (address_id.equals(addressid)) {
                            String receipt_person = jsonData.getString("firstname");
                            String receipt_phone = jsonData.getString("mobile");
                            String receipt_detail = jsonData.getString("address_1");
                            String district = jsonData.getString("district");
                            mReceipt.setReceipt_person(receipt_person);
                            mReceipt.setReceipt_phone(receipt_phone);
                            mReceipt.setReceipt_detail(receipt_detail);
                            mReceipt.setDistrict(district);
                            mReceiptList.add(mReceipt);
                            mNotEmptyRl.setVisibility(View.VISIBLE);
                            emptyTV.setVisibility(View.GONE);
                            firstNameTV.setText(receipt_person);
                            phoneTV.setText(receipt_phone);
                            districtTV.setText(district + " " + receipt_detail);
                        }
                    }
                    if (mReceiptList.size() == 0) {
                        emptyTV.setVisibility(View.VISIBLE);
                        mNotEmptyRl.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (what == mPeiSong) {
                try {
                    String result = response.get();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    float delivery_money = (float) jsonData.getDouble("delivery_money");
                    setPeisong(delivery_money);
                    setJiesuanMoney((float) (allGoodsPrice() + delivery_money));
                    DecimalFormat df = new DecimalFormat("0.00");
                    String st1 = df.format(delivery_money); // 配送价格
                    peisongMoney.setText(st1);
                    String st2 = df.format(allGoodsPrice() + getPeisong()); //商品价格 + 配送价格
                    totalPrice.setText(st2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (what == mJPUSHWhat) {
                //极光推送
            }
            if (what == mDELETEFINISH){
                //删除购物车
            }

        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }

        @Override
        public void onFinish(int what) {

        }
    };


    //事件监听
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
                case R.id.pay_adress:
                    //点击这个跳转到选择地址界面
                    Intent intent = new Intent(PayActivity.this, PaySelectActivity.class);
                    startActivity(intent);
                    break;
                case R.id.weChatPay:
                    //选择了微信支付方式
                    payStyle = false;
                    break;
                case R.id.alipayPay:
                    //选择了支付宝支付方式
                    payStyle = true;
                    break;
                case R.id.gopay:
                    //付款之前先判断收货地址 和支付方式
                    gopay(payStyle);
                    break;
            }
        }
    }

    private double allGoodsPrice() {
        double goodsPrice = 0;
        for (int i = 0; i < mList.size(); i++) {
            goodsPrice += mList.get(i).getPrice() * mList.get(i).getQuantity();
        }
        return goodsPrice;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void Show(String text) {
        //显示信息
        if (mToast == null) {
            mToast = Toast.makeText(PayActivity.this, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    private void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pay(final boolean alipayOrWechatPay, float countMoney) {
        Show("正在获取订单...");
        //BP.pay("商品", "商品", countMoney, alipayOrWechatPay, new PListener() {
        BP.pay("商品", "商品", 0.01, alipayOrWechatPay, new PListener() {
            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Show("支付结果未知,请稍后手动查询");
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Show("支付成功!");
                //推送消息
                pushMessage();
                //并且删除购物车
                getDeleteNoHttpMethod();
                //跳转到个人中心代发货
                Intent intent = new Intent(PayActivity.this, User_orderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("TAG",2);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                Show("获取订单成功!请等待跳转到支付页面~");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {

                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    Show("监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付");
                    installBmobPayPlugin("bp.db");
                } else {
                    Show("支付取消!");
                }
            }
        });
    }

    private void pushMessage() {
        String id = sp.getString("CUSTOMER_ID", "");
        String name = sp.getString("USER_NAME", "");
        String path = "http://headvip.cn/JPush_web/PushServlet";
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(path, RequestMethod.GET);
        String content = name + "您以完成支付，谢谢您对我们的支持。";
        request.add("content", content);
        request.add("id", id);
        mRequestQueue.add(mJPUSHWhat, request, mOnResponseListener);
    }

    private void getDeleteNoHttpMethod() {
        for (int i = 0; i < mList.size(); i++) {
            String url = "http://38eye.test.ilexnet.com/api/mobile/cart-api/cart/" + mList.get(i).getShopping_cart_id();
            mRequestQueue = NoHttp.newRequestQueue();
            Request<String> request = NoHttp.createStringRequest(url, RequestMethod.DELETE);
            request.addHeader("Authorization", authorization()); // 添加请求头
            mRequestQueue.add(mDELETEFINISH, request, mOnResponseListener);

        }

    }
}
