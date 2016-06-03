package com.example.eyes38.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.activity.home.HomexptjActivity;
import com.example.eyes38.activity.home.HomezhuantiActivity;
import com.example.eyes38.adapter.Home_ContentAdapter;
import com.example.eyes38.adapter.Home_ad_adapter;
import com.example.eyes38.beans.HomeContent;
import com.example.eyes38.beans.HomeContentContent;
import com.example.eyes38.beans.HomeFourSort;
import com.example.eyes38.fragment.home.HomeRecycleView;
import com.example.eyes38.fragment.home.HomeSpinnerView;
import com.example.eyes38.fragment.search.SearchActivity;
import com.example.eyes38.utils.LoadMoreFooterView;
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
import java.util.Timer;
import java.util.TimerTask;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * Created by jcy on 2016/5/8.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    MainActivity mMainActivity;
    View view;
    //初始化轮播图的viewpager
    ViewPager mViewPager;
    //初始化recycleView
    RecyclerView mRecyclerView;
    //封装的recycleView实现类
    HomeRecycleView mHomeRecycleView;
    //初始化spinner
    Spinner mSpinner;
    //封装的spinner的实现类
    HomeSpinnerView mHomeSpinnerView;
    ImageView home_fangdajing;
    ImageView home_xptjgengduo;
    ImageView home_yzcpgengduo;
    private int mWhat = 123;
    ArrayList<View> mViewList;
    List<String> mList;
    List<HomeContent> mrecycleList;
    List<HomeFourSort> mHomeFourSortsList;
    Home_ContentAdapter hcAdapter;
    public static final int LUNBOFINSH = 3;
    public static final int RECYCLEFINSH = 4;
    int mCurrentItem = Integer.MAX_VALUE / 2;
    public static final int IMAGE_UPDATE = 1;
    public static final int REFRESHTIME = 5 * 1000;
    public static final int IMAGE_CHANGED = 2;

    ImageView mImageView;
    int height;
    RelativeLayout mRelativeLayout;
    private RequestQueue mRequestQueue;

    //4个home_sort的图标和文字初始化
    private ImageView home_sort1image;
    private ImageView home_sort2image;
    private ImageView home_sort3image;
    private ImageView home_sort4image;
    private TextView home_sort1text;
    private TextView home_sort2text;
    private TextView home_sort3text;
    private TextView home_sort4text;
    private Timer timer;
    private PtrFrameLayout ptrFrame;
    private RecyclerView homerecycle;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, null);
        mMainActivity = (MainActivity) getActivity();
        initView();
        //获取轮播图的数据并实现,最新接口没数据,用的测试接口的数据
        getHttpMethod("http://api.dev.ilexnet.com/simulate/38eye/article-api/banner-images", mlunboOnResponseListener);
        //获取四大类的数据并实现
        getHttpMethod("http://38eye.test.ilexnet.com/api/mobile/home-category/list", mhomecategoryOnResponseListener);
        //获取recycleView的数据并实现
        getHttpMethod("http://38eye.test.ilexnet.com/api/mobile/special-product/listConfig", mOnRecycleResponseListener);
        //计算屏幕的尺寸并初始化spinner
        caculate();
        listener();
        return view;
    }

    private void listener() {
        LoadMoreFooterView header = new LoadMoreFooterView(view.getContext());
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
                        //获取轮播图的数据并实现,最新接口没数据,用的测试接口的数据
                        getHttpMethod("http://api.dev.ilexnet.com/simulate/38eye/article-api/banner-images", mlunboOnResponseListener);
                        //获取四大类的数据并实现
                        getHttpMethod("http://38eye.test.ilexnet.com/api/mobile/home-category/list", mhomecategoryOnResponseListener);
                        //获取recycleView的数据并实现
                        getHttpMethod("http://38eye.test.ilexnet.com/api/mobile/special-product/listConfig", mOnRecycleResponseListener);
                        //计算屏幕的尺寸并初始化spinner
                        caculate();
                    }
                },1800);

            }
        });
    }

    private void getHttpMethod(String url, OnResponseListener mOnResponseListener) {
        mrecycleList = new ArrayList<>();
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        //request.setRequestFailedReadCache(true);
        mRequestQueue.add(mWhat, request, mOnResponseListener);
    }

    //轮播图的OnResponseListener
    private OnResponseListener<String> mlunboOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWhat) {
                //请求成功
                String result = response.get();
                try {
                    //解析第一层
                    JSONObject object = new JSONObject(result);
                    JSONArray homelunbo = object.getJSONArray("data");
                    mList = new ArrayList<String>();
                    for (int i = 0; i < homelunbo.length(); i++) {
                        JSONObject jsonObject = homelunbo.getJSONObject(i);
                        String address = jsonObject.getString("image");
                        mList.add(address);
                        Log.e("获取的数据jjjj", address);
                    }
                    mViewList = new ArrayList<View>();
                    for (int i = 0; i < mList.size(); i++) {
                        View view = View.inflate(mMainActivity, R.layout.home_ad_item, null);
                        ImageView mItemIvContent = (ImageView) view.findViewById(R.id.item_iv_content);
                        Glide.with(mMainActivity).load(mList.get(i)).into(mItemIvContent);
                        mViewList.add(view);
                    }
                    Home_ad_adapter myAdapter = new Home_ad_adapter(mViewList);
                    mViewPager.setAdapter(myAdapter);
                    //  mViewPager.setCurrentItem(mCurrentItem);
                    Message message = new Message();
                    message.what = LUNBOFINSH;
                    handler.sendMessage(message);
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
    //四大类的OnResponseListener
    private OnResponseListener<String> mhomecategoryOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWhat) {
                //请求成功
                String result = response.get();
                try {
                    //解析第一层
                    JSONObject object = new JSONObject(result);
                    JSONArray homecategory = object.getJSONArray("data");
                    mHomeFourSortsList = new ArrayList<>();
                    for (int i = 0; i < homecategory.length(); i++) {
                        JSONObject jsonObject = homecategory.getJSONObject(i);
                        HomeFourSort homeFourSort = new HomeFourSort();
                        homeFourSort.setCategory_name(jsonObject.getString("category_name"));
                        homeFourSort.setCategory_image(jsonObject.getString("category_image"));
                        mHomeFourSortsList.add(homeFourSort);
                        Log.e("javabean", homeFourSort.getCategory_name() + homeFourSort.getCategory_image());
                    }

                    Glide.with(mMainActivity).load(mHomeFourSortsList.get(0).getCategory_image()).into(home_sort1image);
                    home_sort1text.setText(mHomeFourSortsList.get(0).getCategory_name());
                    Glide.with(mMainActivity).load(mHomeFourSortsList.get(1).getCategory_image()).into(home_sort2image);
                    home_sort2text.setText(mHomeFourSortsList.get(1).getCategory_name());
                    Glide.with(mMainActivity).load(mHomeFourSortsList.get(2).getCategory_image()).into(home_sort3image);
                    home_sort3text.setText(mHomeFourSortsList.get(2).getCategory_name());
                    Glide.with(mMainActivity).load(mHomeFourSortsList.get(3).getCategory_image()).into(home_sort4image);
                    home_sort4text.setText(mHomeFourSortsList.get(3).getCategory_name());

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
    private HomeContent hc;

    //专题的OnResponseListener
    private OnResponseListener<String> mOnRecycleResponseListener = new OnResponseListener<String>() {
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
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String zhuantiname = jsonObject.getString("name");
                        int is_app_show = jsonObject.getInt("is_app_show");
                        if (is_app_show == 0) {
                            //break;
                        } else {
                            //初始化mmlist
                            List<HomeContentContent> mmList = new ArrayList<>();
                            JSONArray array2 = jsonObject.getJSONArray("products");

                            Log.e("arrr22222", array2.length() + "");
                            for (int j = 0; j < array2.length(); j++) {
                                if (mmList.size() < 2) {
                                    //只显示两个
                                    JSONObject jsonObject1 = array2.getJSONObject(j);
                                    Log.e("循环外面", "dasdaafasf");
                                    if (!jsonObject1.getString("product").equals("false")) {
                                        Log.e("循环里面", jsonObject1.toString());
                                        JSONObject jsonObject2 = jsonObject1.getJSONObject("product");
                                        Log.e("内层循环", jsonObject2.toString());
                                        String image = jsonObject2.getString("image");
                                        String name = jsonObject2.getString("name");
                                        Double price = jsonObject2.getDouble("price");
                                        String extension4 = jsonObject2.getString("extension4");
                                        Log.e("专题", image + ":" + name);
                                        HomeContentContent hcc = new HomeContentContent(image, name, price, extension4);
                                        mmList.add(hcc);
                                    }
                                }
                            }
                            hc = new HomeContent(zhuantiname, mmList);
                            if (mmList.size() == 0) {
                                Log.e("看看mmlist里有什么", zhuantiname);
                            } else {
                                Log.e("看看mmlist里有什么", zhuantiname + mmList.get(0).toString());
                            }
                            mrecycleList.add(hc);
                        }
                    }
                    handler.sendEmptyMessage(RECYCLEFINSH);
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

    //轮播图的listener
    private void setLinstener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //手动滑到这个广告的时候,发送改位置的值
                mHandler.sendMessage(Message.obtain(mHandler, IMAGE_CHANGED, position, 0));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //获取数据的handler
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LUNBOFINSH:
                    //这是监听
                    setLinstener();
                case RECYCLEFINSH:
                    initrecycleAdapter();
                    initrecycleListener();

            }
        }
    };

    private void initrecycleListener() {
        hcAdapter.setmOnItemClickListener(new Home_ContentAdapter.OnMoreItemClickListener() {
            @Override
            public void onItemClick(View view, HomeContent hc) {
                if (hc.getName().equals("一周菜谱")){
                    Intent intent = new Intent(mMainActivity, HomexptjActivity.class);
                    startActivity(intent);
                Log.e("看看hc里有什么",hc.getName());
                }
                else{
                    Intent intent = new Intent(mMainActivity, HomezhuantiActivity.class);
                    intent.putExtra("zhuantiname",hc.getName());
                    startActivity(intent);
                }
            }
        });
    }

    private void initrecycleAdapter() {
        hcAdapter = new Home_ContentAdapter(mMainActivity, mrecycleList);
        mRecyclerView.setAdapter(hcAdapter);
        LinearLayoutManager linear = new LinearLayoutManager(mMainActivity);
        mRecyclerView.setLayoutManager(linear);
    }

    //轮播图的handler
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int action = msg.what;
            if (mHandler.hasMessages(IMAGE_UPDATE)) {
                mHandler.removeMessages(IMAGE_UPDATE);
            }
            switch (action) {

                case IMAGE_UPDATE:
                    //轮播图经行更新
                    mCurrentItem += 1;
                    mViewPager.setCurrentItem(mCurrentItem);
                    mHandler.sendEmptyMessageDelayed(IMAGE_UPDATE, REFRESHTIME);
                    break;
                case IMAGE_CHANGED:
                    //手动滑了广告
                    mCurrentItem = msg.arg1;
                    mViewPager.setCurrentItem(mCurrentItem);
                    mHandler.sendEmptyMessageDelayed(IMAGE_UPDATE, REFRESHTIME);
                    break;
            }
        }
    };


    //初始化视图
    private void initView() {
        ptrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.sort_content_ptr);
        mViewPager = (ViewPager) view.findViewById(R.id.main_ad_show);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        mSpinner = (Spinner) view.findViewById(R.id.home_spinner);
        mImageView = (ImageView) view.findViewById(R.id.home_jisuan);
        home_fangdajing= (ImageView) view.findViewById(R.id.home_fangdajing);
        home_fangdajing.setOnClickListener(this);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.cartitle);
        home_yzcpgengduo = (ImageView) view.findViewById(R.id.home_yzcpgengduo);
        home_sort1image = (ImageView) view.findViewById(R.id.home_sort1image);
        home_sort1text = (TextView) view.findViewById(R.id.home_sort1text);
        home_sort2image = (ImageView) view.findViewById(R.id.home_sort2image);
        home_sort2text = (TextView) view.findViewById(R.id.home_sort2text);
        home_sort3image = (ImageView) view.findViewById(R.id.home_sort3image);
        home_sort3text = (TextView) view.findViewById(R.id.home_sort3text);
        home_sort4image = (ImageView) view.findViewById(R.id.home_sort4image);
        home_sort4text = (TextView) view.findViewById(R.id.home_sort4text);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void caculate() {
        final Handler myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    if (mRelativeLayout.getWidth() != 0) {
                        //取消定时器
                        timer.cancel();
                        height = mRelativeLayout.getHeight();
                        if (height!=0){
                            //初始化spinner
                            mHomeSpinnerView = new HomeSpinnerView(mMainActivity, mSpinner, height);
                            mHomeSpinnerView.startspinner();
                        }
                    }
                }
            }
        };

        timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = 1;
                myHandler.sendMessage(message);
            }
        };
        //延迟每次延迟10 毫秒 隔1秒执行一次
        timer.schedule(task, 10, 1000);

    }

    @Override
    public void onClick(View v) {
        int buttonid = v.getId();
        switch (buttonid) {
            case R.id.home_fangdajing:
                Intent intent=new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}