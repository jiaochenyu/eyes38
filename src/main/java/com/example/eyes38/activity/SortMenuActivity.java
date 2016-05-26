package com.example.eyes38.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.adapter.Sort_SortAdapter;
import com.example.eyes38.beans.Goods;
import com.example.eyes38.beans.SortContentContent;
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

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class SortMenuActivity extends AppCompatActivity {
    public static final int FINISHED = 1;
    RecyclerView mRecyclerView;
    Sort_SortAdapter sort_sortAdapter;
    List<Goods> mList;
    //分类导航栏
    private RadioGroup mRadioGroup;
    private TextView titleTextView;
    private String titlecontent;
    //刷新界面
    private PtrClassicFrameLayout ptrFrame;

    //测试获取json数据
    //创建 请求队列成员变量
    private RequestQueue mRequestQueue;
    private final static int mWhat = 520;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_menu);
        initView();
        initData();
        getDatas();
        setUI();
        initListener();

    }

    private void initListener() {
        LoadMoreFooterView header = new LoadMoreFooterView(this);
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);
        //刷新
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
                        Log.e("load","加载了");
                        getHttpMedthod();
                        ptrFrame.refreshComplete();
                    }
                },1800);

            }
        });
    }

    private void setUI() {
        //设置标题栏的内容
        titleTextView.setText(titlecontent);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FINISHED:
                    initAdapter();
                    setRadioGroupListener();
                    break;
            }
        }
    };
    private void setRadioGroupListener() {
        //对分类导航栏监听
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                resetDatas(checkedId);
            }
        });
    }


    private void resetDatas(int checkedId) {
        //更改mlist中的数据，并通知适配器来更新UI
        mList.clear();
        switch (checkedId) {
            case R.id.sort_menu_default:
                Goods g1 = new Goods(1, "默认", null, "水果", "100g", "10/100g", null, null, 11f, 10f, 0, 0, 100);
                mList.add(g1);
                break;
            case R.id.sort_menu_new:
                Goods g2 = new Goods(1, "最新", null, "水果", "100g", "10/100g", null, null, 11f, 10f, 0, 0, 100);
                mList.add(g2);
                break;
            case R.id.sort_menu_sale:
                Goods g3 = new Goods(1, "销量", null, "水果", "100g", "10/100g", null, null, 11f, 10f, 0, 0, 100);
                mList.add(g3);
                break;
            case R.id.sort_menu_price:
                Goods g4 = new Goods(1, "价格", null, "水果", "100g", "10/100g", null, null, 11f, 10f, 0, 0, 100);
                mList.add(g4);
                break;
        }
        sort_sortAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.sort_sort_recyclerview);
        //显示两列
        GridLayoutManager grid = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(grid);
        //分类界面的导航栏
        mRadioGroup = (RadioGroup) findViewById(R.id.sort_menu);
        titleTextView = (TextView) findViewById(R.id.sort_sort_title);
        ptrFrame = (PtrClassicFrameLayout) findViewById(R.id.sort_sort_ptr);
    }

    private void initData() {
        getHttpMedthod();
        /*Goods g1 = new Goods(1, "苹果", null, "水果", "100g", "10/100g", null, null, 11f, 10f, 0, 0, 100);
        Goods g2 = new Goods(2, "苹果", null, "水果", "100g", "10/100g", null, null, 11f, 10f, 0, 0, 100);
        Goods g3 = new Goods(3, "苹果", null, "水果", "100g", "10/100g", null, null, 11f, 10f, 0, 0, 100);
        Goods g4 = new Goods(4, "苹果", null, "水果", "100g", "10/100g", null, null, 11f, 10f, 0, 0, 100);
        mList.add(g1);
        mList.add(g2);
        mList.add(g3);
        mList.add(g4);*/


    }
    private void getHttpMedthod() {
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile//product-api/products";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
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
                //请求成功
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    mList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        int id = jsonObject.getInt("product_id");
                        String name = jsonObject.getString("name");
                        String path = jsonObject.getString("image");
                        String unit = jsonObject.getString("extension4");
                        String txt_pic = jsonObject.getString("description");
                        float price = (float) jsonObject.getDouble("price");
                        float market_price = (float) jsonObject.getDouble("market_price");
                        JSONObject search = jsonObject.getJSONObject("product_search");
                        int comment_count = search.getInt("comment_num");
                        int stock = search.getInt("stock_num");
                        Goods goods = new Goods(id, "默认", path, "水果", txt_pic, unit, null, name, market_price, price, 0, comment_count, stock);

                        mList.add(goods);
                    }
                    handler.sendEmptyMessage(FINISHED);
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

    //获取传来的商品的id
    private void getDatas(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("values");
        SortContentContent scc = (SortContentContent) bundle.get("values");
        titlecontent = scc.getConten();
    }

    private void initAdapter() {
        sort_sortAdapter = new Sort_SortAdapter(this, mList);
        mRecyclerView.setAdapter(sort_sortAdapter);
        sort_sortAdapter.setmOnItemClickListener(new Sort_SortAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Goods goods) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("values",goods);
                Intent intent = new Intent(SortMenuActivity.this, GoodDetailActivity.class);
                intent.putExtra("values",bundle);
                startActivity(intent);
            }
        });
    }
}
