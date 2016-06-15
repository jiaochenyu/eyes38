package com.example.eyes38.fragment.search;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.eyes38.R;
import com.example.eyes38.beans.Goods;
import com.example.eyes38.beans.SearchGoods;
import com.example.eyes38.fragment.search.search_adapter.GridAdapter;
import com.example.eyes38.fragment.search.search_adapter.ListAdapter;
import com.example.eyes38.fragment.search.search_adapter.SearchRecycleViewAdapter;
import com.example.eyes38.fragment.search.search_bean.bansearchBean;
import com.example.eyes38.fragment.search.utils.HistoryTable;
import com.example.eyes38.fragment.search.utils.MyHelper;
import com.example.eyes38.fragment.search.utils.SlidingMenu;
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
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class SearchActivity extends AppCompatActivity {
    public static final int mWhat2 = 25;
    public static final int MSHIFWHAT = 398;
    //创建数据库辅助类对象
    MyHelper myHelper;
    //声明一个数据库操作类
    SQLiteDatabase mDatabase;
    //声明一个游标对象
    Cursor mCursor;

    Button history_clear;//清空历史记录按钮
    String text;
    Toast mToast;
    public static final int mWhat = 911;
    public static final int mFinish = 922;
    private Context mContext;
    //数据库总量
    private List<SearchGoods> mList;
    //搜索结果
    private List<SearchGoods> resultList;
    //历史记录
    private List<bansearchBean> banList;
    //适配器
    SearchRecycleViewAdapter mSearchRecycleViewAdapter = null;
    ListAdapter mListAdapter;
    GridAdapter mGridAdapter;
    SlidingMenu mleftMenu;
    //gridbean
    List<Goods> mGridList;
    GridView mGridView;
    private ImageView search_back;
    private Button search_go;
    ImageView search_clear;
    private EditText mEditText;
    private boolean flag;
    private boolean notify;
    //综合等radiobutton
    private RadioButton search_comprise, search_num, search_price, search_num_down, search_price_down;
    //有无库存
    private CheckBox mCheckBox;
    //两个radiogroup
    RadioGroup search_category, search_caixi;
    //分类等radiobutton
    private RadioButton search_fruit, search_meat, search_water, search_green, search_riyong;
    //菜系分类
    private RadioButton search_common, search_special;
    //价格区间
    private EditText search_by_low, search_by_high;
    //确定及取消
    private Button search_window_cancel, search_window_yes;
    private Button search_sift;
    RecyclerView resultRecyclerView;
    ListView listView;
    LinearLayout now_Layout;
    RelativeLayout result_Layout;
    InputMethodManager imm;
    //采用nohttp
    private RequestQueue mRequestQueue;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case mFinish:
                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        initViews();
        initListeners();
    }

    //请求http获取
    private void getHttpMetod() {
        String url = "http://38eye.test.ilexnet.com/api/mobile/product-api/products";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("limit", "28");
        request.setCacheMode(CacheMode.DEFAULT);
        mRequestQueue.add(mWhat, request, mOnResponseListener);
    }

    /**
     * 请求http结果 回调对象，接受请求结果
     */
    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWhat) {
                //请求成功
                String result = response.get();
                mList = new ArrayList<>();//ssssss
                resultList.clear();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray data = object.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject = data.getJSONObject(i);
                        int id = jsonObject.getInt("product_id");
                        String name = jsonObject.getString("name");
                        String path = jsonObject.getString("image");
                        String unit = jsonObject.getString("extension4");
                        String txt_pic = jsonObject.getString("description");
                        float price = (float) jsonObject.getDouble("price");
                        float market_price = (float) jsonObject.getDouble("market_price");
                        JSONObject search = jsonObject.getJSONObject("product_search");
                        int comment_count = search.getInt("comment_num");
                        int sales_num = search.getInt("sales");
                        int stock = search.getInt("stock_num");
                        SearchGoods searchGoods = new SearchGoods(id, name, path, unit, market_price, price, comment_count, stock, txt_pic, "false", sales_num);
                        mList.add(searchGoods);
                    }
                    for (int i = 0; i < mList.size(); i++) {
                        //匹配条件  加上排序条件
                        if (mList.get(i).getGoods_name().contains(text.trim()) && search_comprise.isChecked()) {
                            //默认按销量*0.6+价格*0.4+评价*0.2排序
                            resultList.add(mList.get(i));
                            Collections.sort(resultList, new Comparator<SearchGoods>() {
                                @Override
                                public int compare(SearchGoods gd1, SearchGoods gd2) {
                                    //综合排名按降序排列
                                    if (gd1.getSales() * 0.6 + gd1.getGoods_platform_price() * 0.4 + gd1.getGoods_comment_count() * 0.2 < gd2.getSales() * 0.6 + gd2.getGoods_platform_price() * 0.4 + gd2.getGoods_comment_count() * 0.2) {
                                        return 1;
                                    }
                                    if (gd1.getSales() * 0.6 + gd1.getGoods_platform_price() * 0.4 + gd1.getGoods_comment_count() * 0.2 == gd2.getSales() * 0.6 + gd2.getGoods_platform_price() * 0.4 + gd2.getGoods_comment_count() * 0.2) {
                                        return 0;
                                    }
                                    return -1;
                                }
                            });
                        }

                    }
                    //此时判断resultList的大小
                    if (resultList.size() == 0) {
                        //未匹配结果,切换到之前界面
                        result_Layout.setVisibility(View.GONE);
                        resultList.clear();
                        now_Layout.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        show("抱歉，没有找到你想要的商品");
                    }

                    mSearchRecycleViewAdapter.notifyDataSetChanged();
                    //接着判断editText内容
                    mEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (mEditText.getText().toString().equals("")) {
                                flag = false;
                                search_clear.setVisibility(View.GONE);
                                result_Layout.setVisibility(View.GONE);
                                now_Layout.setVisibility(View.VISIBLE);

                            } else {
                                resultList.clear();
                                mList.clear();
                                //隐藏软键盘
                                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                searchResult();
                            }
                        }
                    });
                    Message message = new Message();
                    message.what = mFinish;
                    mHandler.sendMessage(message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (what == MSHIFWHAT) {
                //请求成功
                String result = response.get();
                mList = new ArrayList<>();//ssssss
                resultList.clear();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray data = object.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject = data.getJSONObject(i);
                        int id = jsonObject.getInt("product_id");
                        String name = jsonObject.getString("name");
                        String path = jsonObject.getString("image");
                        String unit = jsonObject.getString("extension4");
                        String txt_pic = jsonObject.getString("description");
                        float price = (float) jsonObject.getDouble("price");
                        float market_price = (float) jsonObject.getDouble("market_price");
                        JSONObject search = jsonObject.getJSONObject("product_search");
                        int comment_count = search.getInt("comment_num");
                        int sales_num = search.getInt("sales");
                        int stock = search.getInt("stock_num");
                        SearchGoods searchGoods = new SearchGoods(id, name, path, unit, market_price, price, comment_count, stock, txt_pic, "false", sales_num);
                        mList.add(searchGoods);
                    }
                    for (int i = 0; i < mList.size(); i++) {
                        //匹配条件  加上排序条件
                        if (mList.get(i).getGoods_name().contains(text.trim())) {
                            //默认按销量*0.6+价格*0.4+评价*0.2排序
                            resultList.add(mList.get(i));
                        }
                    }
                    //此时判断resultList的大小
                    if (resultList.size() == 0) {
                        //未匹配结果,切换到之前界面
                        show("抱歉，没有找到你想要的商品");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if (what == mWhat2) {
                //请求成功
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("data");
                    if (array.length() != 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            int id = jsonObject.getInt("product_id");
                            String name = jsonObject.getString("name");
                            String path = jsonObject.getString("image");
                            String unit = jsonObject.getString("extension4");
                            String txt_pic = jsonObject.getString("description");
                            float price = (float) jsonObject.getDouble("price");
                            float market_price = (float) jsonObject.getDouble("market_price");
                            JSONObject search = jsonObject.getJSONObject("product_search");
                            int comment_count = search.getInt("comment_num");
                            int stock = search.getInt("stock_num");
                            Goods goods = new Goods(id, name, path, unit, market_price, price, comment_count, stock, txt_pic);
                            mGridList.add(goods);
                        }
                    }
                    //随机生成9个(28以内的)不重复的随机整数
                    Random random = new Random();
                    boolean[] bool = new boolean[28];
                    List<Integer> integers = new ArrayList<>();
                    List<Goods> gridList = new ArrayList<>();
                    int randInt;
                    for (int i = 0; i < 9; i++) {
                        do {
                            randInt = random.nextInt(28);
                        } while (bool[randInt]);
                        bool[randInt] = true;
                        integers.add(randInt);
                    }
                    for (int i = 0; i < 9; i++) {
                        gridList.add(mGridList.get(integers.get(i)));
                    }
                    mGridAdapter = new GridAdapter(SearchActivity.this, gridList);
                    mGridView.setAdapter(mGridAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            Toast.makeText(SearchActivity.this, "数据请求失败，请重试!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinish(int what) {

        }
    };

    private void AdjustSearchChange() {
        //价格
        search_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDefine();
                //在判断价格升序还是降序
                search_num_down.setChecked(false);
                search_price_down.setClickable(true);
                search_price.setTextColor(getResources().getColor(R.color.topical));
                search_num.setTextColor(getResources().getColor(R.color.bottomtext));
                search_price_down.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        simpleDefine();
                        search_num_down.setClickable(false);
                        search_num.setTextColor(getResources().getColor(R.color.bottomtext));
                        search_price.setTextColor(getResources().getColor(R.color.topical));
                        Collections.sort(resultList, new Comparator<SearchGoods>() {
                            @Override
                            public int compare(SearchGoods gd1, SearchGoods gd2) {
                                //综合排名按降序排列>从低到高
                                if (gd1.getGoods_platform_price() > gd2.getGoods_platform_price()) {
                                    return 1;
                                }
                                if (gd1.getGoods_platform_price() == gd2.getGoods_platform_price()) {
                                    return 0;
                                }
                                return -1;
                            }
                        });
                        mSearchRecycleViewAdapter.notifyDataSetChanged();
                    }
                });
                Collections.sort(resultList, new Comparator<SearchGoods>() {
                    @Override
                    public int compare(SearchGoods gd1, SearchGoods gd2) {
                        //综合排名按降序排列>从低到高
                        if (gd1.getGoods_platform_price() < gd2.getGoods_platform_price()) {
                            return 1;
                        }
                        if (gd1.getGoods_platform_price() == gd2.getGoods_platform_price()) {
                            return 0;
                        }
                        return -1;
                    }
                });
                mSearchRecycleViewAdapter.notifyDataSetChanged();
            }
        });
        //综合
        search_comprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDefine();
                search_price.setTextColor(getResources().getColor(R.color.bottomtext));
                search_num.setTextColor(getResources().getColor(R.color.bottomtext));
                Collections.sort(resultList, new Comparator<SearchGoods>() {
                    @Override
                    public int compare(SearchGoods gd1, SearchGoods gd2) {
                        //价格默认排名按降序排列
                        if (gd1.getSales() * 0.6 + gd1.getGoods_platform_price() * 0.4 + gd1.getGoods_comment_count() * 0.2 < gd2.getSales() * 0.6 + gd2.getGoods_platform_price() * 0.4 + gd2.getGoods_comment_count() * 0.2) {
                            return 1;
                        }
                        if (gd1.getSales() * 0.6 + gd1.getGoods_platform_price() * 0.4 + gd1.getGoods_comment_count() * 0.2 == gd2.getSales() * 0.6 + gd2.getGoods_platform_price() * 0.4 + gd2.getGoods_comment_count() * 0.2) {
                            return 0;
                        }
                        return -1;
                    }
                });
                mSearchRecycleViewAdapter.notifyDataSetChanged();
            }
        });


        //销量
        search_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleDefine();
                search_num.setTextColor(getResources().getColor(R.color.topical));
                search_price.setTextColor(getResources().getColor(R.color.bottomtext));
                search_price_down.setChecked(false);
                search_num_down.setClickable(true);
                //在对升序还是降序进行监听
                search_num_down.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        simpleDefine();
                        search_price.setTextColor(getResources().getColor(R.color.bottomtext));
                        search_price_down.setClickable(false);
                        search_num.setTextColor(getResources().getColor(R.color.topical));
                        Collections.sort(resultList, new Comparator<SearchGoods>() {
                            @Override
                            public int compare(SearchGoods gd1, SearchGoods gd2) {
                                //综合排名按降序排列
                                if (gd1.getSales() > gd2.getSales()) {
                                    return 1;
                                }
                                if (gd1.getSales() == gd2.getSales()) {
                                    return 0;
                                }
                                return -1;
                            }
                        });
                        mSearchRecycleViewAdapter.notifyDataSetChanged();
                    }
                });
                Collections.sort(resultList, new Comparator<SearchGoods>() {
                    @Override
                    public int compare(SearchGoods gd1, SearchGoods gd2) {
                        //综合排名默认按降序排列
                        if (gd1.getSales() < gd2.getSales()) {
                            return 1;
                        }
                        if (gd1.getSales() == gd2.getSales()) {
                            return 0;
                        }
                        return -1;
                    }
                });
                mSearchRecycleViewAdapter.notifyDataSetChanged();
            }
        });
    }

    //事件监听
    private void initListeners() {
        //判断
        AdjustSearchChange();
        //筛选部分的监听
        SearchShifListener();
        search_sift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mleftMenu.toggle();
            }
        });
        //监听清除数据按键
        history_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = myHelper.getReadableDatabase();
                banList.clear();
                String sql1 = "delete from historylist";
                String sql2 = "update sqlite_sequence set seq=0 where name='historylist'";
                mDatabase.execSQL(sql1);
                mDatabase.execSQL(sql2);
                mDatabase.close();
                showHistory();
                if (banList.size() == 0) {
                    mListAdapter.notifyDataSetChanged();
                    history_clear.setText("没有历史数据可以清理");
                }
            }
        });
        //返回键
        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //若有错误，需要隐藏键盘
            }
        });
        //清除输入内容
        search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
                search_clear.setVisibility(View.GONE);
                result_Layout.setVisibility(View.GONE);
                now_Layout.setVisibility(View.VISIBLE);

            }
        });
        //开始搜索
        searchResult();

    }

    private void searchResult() {
        //开始搜索
        search_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    //隐藏软键盘
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    now_Layout.setVisibility(View.GONE);
                    result_Layout.setVisibility(View.VISIBLE);
                    search_clear.setVisibility(View.VISIBLE);
                    text = mEditText.getText().toString().trim();
                    //遍历horistry表中的数据
                    showHistoryData();
                    //判断是否需要插入历史记录
                    if (notify == true) {
                        insertData();
                    }
                    getHttpMetod();
                } else if (!flag) {
                    search_clear.setVisibility(View.GONE);
                    show("请输入关键字");
                }
            }
        });
    }

    //遍历history表
    private void showHistoryData() {
        mDatabase = myHelper.getReadableDatabase();
        //查询
        banList.clear();
        mCursor = mDatabase.query(HistoryTable.Field.TABLE_NAME, null, null, null, null, null, HistoryTable.Field._ID + " desc");
        while (mCursor.moveToNext()) {
            String name = mCursor.getString(mCursor.getColumnIndex(HistoryTable.Field.HISTORY_NAME));
            bansearchBean bansearchBean = new bansearchBean(name);
            banList.add(bansearchBean);
        }
        if (banList.size() == 0) {
            notify = true;
        } else {
            //用于判断list中的数量和内容
            for (int i = 0; i < banList.size(); i++) {
                if (banList.get(i).getContent().equals(text)) {
                    notify = false;
                } else {
                    notify = true;
                }
            }
        }
        mDatabase.close();
    }

    //历史搜素记录
    private void insertData() {
        mDatabase = myHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", text);
        mDatabase.insert(HistoryTable.Field.TABLE_NAME, null, values);
        //关闭连接
        mDatabase.close();
        //更新数据
        setAdapter();

    }

    private void setAdapter() {
        showHistory();
        if (mListAdapter == null) {
            mListAdapter = new ListAdapter(banList, SearchActivity.this);
            listView.setAdapter(mListAdapter);
        } else {
            mListAdapter.notifyDataSetChanged();
        }

    }

    private void showHistory() {
        mDatabase = myHelper.getReadableDatabase();
        //清空所有数据
        banList.clear();
        //查询
        mCursor = mDatabase.query(HistoryTable.Field.TABLE_NAME, null, null, null, null, null, HistoryTable.Field._ID + " desc");
        while (mCursor.moveToNext()) {
            String name = mCursor.getString(mCursor.getColumnIndex(HistoryTable.Field.HISTORY_NAME));
            bansearchBean bansearchBean = new bansearchBean(name);
            banList.add(bansearchBean);
        }
        if (banList.size() > 0) {
            history_clear.setText("清除历史搜索");//sssssssssssss
        }
        mDatabase.close();
    }


    private void initViews() {
        mGridView = (GridView) findViewById(R.id.hotGridView);
        mRequestQueue = NoHttp.newRequestQueue();
        //绑定gridview
        gridViewSearch();
        history_clear = (Button) findViewById(R.id.history_clear);
        //创建数据库对象N
        mleftMenu = (SlidingMenu) findViewById(R.id.id_menu);
        //筛选按钮
        search_sift = (Button) findViewById(R.id.search_sift);
        search_comprise = (RadioButton) findViewById(R.id.search_comprise);
        search_num = (RadioButton) findViewById(R.id.search_num);
        search_price = (RadioButton) findViewById(R.id.search_price);
        search_num_down = (RadioButton) findViewById(R.id.search_num_down);
        search_price_down = (RadioButton) findViewById(R.id.search_price_down);
        //筛选监听
        searchByShit();
        myHelper = new MyHelper(SearchActivity.this);
        now_Layout = (LinearLayout) findViewById(R.id.now_linear);
        result_Layout = (RelativeLayout) findViewById(R.id.result_linear);
        search_back = (ImageView) findViewById(R.id.search_back);
        search_go = (Button) findViewById(R.id.search_go);
        search_clear = (ImageView) findViewById(R.id.search_clear);
        mEditText = (EditText) findViewById(R.id.search_edit);
        listView = (ListView) findViewById(R.id.bad_list);
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycle);
        mList = new ArrayList<>();
        resultList = new ArrayList<>();
        mSearchRecycleViewAdapter = new SearchRecycleViewAdapter(resultList, SearchActivity.this);
        resultRecyclerView.setAdapter(mSearchRecycleViewAdapter);
        banList = new ArrayList<>();
        setAdapter();
        someViewListener();

    }

    //筛选按钮监听
    private void searchByShit() {
        mCheckBox = (CheckBox) findViewById(R.id.search_by_yes);
        search_category = (RadioGroup) findViewById(R.id.search_category);
        search_caixi = (RadioGroup) findViewById(R.id.search_caixi);
        search_fruit = (RadioButton) findViewById(R.id.search_fruit);
        search_meat = (RadioButton) findViewById(R.id.search_meat);
        search_water = (RadioButton) findViewById(R.id.search_water);
        search_green = (RadioButton) findViewById(R.id.search_green);
        search_riyong = (RadioButton) findViewById(R.id.search_riyong);
        search_common = (RadioButton) findViewById(R.id.search_common);
        search_special = (RadioButton) findViewById(R.id.search_special);
        search_by_low = (EditText) findViewById(R.id.search_by_low);
        search_by_high = (EditText) findViewById(R.id.search_by_high);
        search_window_cancel = (Button) findViewById(R.id.search_window_cancel);
        search_window_yes = (Button) findViewById(R.id.search_window_yes);
    }

    //筛选按钮的监听
    private void SearchShifListener() {
        //烦躁烦躁烦躁烦躁烦躁
        //确定按钮的监听
        search_window_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新请求下数据
                getShifHttpMetod();
                if (mCheckBox.isChecked()) {
                    Iterator<SearchGoods> iterator = resultList.iterator();
                    while (iterator.hasNext()) {
                        SearchGoods searchGoods = iterator.next();
                        if (searchGoods.getGoods_stock() == 0) {
                            iterator.remove();
                        }
                    }
                }
                float low, high;
                //价格区间
                String text1 = search_by_low.getText().toString();
                String text2 = search_by_high.getText().toString();
                if (text1.equals("")) {
                    text1 = "-0.1";
                }
                if (text2.equals("")) {
                    text2 = "9999999999";
                }
                low = Float.parseFloat(text1);
                high = Float.parseFloat(text2);
                //这里需要使用增强的for循环，不然容易显示错误
                Iterator<SearchGoods> iterator = resultList.iterator();
                while (iterator.hasNext()) {
                    SearchGoods searchGoods = iterator.next();
                    if (searchGoods.getGoods_platform_price() < low || searchGoods.getGoods_platform_price() > high) {
                        iterator.remove();
                    }
                }
                mSearchRecycleViewAdapter.notifyDataSetChanged();
                mleftMenu.toggle();
                //为什么要多点击几次,才正确显示呢?????
            }

        });

        //取消按钮监听
        search_window_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mleftMenu.toggle();
                //还原默认设置
                mCheckBox.setChecked(false);
                search_by_low.setText("");
                search_by_high.setText("");
            }
        });

    }

    //每次筛选之间重新获取resultList集合
    private void getShifHttpMetod() {
        String url = "http://38eye.test.ilexnet.com/api/mobile/product-api/products";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("limit", "28");
        request.setCacheMode(CacheMode.DEFAULT);
        mRequestQueue.add(MSHIFWHAT, request, mOnResponseListener);
    }

    private void someViewListener() {
        //将历史数据上的放在文本框上
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mEditText.setText(banList.get(position).getContent());
            }
        });
        //隐藏软键盘
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(mEditText.getWindowToken(), 0);//强制隐藏软键盘
        //设置cycleView的布局管理
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchActivity.this, 2);
        resultRecyclerView.setLayoutManager(gridLayoutManager);
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setFocusableInTouchMode(true);
                //将软键盘弹出
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);
            }
        });
        //在判断是否内容被编辑
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEditText.getText().toString().equals("")) {
                    flag = false;
                } else {
                    search_clear.setVisibility(View.VISIBLE);
                    flag = true;

                }
            }
        });
    }

    //绑定gridview
    private void gridViewSearch() {
        mGridList = new ArrayList<>();
        getHttpMetod2();

    }

    //获取28个中9个数据
    private void getHttpMetod2() {
        String url = "http://38eye.test.ilexnet.com/api/mobile/product-api/products";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.add("limit", "28");
        request.setCacheMode(CacheMode.DEFAULT);
        mRequestQueue.add(mWhat2, request, mOnResponseListener);
    }
    public void show(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(SearchActivity.this, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public void simpleDefine() {
        //相当于确定操作
        if (mCheckBox.isChecked()) {
            Iterator<SearchGoods> iterator = resultList.iterator();
            while (iterator.hasNext()) {
                SearchGoods searchGoods = iterator.next();
                if (searchGoods.getGoods_stock() == 0) {
                    iterator.remove();
                }
            }
        }
        float low, high;
        //价格区间
        String text1 = search_by_low.getText().toString();
        String text2 = search_by_high.getText().toString();
        if (text1.equals("")) {
            text1 = "-0.1";
        }
        if (text2.equals("")) {
            text2 = "9999999999";
        }
        low = Float.parseFloat(text1);
        high = Float.parseFloat(text2);
        //这里需要使用增强的for循环，不然容易显示错误
        Iterator<SearchGoods> iterator = resultList.iterator();
        while (iterator.hasNext()) {
            SearchGoods searchGoods = iterator.next();
            if (searchGoods.getGoods_platform_price() < low || searchGoods.getGoods_platform_price() > high) {
                iterator.remove();
            }
        }
    }

}



