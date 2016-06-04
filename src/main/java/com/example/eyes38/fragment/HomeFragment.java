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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eyes38.MainActivity;
import com.example.eyes38.R;
import com.example.eyes38.activity.SortMenuActivity;
import com.example.eyes38.activity.home.HomexptjActivity;
import com.example.eyes38.activity.home.HomezhuantiActivity;
import com.example.eyes38.adapter.Home_ContentAdapter;
import com.example.eyes38.adapter.Home_ad_adapter;
import com.example.eyes38.beans.HomeContent;
import com.example.eyes38.beans.HomeContentContent;
import com.example.eyes38.beans.HomeFourSort;
import com.example.eyes38.beans.Home_district;
import com.example.eyes38.beans.SortContentContent;
import com.example.eyes38.fragment.home.HomeRecycleView;
import com.example.eyes38.fragment.home.HomeSpinnerView;
import com.example.eyes38.fragment.search.SearchActivity;
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
    private int mWhatspinner = 124;
    private int mWhatlunbo = 125;
    private int mWhatfoursort = 126;
    private int mWhatrecycle = 127;
    ArrayList<View> mViewList;
    List<String> mList;
    List<String> spinnerlist;
    List<HomeContent> mrecycleList;
    List<HomeFourSort> mHomeFourSortsList;
    Home_ContentAdapter hcAdapter;
    public static final int LUNBOFINSH = 3;
    public static final int SPINNERFINSH = 5;
    public static final int RECYCLEFINSH = 4;
    int mCurrentItem = Integer.MAX_VALUE / 2;
    public static final int IMAGE_UPDATE = 1;
    public static final int REFRESHTIME = 5 * 1000;
    public static final int IMAGE_CHANGED = 2;

    ImageView mImageView;
    int height;
    RelativeLayout mRelativeLayout;
    private RequestQueue mRequestQueue;
    private ArrayList<Home_district> mCity;//城市的信息
    int district_id;
    private HomeContent hc;

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
    private ArrayAdapter<String> spinnerapapter;
    private LinearLayout home_sort1layout;
    private LinearLayout home_sort2layout;
    private LinearLayout home_sort3layout;
    private LinearLayout home_sort4layout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, null);
        mMainActivity = (MainActivity) getActivity();
        initView();
        // caculate();
        refresh();
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
                        refresh();
                    }
                }, 1800);

            }
        });
    }

    private void getHttpMethod(String url, int what) {
        mrecycleList = new ArrayList<>();
        mRequestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("district_id", district_id);
        request.setCacheMode(CacheMode.DEFAULT);
        mRequestQueue.add(what, request, mOnResponseListener);
        //request.setRequestFailedReadCache(true);
        mRequestQueue.add(what, request, mOnResponseListener);

    }

    //ResponseListener
    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWhatspinner) {
                //spinner获取数据
                String result = response.get();
                try {
                    //解析第一层
                    JSONObject object = new JSONObject(result);
                    JSONArray homefirst = object.getJSONArray("data");
                    spinnerlist = new ArrayList<String>();
                    mCity = new ArrayList<Home_district>();
                    for (int i = 0; i < homefirst.length(); i++) {
                        JSONObject jsonObject = homefirst.getJSONObject(i);
                        //mCartGoods.setPath(jsonObject.getString("create_date"));
                        JSONObject mtest = jsonObject.getJSONObject("district");
                        String name = mtest.getString("name");
                        int district_id = mtest.getInt("district_id");
                        Home_district home_district = new Home_district(name, district_id);
                        mCity.add(home_district);
                        spinnerlist.add(name);
                        Log.e("获取的数据哈达和", jsonObject.getString("create_date") + " " + name + district_id);
                    }
                    initspinnerAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (what == mWhatfoursort) {
                //四大类获取数据并实现
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

            } else if (what == mWhatlunbo) {
                //轮播图获取数据并实现
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
                    setlunboLinstener();
                    // mViewPager.setCurrentItem(mCurrentItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (what == mWhatrecycle) {
                //recycle获取数据并实现
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String zhuantiname = jsonObject.getString("name");
                        int is_app_show = jsonObject.getInt("is_app_show");
                        if (is_app_show == 0) {
                        } else {
                            //初始化mmlist
                            List<HomeContentContent> mmList = new ArrayList<>();
                            JSONArray array2 = jsonObject.getJSONArray("products");
                            for (int j = 0; j < array2.length(); j++) {
                                if (mmList.size() < 2) {
                                    //只显示两个
                                    JSONObject jsonObject1 = array2.getJSONObject(j);
                                    if (!jsonObject1.getString("product").equals("false")) {
                                        JSONObject jsonObject2 = jsonObject1.getJSONObject("product");
                                        String image = jsonObject2.getString("image");
                                        String name = jsonObject2.getString("name");
                                        Double price = jsonObject2.getDouble("price");
                                        String extension4 = jsonObject2.getString("extension4");
                                        HomeContentContent hcc = new HomeContentContent(image, name, price, extension4);
                                        mmList.add(hcc);
                                    }
                                }
                            }
                            hc = new HomeContent(zhuantiname, mmList);
                            mrecycleList.add(hc);
                        }
                    }
                    initrecycleAdapter();
                    initrecycleListener();
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

    //spinner的适配器
    private void initspinnerAdapter() {
        spinnerapapter = new ArrayAdapter<String>(mMainActivity, R.layout.home_spinner, spinnerlist);
        // 3:adapter设置一个下拉列表样式
        spinnerapapter.setDropDownViewResource(R.layout.home_spinner_item);
        mSpinner.setDropDownHorizontalOffset(200);
        mSpinner.setDropDownVerticalOffset(height);
        //  4:spinner加载适配器
        mSpinner.setAdapter(spinnerapapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cityName = spinnerapapter.getItem(position);
                for (int i = 0; i < mCity.size(); i++) {
                    if (mCity.get(i).getCityName().equals(cityName))
                        district_id = mCity.get(i).getDistrict_id();
                }
                Log.e("spinnersize", district_id + "");
                refresh1();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //轮播图的listener
    private void setlunboLinstener() {
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

    private void initrecycleListener() {
        hcAdapter.setmOnItemClickListener(new Home_ContentAdapter.OnMoreItemClickListener() {
            @Override
            public void onItemClick(View view, HomeContent hc) {
                if (hc.getName().equals("一周菜谱")) {
                    Intent intent = new Intent(mMainActivity, HomexptjActivity.class);
                    startActivity(intent);
                    Log.e("看看hc里有什么", hc.getName());
                } else {
                    Intent intent = new Intent(mMainActivity, HomezhuantiActivity.class);
                    intent.putExtra("zhuantiname", hc.getName());
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
        home_fangdajing = (ImageView) view.findViewById(R.id.home_fangdajing);
        home_fangdajing.setOnClickListener(this);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.cartitle);
        home_yzcpgengduo = (ImageView) view.findViewById(R.id.home_yzcpgengduo);
        home_sort1layout= (LinearLayout) view.findViewById(R.id.home_sort1layout);
        home_sort1layout.setOnClickListener(this);
        home_sort1image = (ImageView) view.findViewById(R.id.home_sort1image);
        home_sort1text = (TextView) view.findViewById(R.id.home_sort1text);
        home_sort2layout= (LinearLayout) view.findViewById(R.id.home_sort2layout);
        home_sort2layout.setOnClickListener(this);
        home_sort2image = (ImageView) view.findViewById(R.id.home_sort2image);
        home_sort2text = (TextView) view.findViewById(R.id.home_sort2text);
        home_sort3layout= (LinearLayout) view.findViewById(R.id.home_sort3layout);
        home_sort3layout.setOnClickListener(this);
        home_sort3image = (ImageView) view.findViewById(R.id.home_sort3image);
        home_sort3text = (TextView) view.findViewById(R.id.home_sort3text);
        home_sort4layout= (LinearLayout) view.findViewById(R.id.home_sort4layout);
        home_sort4layout.setOnClickListener(this);
        home_sort4image = (ImageView) view.findViewById(R.id.home_sort4image);
        home_sort4text = (TextView) view.findViewById(R.id.home_sort4text);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        int buttonid = v.getId();
        switch (buttonid) {
            case R.id.home_fangdajing:
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.home_sort1layout:
                Intent intent1 = new Intent(mMainActivity, SortMenuActivity.class);
                Bundle bundle = new Bundle();
                SortContentContent scc = new SortContentContent(1,"水果",null);
                bundle.putSerializable("values",scc);
                intent1.putExtra("values",bundle);
                startActivity(intent1);
                break;
            case R.id.home_sort2layout:
                Intent intent2 = new Intent(mMainActivity, SortMenuActivity.class);
                Bundle bundle2 = new Bundle();
                SortContentContent scc2 = new SortContentContent(2,"鲜肉",null);
                bundle2.putSerializable("values",scc2);
                intent2.putExtra("values",bundle2);
                startActivity(intent2);
                break;
            case R.id.home_sort3layout:
                Intent intent3 = new Intent(mMainActivity, SortMenuActivity.class);
                Bundle bundle3 = new Bundle();
                SortContentContent scc3 = new SortContentContent(3,"水产",null);
                bundle3.putSerializable("values",scc3);
                intent3.putExtra("values",bundle3);
                startActivity(intent3);
                break;
            case R.id.home_sort4layout:
                Intent intent4 = new Intent(mMainActivity, SortMenuActivity.class);
                Bundle bundle4 = new Bundle();
                SortContentContent scc4 = new SortContentContent(4,"水产",null);
                bundle4.putSerializable("values",scc4);
                intent4.putExtra("values",bundle4);
                startActivity(intent4);
                break;
        }
    }

    private void refresh() {
        //计算屏幕的尺寸
        //   caculate();
        //获取spinner的数据并实现
        getHttpMethod("http://38eye.test.ilexnet.com/api/mobile/sell-district/list", mWhatspinner);
        //获取轮播图的数据并实现,最新接口没数据,用的测试接口的数据
        getHttpMethod("http://38eye.test.ilexnet.com/api/mobile/article-api/banner-images?code=app_home_banner", mWhatlunbo);
        //获取四大类的数据并实现
        getHttpMethod("http://38eye.test.ilexnet.com/api/mobile/home-category/list", mWhatfoursort);
        //获取recycleView的数据并实现
        getHttpMethod("http://38eye.test.ilexnet.com/api/mobile/special-product/listConfig", mWhatrecycle);
        listener();
    }

    private void refresh1() {
        getHttpMethod("http://38eye.test.ilexnet.com/api/mobile/article-api/banner-images?code=app_home_banner", mWhatlunbo);
        //获取四大类的数据并实现
        getHttpMethod("http://38eye.test.ilexnet.com/api/mobile/home-category/list", mWhatfoursort);
        //获取recycleView的数据并实现
        getHttpMethod("http://38eye.test.ilexnet.com/api/mobile/special-product/listConfig", mWhatrecycle);
        listener();
    }

    private void caculate() {
        final Handler myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    if (mRelativeLayout.getWidth() != 0) {
                        timer.cancel();
                        height = mRelativeLayout.getHeight();
                        if (height != 0) {
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
}