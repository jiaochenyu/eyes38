package com.example.eyes38.fragment.sort;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eyes38.R;
import com.example.eyes38.adapter.Sort_ContentAdapter;
import com.example.eyes38.beans.SortContent;
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

/**
 * Created by jqchen on 2016/4/28.
 */
public class ContentFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private View mView;
    private List<SortContent> mList;
    //下拉刷新控件

    private final static int mWhat = 520;
    private PtrClassicFrameLayout ptrFrame;
    private RequestQueue mRequestQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.sort_content, null);
        //初始化布局
        initView();
        //初始化数据
        initData();
        initListener();

        return mView;
    }

    private void initListener() {
        //自定义下拉刷新动画
        LoadMoreFooterView header = new LoadMoreFooterView(mView.getContext());
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);
        //上拉刷新
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
                        getHttpMedthod();
                        ptrFrame.refreshComplete();
                    }
                },1800);

            }
        });

    }


    private void initAdapter() {
        Sort_ContentAdapter scAdapter = new Sort_ContentAdapter(getContext(), mList);
        mRecyclerView.setAdapter(scAdapter);
    }

    private void initData() {
        mRequestQueue = NoHttp.newRequestQueue();
        getHttpMedthod();

        /*mmList = new ArrayList<>();
        mList = new ArrayList<>();
        SortContentContent scc1 = new SortContentContent(1, null, "test1");
        SortContentContent scc2 = new SortContentContent(2, null, "test2");
        SortContentContent scc3 = new SortContentContent(3, null, "test3");
        SortContentContent scc4 = new SortContentContent(4, null, "test4");
        SortContentContent scc5 = new SortContentContent(5, null, "test5");
        SortContentContent scc6 = new SortContentContent(6, null, "test6");
        SortContentContent scc7 = new SortContentContent(7, null, "test7");
        mmList.add(scc1);
        mmList.add(scc2);
        mmList.add(scc3);
        mmList.add(scc4);
        mmList.add(scc5);
        mmList.add(scc6);
        mmList.add(scc7);
        SortContent sc1 = new SortContent(1, "二级标题1", mmList);
        SortContent sc2 = new SortContent(2, "二级标题2", mmList);
        SortContent sc3 = new SortContent(3, "二级标题3", mmList);
        SortContent sc4 = new SortContent(4, "二级标题4", mmList);
        SortContent sc5 = new SortContent(5, "二级标题5", mmList);
        SortContent sc6 = new SortContent(6, "二级标题6", mmList);
        int id = getID();
        switch (id) {
            case 0:
                mList.add(sc1);
                break;
            case 1:
                mList.add(sc2);
                break;
            case 2:
                mList.add(sc3);
                break;
            case 3:
                mList.add(sc4);
                break;
            case 4:
                mList.add(sc5);
                break;
            case 5:
                mList.add(sc6);
                break;
        }*/
    }

    private void getHttpMedthod() {
        String url = "http://38eye.test.ilexnet.com/api/mobile/category/list";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        //request.setRequestFailedReadCache(true);
        request.add("active",1);
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
                        int parent_id = jsonObject.getInt("parent_id");
                        if (parent_id == getID()){
                            int category_id = jsonObject.getInt("category_id");
                            String name = jsonObject.getString("name");
                            //初始化mmlist
                            List<SortContentContent> mmList = new ArrayList<>();
                            for (int j = 0; j < array.length(); j++){
                                JSONObject jsonObject1 = array.getJSONObject(j);
                                int parent_id1 = jsonObject1.getInt("parent_id");
                                if (parent_id1 == category_id){
                                    int category_id1 = jsonObject1.getInt("category_id");
                                    String name1 = jsonObject1.getString("name");
                                    String path = jsonObject1.getString("image");
                                    SortContentContent scc = new SortContentContent(category_id1,name1,path);
                                    mmList.add(scc);
                                }
                            }
                            if (mmList.size() == 0){
                                SortContentContent scc = new SortContentContent(1,"暂无商品","");
                                mmList.add(scc);
                            }
                            SortContent sc = new SortContent(parent_id,name,mmList);
                            mList.add(sc);
                        }
                    }
                    //初始化适配器
                    initAdapter();

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

    private void initView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.sort_content_recycler);
        LinearLayoutManager linear = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linear);
        ptrFrame = (PtrClassicFrameLayout) mView.findViewById(R.id.sort_content_ptr);
    }

    //获取Recyclerview传来的的值：id
    public int getID() {
        Bundle bundle = getArguments();
        int id = 1;
        if (bundle != null) {
            id = bundle.getInt("id");
            return id;
        }
        return id;
    }
}
