package com.example.eyes38.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.eyes38.Application.Application;
import com.example.eyes38.R;
import com.example.eyes38.adapter.Comment_Adapter;
import com.example.eyes38.beans.Comments;
import com.example.eyes38.beans.Goods;
import com.example.eyes38.utils.CommentAddDialog;
import com.example.eyes38.utils.LoadMoreFooterView;
import com.example.eyes38.utils.SpaceItemDecoration;
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
import java.util.Collections;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class CommentActivity extends AppCompatActivity {
    public static final int ALL = 100;
    public static final int GREAT = 200;
    public static final int MIDDLE = 300;
    public static final int BAD = 400;
    public static final int COMMITCOMMENT = 500;
    private RecyclerView mRecyclerView;
    private List<Comments> mList;
    private LinearLayoutManager linearLayoutManager;
    private RequestQueue mRequestQueue;
    private PtrClassicFrameLayout ptrFrame;
    //评论导航栏
    private RadioGroup mRadioGroup;
    //记录传来的商品id
    private int product_id;
    //评论有无，显示不同区域
    private RelativeLayout noneComment;
    private ImageView commentAdd;
    private SharedPreferences sharedPreferences;
    private int record = ALL;
    private Toast mToast;
    private Goods goods;
    private Comment_Adapter comment_adapter;
    private String Authorization;

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
                        getHttpRequest(record);
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
        //天机评论
        commentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示评论框
                final CommentAddDialog.Builder builder = new CommentAddDialog.Builder(CommentActivity.this);
                builder.setCancelOnClickListener("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCommitOnClickListener("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (Application.isLogin){
                            String comment  = builder.getmEditText();
                            if (comment.equals("")){
                                show("评论不能为空!");
                            }else {
                                int rating = builder.getmRatingBar();
                                commitComment(comment,rating);
                            }
                        }else {
                            show("当前未登录，请先登录");
                        }
                    }
                });
                builder.create().show();

            }
        });
    }

    private void commitComment(String comment1, int ratingBar) {
        //获取用户的id,账号密码加密
        String username = sharedPreferences.getString("USER_NAME", "");
        String password = sharedPreferences.getString("PASSWORD", "");
        String header = username + "" + password;
        String newHeader = new String(Base64.encode(header.getBytes(), Base64.DEFAULT));//加密后的header
        Authorization = "Basic " + newHeader;
        int author_id = Integer.parseInt(sharedPreferences.getString("CUSTOMER_ID", ""));
        String author_type = "customer";
        String comment = comment1;
        String comment_type = "product";
        int item_id = product_id;
        int parent_id = 0;
        int rating = ratingBar;
        int store_id = goods.getStore();
        int active = 1;
        String url = "http://38eye.test.ilexnet.com/api/mobile/discussion-api/discussions";
        Request<String> mRequest = NoHttp.createStringRequest(url, RequestMethod.POST);
        //添加头
        mRequest.addHeader("Authorization", Authorization);
        mRequest.add("author_id", author_id);
        mRequest.add("author_type", author_type);
        mRequest.add("comment", comment);
        mRequest.add("comment_type", comment_type);
        mRequest.add("item_id", item_id);
        mRequest.add("parent_id", parent_id);
        mRequest.add("rating", rating);
        mRequest.add("store_id", store_id);
        mRequest.add("active", active);

        //设置缓存
        mRequest.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        mRequestQueue.add(COMMITCOMMENT, mRequest, mOnResponseListener);
    }

    private void resetCommentData(int checkId) {
        //重置评论内容
        switch (checkId) {
            case R.id.comment_all:
                getHttpRequest(ALL);
                record = ALL;
                break;
            case R.id.comment_great:
                getHttpRequest(GREAT);
                record = GREAT;
                break;
            case R.id.comment_middle:
                getHttpRequest(MIDDLE);
                record = MIDDLE;
                break;
            case R.id.comment_bad:
                getHttpRequest(BAD);
                record = BAD;
                break;
        }
    }


    private void initAdapter() {
        if (mList.size() == 0){
            //没有评论
            noneComment.setVisibility(View.VISIBLE);
            ptrFrame.setVisibility(View.GONE);
        }else {
            //有评论
            Collections.reverse(mList);
            noneComment.setVisibility(View.GONE);
            ptrFrame.setVisibility(View.VISIBLE);
            comment_adapter = new Comment_Adapter(mList, this);
            mRecyclerView.setAdapter(comment_adapter);
            getHeaderImage();
        }

    }

    private void getHeaderImage() {
        for (int i = 0; i < mList.size(); i++) {
            Comments c = mList.get(i);
            String url = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customers/"+c.getAuthor_id();
            Request<String> mRequest = NoHttp.createStringRequest(url, RequestMethod.GET);
            mRequest.addHeader("Authorization", Authorization);
            mRequestQueue.add(c.getComment_id(), mRequest, mOnResponseListener);
        }
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
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        getProductId();
        getHttpRequest(ALL);
    }

    private void getProductId() {
        //获取传来的商品id
        Intent intent = getIntent();
        goods = (Goods) intent.getSerializableExtra("goods");
        product_id = goods.getGoods_id();
    }

    private void getHttpRequest(int what) {
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/discussion-api/discussions";
        Request<String> mRequest = NoHttp.createStringRequest(url, RequestMethod.GET);
        //添加属性，筛选评论
        mRequest.add("item_id", product_id);
        mRequest.add("parent_id",0);
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
                        int comment_id = jsonObject.getInt("comment_id");
                        int item_id = jsonObject.getInt("item_id");
                        String path = "";
                        String author_name = jsonObject.getString("author_name");
                        int rating = jsonObject.getInt("rating");
                        String comment = jsonObject.getString("comment");
                        String create_date = jsonObject.getString("create_date");
                        int store_id = jsonObject.getInt("store_id");
                        int author_id = jsonObject.getInt("author_id");
                        Comments comments = new Comments(comment_id,item_id,path,author_name,rating,comment,create_date,store_id,author_id);
                        mList.add(comments);
                    }
                    initAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (what == GREAT) {
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    mList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        int rating = jsonObject.getInt("rating");
                        if (rating >= 5) {
                            int comment_id = jsonObject.getInt("comment_id");
                            int item_id = jsonObject.getInt("item_id");
                            String path = "";
                            String author_name = jsonObject.getString("author_name");
                            String comment = jsonObject.getString("comment");
                            String create_date = jsonObject.getString("create_date");
                            int store_id = jsonObject.getInt("store_id");
                            int author_id = jsonObject.getInt("author_id");
                            Comments comments = new Comments(comment_id,item_id,path,author_name,rating,comment,create_date,store_id,author_id);
                            mList.add(comments);
                        }
                    }
                    initAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (what == MIDDLE) {
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    mList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        int rating = jsonObject.getInt("rating");
                        if (rating >= 3 && rating < 5) {
                            int comment_id = jsonObject.getInt("comment_id");
                            int item_id = jsonObject.getInt("item_id");
                            String path = "";
                            String author_name = jsonObject.getString("author_name");
                            String comment = jsonObject.getString("comment");
                            String create_date = jsonObject.getString("create_date");
                            int store_id = jsonObject.getInt("store_id");
                            int author_id = jsonObject.getInt("author_id");
                            Comments comments = new Comments(comment_id,item_id,path,author_name,rating,comment,create_date,store_id,author_id);
                            mList.add(comments);
                        }
                    }
                    initAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (what == BAD) {
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    mList = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        int rating = jsonObject.getInt("rating");
                        if (rating <= 2) {
                            int comment_id = jsonObject.getInt("comment_id");
                            int item_id = jsonObject.getInt("item_id");
                            String path = "";
                            String author_name = jsonObject.getString("author_name");
                            String comment = jsonObject.getString("comment");
                            String create_date = jsonObject.getString("create_date");
                            int store_id = jsonObject.getInt("store_id");
                            int author_id = jsonObject.getInt("author_id");
                            Comments comments = new Comments(comment_id,item_id,path,author_name,rating,comment,create_date,store_id,author_id);
                            mList.add(comments);
                        }
                    }
                    initAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (what == COMMITCOMMENT){
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    boolean success = object.getBoolean("success");
                    if (success){
                        show("评论成功");
                        getHttpRequest(record);
                    }else {
                        show("发表失败！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < mList.size(); i++) {
                if (what == mList.get(i).getComment_id()){
                    String result = response.get();
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONObject object1 = object.getJSONObject("data");
                        String path = object1.getString("image");
                        if (!path.equals("null")){
                            mList.get(i).setPath(path);
                            comment_adapter.notifyItemChanged(i);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    private void show(String text) {
        //显示信息
        if (mToast == null) {
            mToast = Toast.makeText(CommentActivity.this, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.goods_comment_recycler);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //添加分割线
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recyclerview_space);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        ptrFrame = (PtrClassicFrameLayout) findViewById(R.id.goods_comment_ptr);
        mRadioGroup = (RadioGroup) findViewById(R.id.goods_comment_rg);
        noneComment = (RelativeLayout) findViewById(R.id.comment_none);
        commentAdd = (ImageView) findViewById(R.id.comment_add);
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
