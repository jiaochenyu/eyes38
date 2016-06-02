package com.example.eyes38.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eyes38.R;
import com.example.eyes38.adapter.Sort_TitleAdapter;
import com.example.eyes38.beans.SortTitle;
import com.example.eyes38.fragment.sort.ContentFragment;
import com.example.eyes38.utils.DividerItemDecoration;
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
 * Created by jcy on 2016/5/8.
 */
public class SortFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    //适配器
    private Sort_TitleAdapter titleAdapter;
    //处理事务
    private List<SortTitle> mList;
    private LinearLayoutManager liner;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private ContentFragment mContentFragment;

    //测试获取json数据
    //创建 请求队列成员变量
    private RequestQueue mRequestQueue;
    private final static int mWhat = 520;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sort, null);
        //初始化数据
        initData();

        //初始化布局
        initView();


        //初始化碎片
        initFragment();
        return view;
    }

    private void initAdapter() {
        titleAdapter = new Sort_TitleAdapter(mList,getContext());
        mRecyclerView.setAdapter(titleAdapter);
    }

    private void initData() {
        getHttpMedthod();

        /*//以及标题
        mList = new ArrayList<>();
        SortTitle s1 = new SortTitle(0, "蔬菜豆菇", true);
        SortTitle s2 = new SortTitle(1, "新鲜蔬果", false);
        SortTitle s3 = new SortTitle(2, "鲜肉蛋类", false);
        SortTitle s4 = new SortTitle(3, "水产海鲜", false);
        SortTitle s5 = new SortTitle(4, "速冻冻品", false);
        SortTitle s6 = new SortTitle(5, "牛奶面点", false);
        SortTitle s7 = new SortTitle(6, "粮油副食", false);
        SortTitle s8 = new SortTitle(7, "零食酒水", false);
        SortTitle s9 = new SortTitle(8, "进口食品", false);
        SortTitle s10 = new SortTitle(9, "厨房用品", false);
        mList.add(s1);
        mList.add(s2);
        mList.add(s3);
        mList.add(s4);
        mList.add(s5);
        mList.add(s6);
        mList.add(s7);
        mList.add(s8);
        mList.add(s9);
        mList.add(s10);*/
    }

    private void getHttpMedthod() {
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/category/list";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.setRequestFailedReadCache(true);
        request.add("active",1);
        request.add("parent_id",0);
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
                        boolean ischecked = false;
                        ischecked = (i == 0 ?true:false);
                        int id = jsonObject.getInt("category_id");
                        String name = jsonObject.getString("name");
                        String path = jsonObject.getString("image");
                        SortTitle s = new SortTitle(i,id,name, ischecked,path);
                        mList.add(s);
                    }
                    //初始化适配器
                    initAdapter();
                    //这是监听
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

    private void initView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.sort_title);
        liner = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(liner);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    private void initListener() {
        titleAdapter.setmOnItemClickListener(new Sort_TitleAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, SortTitle sortTitle) {
                mTransaction = mFragmentManager.beginTransaction();
                mContentFragment = new ContentFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id", sortTitle.getCategory_id());
                mContentFragment.setArguments(bundle);
                mTransaction.replace(R.id.right, mContentFragment);
                mTransaction.commit();
            }

        });
    }

    private void initFragment() {
        mFragmentManager = getChildFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mContentFragment = new ContentFragment();
        mTransaction.add(R.id.right, mContentFragment);
        mTransaction.commit();
    }
}
