package com.example.eyes38.EventActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eyes38.R;
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
public class EventDay2 extends Fragment{
    public static final int mWhat = 522;
    public static final int mFinish = 523;
    EventActivity mEventActivity;
    View view;
    RecyclerView mRecyclerView;
    //适配器
    EventRecycleViewAdapter mEventRecycleViewAdapter=null;
    List<Goods> mList;
    //采用 NoHttp
    //创建 请求队列成员变量
    private RequestQueue mRequestQueue;
    //Handler
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case mFinish:
                    //加载数据完成
                    initListener();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void initListener() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mEventActivity= (EventActivity) getActivity();
        view=inflater.inflate(R.layout.event_day2,null);
        initViews();
        initData();
        getHttpMethod();
        return view;
    }



    private void initViews() {
        mRecyclerView= (RecyclerView) view.findViewById(R.id.event_day2_recycle);
        //设置cecycleview的布局管理
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
        mRecyclerView.setLayoutManager(gridLayoutManager);

    }
    //初始化数据
    private void initData() {
        mList=new ArrayList<>();
    }
    //请求http获取图片
    public void getHttpMethod() {
        mRequestQueue=NoHttp.newRequestQueue();//默认是3个请求
        String url = "http://38eye.test.ilexnet.com/api/mobile/product-api/products";
        Request<String> request=NoHttp.createStringRequest(url,RequestMethod.GET);
        //request.setRequestFailedReadCache(true);
        mRequestQueue.add(mWhat,request,mOnResponseListener);
    }
    /**
     * 请求http结果 回调对象 接受请求结果
     */
    private OnResponseListener<String> mOnResponseListener=new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what==mWhat){
                //请求成功
                String result=response.get();
                Log.e("CC","开始解析");
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    JSONArray data=jsonObject.getJSONArray("data");
                    for (int i=0;i<data.length();i++){
                        JSONObject object=data.getJSONObject(i);
                        Goods good=new Goods();
                        String image=object.getString("image");
                        String title=object.getString("name");
                        good.setPath(image);
                        good.setGoods_name(title);
                        mList.add(good);
                    }
                    mEventRecycleViewAdapter=new EventRecycleViewAdapter(mList,getActivity());
                    mRecyclerView.setAdapter(mEventRecycleViewAdapter);
                    Message message=new Message();
                    message.what= mFinish;
                    Log.e("请求完成","11");
                    mHandler.sendMessage(message);

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
            //请求结束。通知初始化 适配器
        }
    };

}