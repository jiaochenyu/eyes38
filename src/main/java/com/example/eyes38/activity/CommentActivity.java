package com.example.eyes38.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;

import com.example.eyes38.R;
import com.example.eyes38.adapter.Comment_Adapter;
import com.example.eyes38.beans.CommentReply;
import com.example.eyes38.beans.Comments;
import com.example.eyes38.utils.DividerItemDecoration;
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

public class CommentActivity extends AppCompatActivity {
    public static final int ALL = 1;
    public static final int GREAT = 2;
    public static final int MIDDLE = 3;
    public static final int BAD = 4;
    public static final int PICTURE = 5;
    private RecyclerView mRecyclerView;
    private List<Comments> mList;
    private LinearLayoutManager linearLayoutManager;
    private RequestQueue mRequestQueue;
    private PtrClassicFrameLayout ptrFrame;
    //评论导航栏
    private RadioGroup mRadioGroup;
    //记录传来的商品id
    private int product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        //刷新区域
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
        //导航栏
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                resetCommentData(checkedId);
            }
        });
    }

    private void resetCommentData(int checkId) {
        //重置评论内容
        switch (checkId) {
            case R.id.comment_all:
                getHttpRequest(ALL);
                break;
            case R.id.comment_great:
                getHttpRequest(GREAT);
                break;
            case R.id.comment_middle:
                getHttpRequest(MIDDLE);
                break;
            case R.id.comment_bad:
                getHttpRequest(BAD);
                break;
            case R.id.comment_picture:
                getHttpRequest(PICTURE);
                break;
        }
    }


    private void initAdapter() {
        Comment_Adapter comment_adapter = new Comment_Adapter(mList, this);
        mRecyclerView.setAdapter(comment_adapter);
    }

    private void initData() {
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
        getProductId();
        getHttpRequest(ALL);
    }

    private void getProductId() {
        //获取传来的商品id
        Intent intent = getIntent();
        product_id = intent.getIntExtra("product_id",1);
    }

    private void getHttpRequest(int what) {
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/discussion-api/discussions";
        Request<String> mRequest = NoHttp.createStringRequest(url, RequestMethod.GET);
        mRequest.add("item_id",product_id);
        //设置缓存
        mRequest.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        mRequestQueue.add(what, mRequest, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == ALL) {
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    mList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        int id = jsonObject.getInt("comment_id");
                        String name = jsonObject.getString("author_name");
                        String time = jsonObject.getString("create_date");
                        String content = jsonObject.getString("comment");
                        int ratingbar = jsonObject.getInt("rating");
                        List<CommentReply> list = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("replies");
                        if (jsonArray.length() != 0) {
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject reply = jsonArray.getJSONObject(j);
                                String reply_name = reply.getString("author_name");
                                String reply_content = reply.getString("comment");
                                String reply_time = reply.getString("create_date");
                                CommentReply commentReply = new CommentReply(j+1, reply_name, null, reply_content, reply_time);
                                list.add(commentReply);
                            }
                        }
                        Comments comments = new Comments(id, null, name, ratingbar, content, time, list);
                        mList.add(comments);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initAdapter();
            }
            if (what == GREAT) {
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    mList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        int ratingbar = jsonObject.getInt("rating");
                        if (ratingbar >= 5) {
                            int id = jsonObject.getInt("comment_id");
                            String name = jsonObject.getString("author_name");
                            String time = jsonObject.getString("create_date");
                            String content = jsonObject.getString("comment");
                            List<CommentReply> list = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("replies");
                            if (jsonArray.length() != 0) {
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject reply = jsonArray.getJSONObject(j);
                                    String reply_name = reply.getString("author_name");
                                    String reply_content = reply.getString("comment");
                                    String reply_time = reply.getString("create_date");
                                    CommentReply commentReply = new CommentReply(j+1, reply_name, null, reply_content, reply_time);
                                    list.add(commentReply);
                                }
                            }
                            Comments comments = new Comments(id, null, name, ratingbar, content, time,list);
                            mList.add(comments);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initAdapter();
            }
            if (what == MIDDLE) {
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    mList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        int ratingbar = jsonObject.getInt("rating");
                        if (ratingbar >= 3 && ratingbar < 5) {
                            int id = jsonObject.getInt("comment_id");
                            String name = jsonObject.getString("author_name");
                            String time = jsonObject.getString("create_date");
                            String content = jsonObject.getString("comment");
                            List<CommentReply> list = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("replies");
                            if (jsonArray.length() != 0) {
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject reply = jsonArray.getJSONObject(j);
                                    String reply_name = reply.getString("author_name");
                                    String reply_content = reply.getString("comment");
                                    String reply_time = reply.getString("create_date");
                                    CommentReply commentReply = new CommentReply(j+1, reply_name, null, reply_content, reply_time);
                                    list.add(commentReply);
                                }
                            }
                            Comments comments = new Comments(id, null, name, ratingbar, content, time,list);
                            mList.add(comments);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initAdapter();
            }
            if (what == BAD) {
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    mList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        int ratingbar = jsonObject.getInt("rating");
                        if (ratingbar <= 1) {
                            int id = jsonObject.getInt("comment_id");
                            String name = jsonObject.getString("author_name");
                            String time = jsonObject.getString("create_date");
                            String content = jsonObject.getString("comment");
                            List<CommentReply> list = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("replies");
                            if (jsonArray.length() != 0) {
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject reply = jsonArray.getJSONObject(j);
                                    String reply_name = reply.getString("author_name");
                                    String reply_content = reply.getString("comment");
                                    String reply_time = reply.getString("create_date");
                                    CommentReply commentReply = new CommentReply(j+1, reply_name, null, reply_content, reply_time);
                                    list.add(commentReply);
                                }
                            }
                            Comments comments = new Comments(id, null, name, ratingbar, content, time, list);
                            mList.add(comments);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initAdapter();
            }
            if (what == PICTURE) {
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    mList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        int id = jsonObject.getInt("comment_id");
                        String name = jsonObject.getString("author_name");
                        String time = jsonObject.getString("create_date");
                        String content = jsonObject.getString("comment");
                        int ratingbar = jsonObject.getInt("rating");
                        List<CommentReply> list = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("replies");
                        if (jsonArray.length() != 0) {
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject reply = jsonArray.getJSONObject(j);
                                String reply_name = reply.getString("author_name");
                                String reply_content = reply.getString("comment");
                                String reply_time = reply.getString("create_date");
                                CommentReply commentReply = new CommentReply(j+1, reply_name, null, reply_content, reply_time);
                                list.add(commentReply);
                            }
                        }
                        Comments comments = new Comments(id, null, name, ratingbar, content, time,list);
                        mList.add(comments);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initAdapter();
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
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        ptrFrame = (PtrClassicFrameLayout) findViewById(R.id.goods_comment_ptr);
        mRadioGroup = (RadioGroup) findViewById(R.id.goods_comment_rg);
    }

    public void back(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity 销毁，释放资源
        mRequestQueue.stop();
        linearLayoutManager.removeAllViews();

    }
}
