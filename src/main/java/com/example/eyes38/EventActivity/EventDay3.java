package com.example.eyes38.EventActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eyes38.R;
import com.example.eyes38.activity.GoodDetailActivity;
import com.example.eyes38.adapter.EventRecycleViewAdapter;
import com.example.eyes38.beans.Goods;
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
import java.util.List;

/**
 * Created by weixiao on 2016/5/24.
 */
public class EventDay3 extends Fragment {
    private static final int mWhat = 1;
    View view;
    RecyclerView mRecyclerView;
    private RequestQueue mRequestQueue;
    private List<Goods> mList;
    private EventRecycleViewAdapter mEventRecycleViewAdapter;
    private int districtID;
    //适配器

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.event_day3, null);
        initViews();
        initData();
        getHttpMethod();
        return view;
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.event_day3_recyclerview);
        //设置cecycleview的布局管理
        //StaggeredGridLayoutManager 实现瀑布流
       /* StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        EventSpacesItemDecoration decoration = new EventSpacesItemDecoration(16); // 设置item 的间距
        mRecyclerView.addItemDecoration(decoration);*/
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    //初始化数据
    private void initData() {
        mList = new ArrayList<>();
        //获取district id
        Intent intent = getActivity().getIntent();
        districtID = intent.getIntExtra("values", -1);
    }

    private void initListener(){
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

    //请求http获取图片
    public void getHttpMethod() {
        mRequestQueue = NoHttp.newRequestQueue();//默认是3个请求
        String url = "http://38eye.test.ilexnet.com/api/mobile/special-product/list/";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        //request.setRequestFailedReadCache(true);
        request.add("district_id", districtID);
        request.add("type", "week");
        mRequestQueue.add(mWhat, request, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWhat) {
                String result = response.get();
                mList = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        //初始化mmlist
                        JSONObject jsonObject2 = jsonObject.getJSONObject("product");
                        int product_id = jsonObject2.getInt("product_id");
                        String image = jsonObject2.getString("image");
                        String name = jsonObject2.getString("name");
                        float price = (float) jsonObject2.getDouble("price");
                        String extension4 = jsonObject2.getString("extension4"); // 单位
                        int stock_num = jsonObject2.getInt("stock_num");
                        String description = jsonObject2.getString("description");
                        Goods goods = new Goods(product_id, name, image, extension4, 0, price, 0, stock_num, description);
                        goods.setExtension("true");
                        mList.add(goods);
                    }
                    mEventRecycleViewAdapter = new EventRecycleViewAdapter(mList, getActivity());
                    mRecyclerView.setAdapter(mEventRecycleViewAdapter);
                    initListener();
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
