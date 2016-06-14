package com.example.eyes38.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eyes38.R;
import com.example.eyes38.adapter.Sort_SortAdapter;
import com.example.eyes38.beans.Goods;
import com.example.eyes38.beans.SortContentContent;
import com.example.eyes38.utils.LoadMoreFooterView;
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

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class SortMenuActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Sort_SortAdapter sort_sortAdapter;
    private List<Goods> mList;
    private TextView titleTextView;
    //分类标题
    private String titlecontent;
    //分类的id
//    private int category_id;
    //刷新界面
    private PtrClassicFrameLayout ptrFrame;
    private ImageView loading;

    //测试获取json数据
    //创建 请求队列成员变量
    private RequestQueue mRequestQueue;
    private final static int mWhat = 520;
    private GridLayoutManager grid;
    //上拉加载的footview
    private LinearLayout footview;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            footview.setVisibility(View.GONE);
            getHttpMedthod();
        }
    };
    //记录请求次数
    private int count = 1;
    //表示是否还有数据可以加载
    private boolean isLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_menu);
        initView();
        getDatas();
        initAdapter();
        initData();
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

                        ptrFrame.refreshComplete();
                    }
                }, 1800);

            }
        });
        //上拉加载
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isScrolling = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && isScrolling && isLoad) {
                    int lastVisibleItem = grid.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = grid.getItemCount();
                    if (lastVisibleItem == totalItemCount - 1) {
                        //显示footview
                        footview.setVisibility(View.VISIBLE);
                        loading.setBackgroundResource(R.drawable.anim);
                        /*final AnimationDrawable animDrawable = (AnimationDrawable) loading
                                .getBackground();
                        loading.post(new Runnable() {
                            @Override
                            public void run() {
                                animDrawable.start();
                            }
                        });*/
                        handler.sendMessageDelayed(new Message(), 2000);
//                        loadMoreData();
//                        Log.e("load","加载");
//                        加载数据
                        isScrolling = false;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isScrolling = (dy > 0);
            }
        });
    }
    //测试用
    /*private void loadMoreData() {
        //加载数据
        for (int i = 0; i < 5; i++) {
            Goods goods = new Goods(1, "默认", "", "new", 0, 0, 0, 0, "");
            mList.add(goods);
        }
        sort_sortAdapter.notifyDataSetChanged();

    }*/

    private void setUI() {
        //设置标题栏的内容
        titleTextView.setText(titlecontent);
    }


    private void setRadioGroupListener() {
        //对分类导航栏监听
        /*mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                resetDatas(checkedId);
            }
        });*/
    }


    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.sort_sort_recyclerview);
        //显示两列
        grid = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(grid);
        //分类界面的导航栏
//        mRadioGroup = (RadioGroup) findViewById(R.id.sort_menu);
        titleTextView = (TextView) findViewById(R.id.sort_sort_title);
        ptrFrame = (PtrClassicFrameLayout) findViewById(R.id.sort_sort_ptr);
        footview = (LinearLayout) findViewById(R.id.footview);
        loading = (ImageView) findViewById(R.id.footview_image);
    }

    private void initData() {
        mRequestQueue = NoHttp.newRequestQueue();
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
        String url = "http://38eye.test.ilexnet.com/api/mobile//product-api/products";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        //request.add("limit", "28");
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
//        request.add("category_id",category_id);
        //一次请求六条数据
        request.add("limit", 8);
        //请求第几页
        request.add("page", count);
        count++;
        //request.setRequestFailedReadCache(true);
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
                    if (array.length() != 0) {
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
                            Goods goods = new Goods(id, name, path, unit, market_price, price, comment_count, stock, txt_pic);
                            mList.add(goods);
                        }
                        sort_sortAdapter.notifyDataSetChanged();
                    } else {
                        isLoad = false;
                    }
                    setRadioGroupListener();
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
    private void getDatas() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("values");
        SortContentContent scc = (SortContentContent) bundle.get("values");
        if (scc != null){
            titlecontent = scc.getConten();
            //分类的id 因为数据量少，暂时未用到
//            category_id = scc.getId();
        }
    }

    private void initAdapter() {
        //初始化适配器
        mList = new ArrayList<>();
        sort_sortAdapter = new Sort_SortAdapter(this, mList);
        mRecyclerView.setAdapter(sort_sortAdapter);
        sort_sortAdapter.setmOnItemClickListener(new Sort_SortAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Goods goods) {
                Intent intent = new Intent(SortMenuActivity.this, GoodDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("values", goods);
                intent.putExtra("values", bundle);
                //intent.putExtra("values",goods);
                startActivity(intent);
            }
        });
    }

    public void back(View view) {
        finish();
    }
}
