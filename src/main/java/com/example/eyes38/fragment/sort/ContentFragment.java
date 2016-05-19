package com.example.eyes38.fragment.sort;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eyes38.R;
import com.example.eyes38.adapter.Sort_ContentAdapter;
import com.example.eyes38.beans.SortContent;
import com.example.eyes38.beans.SortContentContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jqchen on 2016/4/28.
 */
public class ContentFragment extends Fragment {
    RecyclerView mRecyclerView;
    View mView;
    List<SortContent> mList;
    List<SortContentContent> mmList;
    //下拉刷新控件
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.sort_content, null);
        //初始化布局
        initView();
        //初始化数据
        initData();
        //初始化适配器
        initAdapter();
        //设置下拉刷新
        initRefresh();
        return mView;
    }

    private void initRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(),"下拉刷新",Toast.LENGTH_SHORT).show();
                    }
                },3000);

            }
        });
    }

    private void initAdapter() {
        Sort_ContentAdapter scAdapter = new Sort_ContentAdapter(getContext(), mList);
        mRecyclerView.setAdapter(scAdapter);
    }

    private void initData() {

        mmList = new ArrayList<>();
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
        }
    }

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.sort_swiprefresh);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.texton));
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.sort_content_recycler);
        LinearLayoutManager linear = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linear);
    }

    //获取Recyclerview传来的的值：id
    public int getID() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            int id = bundle.getInt("id");
            return id;
        }
        return 0;
    }
}
