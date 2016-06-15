package com.example.eyes38.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eyes38.Application.Application;
import com.example.eyes38.R;
import com.example.eyes38.adapter.CommentReplyAdapter;
import com.example.eyes38.beans.CommentReply;
import com.example.eyes38.beans.Comments;
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

public class CommentReplyActivity extends AppCompatActivity {
    public static final int GETCOMMENT = 100;
    public static final int SENDCOMMENT = 200;
    //    评论回复界面
    private List<CommentReply> mList;
    private RecyclerView mRecyclerView;
    //写回复界面
    private EditText commentreplyEditText;
    private Button sendButton;
    private Comments mComments;

    //存储回复内容
    private String commentreply;
    private RequestQueue mRequestQueue = NoHttp.newRequestQueue();
    private Toast mToast;
    private String Authorization;
    private CommentReplyAdapter commentReplyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_reply);
        initView();
        getData();
        getCommentReply();
        initListener();
    }

    private void getCommentReply() {
        //获取回复
        String url = "http://38eye.test.ilexnet.com/api/mobile/discussion-api/discussions";
        Request<String> mRequest = NoHttp.createStringRequest(url, RequestMethod.GET);
        int item_id = mComments.getItem_id();
        int parent_id = mComments.getComment_id();
        mRequest.add("item_id", item_id);
        mRequest.add("parent_id", parent_id);
        //设置缓存
        mRequest.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        mRequestQueue.add(GETCOMMENT, mRequest, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == GETCOMMENT) {
                String result = response.get();
                try {
                    mList = new ArrayList<>();
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        int id = i + 1;
                        int comment_id = jsonObject.getInt("comment_id");
                        String author_name = jsonObject.getString("author_name");
                        String path = "";
                        String comment = jsonObject.getString("comment");
                        String create_date = jsonObject.getString("create_date");
                        int author_id = jsonObject.getInt("author_id");
                        CommentReply commentReply = new CommentReply(id, comment_id, author_name, path, comment, create_date,author_id);
                        mList.add(commentReply);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setAdapter();
            }
            if (what == SENDCOMMENT) {
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    boolean success = object.getBoolean("success");
                    if (success) {
                        //回复成功
                        getCommentReply();
                        resetInputComment();
                    }
                } catch (JSONException e) {
                    //回复失败
                    resetInputComment();
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
                            commentReplyAdapter.notifyItemChanged(i);
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

    private void initListener() {
        //对发送回复监听
        sendButton.setOnClickListener(new View.OnClickListener() {
            SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);

            @Override
            public void onClick(View v) {
                commentreply = commentreplyEditText.getText().toString();
                if (Application.isLogin) {
                    //当前为登录状态
                    if (commentreply.equals("")) {
                        show("内容不能为空");
                        resetInputComment();
                    } else {
                        sendCommentReply();
                    }
                } else {
                    //当前为未登录状态，提示用户登录
                    show("当前未登录，请先登录！");
                    resetInputComment();
                }

            }

            private void sendCommentReply() {
                //获取用户的id,账号密码加密
                String username = preferences.getString("USER_NAME", "");
                String password = preferences.getString("PASSWORD", "");
                String header = username + "" + password;
                String newHeader = new String(Base64.encode(header.getBytes(), Base64.DEFAULT));//加密后的header
                Authorization = "Basic " + newHeader;
                int author_id = Integer.parseInt(preferences.getString("CUSTOMER_ID", ""));
                String author_type = "customer";
                String comment = commentreply;
                String comment_type = "product";
                int item_id = mComments.getItem_id();
                int parent_id = mComments.getComment_id();
                int rating = 0;
                int store_id = mComments.getStore_id();
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
                mRequestQueue.add(SENDCOMMENT, mRequest, mOnResponseListener);
            }
        });
    }

    private void resetInputComment() {
        //发送回复失败，提示用户，重置回复输入框，
        commentreplyEditText.setText("");
        commentreplyEditText.clearFocus();
        //收起输入法
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void setAdapter() {
        //如果没有评论，显示评论为空的界面
        Collections.reverse(mList);
        commentReplyAdapter = new CommentReplyAdapter(mList,this);
        mRecyclerView.setAdapter(commentReplyAdapter);
        getHeaderImage();
    }

    private void getHeaderImage() {
        for (int i = 0; i < mList.size(); i++) {
            CommentReply cr = mList.get(i);
            String url = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customers/"+cr.getAuthor_id();
            Request<String> mRequest = NoHttp.createStringRequest(url, RequestMethod.GET);
            mRequest.addHeader("Authorization", Authorization);
            mRequestQueue.add(cr.getComment_id(), mRequest, mOnResponseListener);
        }
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.comment_reply_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        //添加分割线
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recyclerview_space);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        //初始化写回复界面的控件
        commentreplyEditText = (EditText) findViewById(R.id.comment_reply_content_edittext);
        sendButton = (Button) findViewById(R.id.comment_reply_send);
    }

    private void getData() {
        mList = new ArrayList<>();
        Intent intent = getIntent();
        mComments = (Comments) intent.getSerializableExtra("values");
    }

    private void show(String text) {
        //显示信息
        if (mToast == null) {
            mToast = Toast.makeText(CommentReplyActivity.this, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public void back(View view) {
        //返回上一层
        finish();
    }


}
