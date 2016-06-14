package com.example.eyes38.EventActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.eyes38.R;
import com.example.eyes38.activity.GoodDetailActivity;
import com.example.eyes38.adapter.EventRecycleViewAdapter;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by weixiao on 2016/5/24.
 */
public class EventDay2 extends Fragment{
    private static final int mWhat = 1;
    private static final int ptrWhat = 2;
    View view;
    RecyclerView mRecyclerView;
    private RequestQueue mRequestQueue;
    private PtrClassicFrameLayout ptrFrame;
    private List<Goods> mList;
    private GridLayoutManager mGridLayoutManager;
    private EventRecycleViewAdapter mEventRecycleViewAdapter;
    private LinearLayout footView; // 显示加载底部布局
    private ImageView loading;
    private int districtID;
    //记录请求次数
    private int count = 1;
    //表示是否还有数据可以加载
    private boolean isLoad = true;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            footView.setVisibility(View.GONE);
            getHttpMethod();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.event_day2, null);
        initViews();
        initData();
        initListener();
        initPTRListener();
        loadMoreListener();
        getPTRHttpMethod();
        return view;
    }

    private void initViews() {
        ptrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.event_day2_refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.event_day2_recyclerview);
        //设置cecycleview的布局管理
        //StaggeredGridLayoutManager 实现瀑布流
       /* StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        EventSpacesItemDecoration decoration = new EventSpacesItemDecoration(16); // 设置item 的间距
        mRecyclerView.addItemDecoration(decoration);*/
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        footView = (LinearLayout) view.findViewById(R.id.footview);
        loading = (ImageView) view.findViewById(R.id.footview_image);

    }

    //初始化数据
    private void initData() {
        mList = new ArrayList<>();
        mEventRecycleViewAdapter = new EventRecycleViewAdapter(mList, getActivity());
        mRecyclerView.setAdapter(mEventRecycleViewAdapter);
        //获取district id
        Intent intent = getActivity().getIntent();
        districtID = intent.getIntExtra("values", -1);
    }

    //下拉刷新
    private void initPTRListener() {
        //利用 pulltorefesh 刷新
        LoadMoreFooterView header = new LoadMoreFooterView(getActivity()); //刷新动画效果 自定义
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
                        getPTRHttpMethod();
                        ptrFrame.refreshComplete();
                    }
                }, 1800);
            }
        });
    }

    //上拉加载
    private void loadMoreListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isScrolling = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == recyclerView.SCROLL_STATE_IDLE && isScrolling && isLoad) {
                    int lastVisibleItem = mGridLayoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = mGridLayoutManager.getItemCount();
                    if (lastVisibleItem == totalItemCount - 1) {
                        //显示footview
                        footView.setVisibility(View.VISIBLE);
                        loading.setBackgroundResource(R.drawable.anim);
                        mHandler.sendMessageDelayed(new Message(), 2000);
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

    private void initListener() {
        mEventRecycleViewAdapter.setOnRecyclerViewItemClickListener(new EventRecycleViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void OnItemClick(View view, Goods goods) {
                Intent intent = new Intent(getActivity(), GoodDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("values", goods);
                intent.putExtra("values", bundle);
                getActivity().startActivity(intent);
            }
        });
    }

    //下拉刷新
    public void getPTRHttpMethod() {
        mRequestQueue = NoHttp.newRequestQueue();//默认是3个请求
        String url = "http://38eye.test.ilexnet.com/api/mobile/special-product/list/";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("district_id", districtID);
        request.add("type", "week");
        //一次请求8
        request.add("limit", 8);
        //请求第几页
        request.add("page", 1);
        mRequestQueue.add(ptrWhat, request, mOnResponseListener);
    }

    //上拉加载
    public void getHttpMethod() {
        mRequestQueue = NoHttp.newRequestQueue();//默认是3个请求
        String url = "http://38eye.test.ilexnet.com/api/mobile/special-product/list/";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("district_id", districtID);
        request.add("type", "week");
        //一次请求8
        request.add("limit", 8);
        //请求第几页
        request.add("page", count);
        count++;
        mRequestQueue.add(mWhat, request, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == ptrWhat) {
                try {
                    String result = response.get();
                    mList = new ArrayList<>();
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        JSONObject jsonObject2 = jsonObject.getJSONObject("product");
                        int product_id = jsonObject2.getInt("product_id");
                        String image = jsonObject2.getString("image");
                        String name = jsonObject2.getString("name");
                        float price = (float) jsonObject2.getDouble("price");
                        String extension4 = jsonObject2.getString("extension4"); // 单位
                        int stock_num = jsonObject2.getInt("stock_num");
                        float market_price = (float) jsonObject2.getDouble("market_price");
                        String description = jsonObject2.getString("description");
                        Goods goods = new Goods(product_id, name, image, extension4, market_price, price, 0, stock_num, description);
                        //获取日期
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DATE, 1);
                        String date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE);
                        goods.setExtension(date);
                        mList.add(goods);
                    }
                    isLoad = true;
                    mEventRecycleViewAdapter = new EventRecycleViewAdapter(mList, getActivity());
                    mRecyclerView.setAdapter(mEventRecycleViewAdapter);
                    mEventRecycleViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (what == mWhat) {
                try {
                    String result = response.get();
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    if (array.length() != 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            JSONObject jsonObject2 = jsonObject.getJSONObject("product");
                            int product_id = jsonObject2.getInt("product_id");
                            String image = jsonObject2.getString("image");
                            String name = jsonObject2.getString("name");
                            float price = (float) jsonObject2.getDouble("price");
                            String extension4 = jsonObject2.getString("extension4"); // 单位
                            int stock_num = jsonObject2.getInt("stock_num");
                            float market_price = (float) jsonObject.getDouble("market_price");
                            String description = jsonObject2.getString("description");
                            Goods goods = new Goods(product_id, name, image, extension4, market_price, price, 0, stock_num, description);
                            //获取日期
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.DATE, 0);
                            String date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE);
                            goods.setExtension(date);
                            mList.add(goods);
                        }
                        mEventRecycleViewAdapter.notifyDataSetChanged();
                    } else {
                        isLoad = false;
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
}