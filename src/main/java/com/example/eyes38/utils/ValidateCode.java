package com.example.eyes38.utils;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jqchen on 2016/6/3.
 */
public class ValidateCode {
    private static boolean isValidate;

    public static final int VALIDATE = 1;

    public static boolean getValideCode(String telNum){
        //获取验证码
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer/validatecode";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);;
        request.add("mobile",telNum);
        mRequestQueue.add(VALIDATE,request,mOnResponseListener);
        return isValidate;
    }
    private static OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == VALIDATE){
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    isValidate = object.getBoolean("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                isValidate = false;
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
