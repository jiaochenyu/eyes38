package com.example.eyes38.utils;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

/**
 * Created by jqchen on 2016/5/24.
 */
public class GetJson {
    private static OnResponseListener<String> mOnResponseListener;
    private  static RequestQueue mRequestQueue;
    private final static int mWhat = 520;
    private static String result = null;
    public static String getJson(String url){

        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        mOnResponseListener = new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                result = response.get();
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

            }

            @Override
            public void onFinish(int what) {

            }
        };
        mRequestQueue.add(mWhat,request,mOnResponseListener);
        return result;
    }
}
