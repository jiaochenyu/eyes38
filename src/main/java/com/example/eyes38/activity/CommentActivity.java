package com.example.eyes38.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.eyes38.R;
import com.example.eyes38.adapter.Comment_Adapter;
import com.example.eyes38.beans.Comments;
import com.example.eyes38.utils.DividerItemDecoration;
import com.example.eyes38.utils.LoadMoreFooterView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
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

public class CommentActivity extends AppCompatActivity {
    public static final int WHAT = 1;
    public static final int FINISHED = 1;
    private RecyclerView mRecyclerView;
    private List<Comments> mList;
    private Comment_Adapter comment_adapter;
    private LinearLayoutManager linearLayoutManager;
    private RequestQueue mRequestQueue;
    private PtrClassicFrameLayout ptrFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();
        initData();
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
                },1800);

            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FINISHED:
                    initAdapter();
                    break;
            }
        }
    };


    private void initAdapter() {
        comment_adapter = new Comment_Adapter(mList);
        mRecyclerView.setAdapter(comment_adapter);
    }

    private void initData() {
        mList = new ArrayList<>();
        /*for (int i = 0; i < 20; i++) {
            Comments c1 = new Comments(i, "aaaaa", i+"");
            mList.add(c1);
        }*/
        /*Comments c1 = new Comments(1, "aaaaa", "111");
        Comments c2 = new Comments(2, "bbbbb", "222");
        Comments c3 = new Comments(3, "ccccc", "333");
        Comments c4 = new Comments(4, "ddddd", "444");
        mList.add(c1);
        mList.add(c2);
        mList.add(c3);
        mList.add(c4);*/
        getHttpRequest();
    }

    private void getHttpRequest() {
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/discussion-api/discussions";
        Request<String> mRequest = NoHttp.createStringRequest(url);
        mRequestQueue.add(WHAT, mRequest, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == WHAT) {
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        int id = jsonObject.getInt("comment_id");
                        String name = jsonObject.getString("author_name");
                        String time = jsonObject.getString("create_date");
                        String content = jsonObject.getString("comment");
                        int ratingbar = jsonObject.getInt("rating");
                        Comments comments = new Comments(id, null, name, ratingbar, content, time);
                        mList.add(comments);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(FINISHED);
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
        mRecyclerView = (RecyclerView) findViewById(R.id.goods_comment_recycler);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        ptrFrame = (PtrClassicFrameLayout) findViewById(R.id.goods_comment_ptr);
    }

    public void back(View view) {
        finish();
    }


}
