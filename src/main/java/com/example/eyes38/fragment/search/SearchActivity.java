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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eyes38.R;
import com.example.eyes38.fragment.search.search_adapter.GridAdapter;
import com.example.eyes38.fragment.search.search_bean.gridBean;
import com.example.eyes38.fragment.search.utils.HistoryTable;
import com.example.eyes38.fragment.search.search_adapter.ListAdapter;
import com.example.eyes38.fragment.search.utils.MyHelper;
import com.example.eyes38.fragment.search.search_bean.SearchBean;
import com.example.eyes38.fragment.search.search_adapter.SearchRecycleViewAdapter;
import com.example.eyes38.fragment.search.search_bean.bansearchBean;
import com.example.eyes38.utils.DividerItemDecoration;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    public static final int HISTORY_NUM = 10;//显示的历史数据条数
    //创建数据库辅助类对象
    MyHelper myHelper;
    //声明一个数据库操作类
    SQLiteDatabase mDatabase;
    //声明一个游标对象
    Cursor mCursor;
    Button history_clear;
    String text;
    Toast mToast;
    public static final int mWhat = 911;
    public static final int mFinish = 922;
    private Context mContext;
    //数据库总量
    private List<SearchBean> mList;
    //搜索结果
    private List<SearchBean> resultList;
    //历史记录
    private List<bansearchBean> banList;
    //适配器
    SearchRecycleViewAdapter mSearchRecycleViewAdapter = null;
    ListAdapter mListAdapter;
    GridAdapter mGridAdapter;
    //gridbean
    List<gridBean> mGridList;
    GridView mGridView;
    private ImageView search_back;
    private Button search_go;
    ImageView search_clear;
    private EditText mEditText;
    private boolean flag;
    private boolean notify;
    private boolean listview_clear = false;
    RecyclerView resultRecyclerView;
    ListView listView;
    LinearLayout result_Layout, now_Layout;
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
        mRequestQueue = NoHttp.newRequestQueue();
        String url = "http://fuwuqi.guanweiming.top/headvip/json/testdata";
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        request.setRequestFailedReadCache(true);
        request.add("size", "7");
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
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray data = object.getJSONArray("goods");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object1 = data.getJSONObject(i);
                        SearchBean searchBean = new SearchBean();
                        String image = object1.getString("pic");
                        String name = object1.getString("title");
                        float price = Float.parseFloat(object1.getString("id"));
                        String size = object1.getString("content");
                        searchBean.setName(name);
                        searchBean.setPic(image);
                        searchBean.setPrice(price);
                        searchBean.setSize(size);
                        mList.add(searchBean);
                        Log.e("TAG", image + "-->" + name + "-->" + price + "-->" + size);
                    }
                    for (int i = 0; i < mList.size(); i++) {
                        //匹配条件
                        if (mList.get(i).getName().contains(text.trim()) || mList.get(i).getSize().contains(text.trim())) {
                            Log.e("AA", text);
                            resultList.add(mList.get(i));
                        } else {
                            //未匹配结果,切换到之前界面
                            result_Layout.setVisibility(View.GONE);
                            resultList.clear();
                            Log.e("CCC", resultList.size() + "");
                            now_Layout.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.VISIBLE);
                            show("抱歉，没有找到你想要的商品");
                            break;
                        }
                    }
                    if (mSearchRecycleViewAdapter == null) {
                        mSearchRecycleViewAdapter = new SearchRecycleViewAdapter(resultList, SearchActivity.this);
                        resultRecyclerView.setAdapter(mSearchRecycleViewAdapter);
                    } else {
                        mSearchRecycleViewAdapter.notifyDataSetChanged();
                    }

                    Log.e("bb", "sss");
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
                                mSearchRecycleViewAdapter.notifyDataSetChanged();
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
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }

        @Override
        public void onFinish(int what) {

        }
    };


    //事件监听
    private void initListeners() {
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
                onBackPressed();
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
        mCursor = mDatabase.query(HistoryTable.Field.TABLE_NAME, null, null, null, null, null, null);
        while (mCursor.moveToNext()) {
            String name = mCursor.getString(mCursor.getColumnIndex(HistoryTable.Field.HISTORY_NAME));
            bansearchBean bansearchBean = new bansearchBean(name);
            banList.add(bansearchBean);
        }
        Log.e("QWER", banList.size() + "");
        if (banList.size() == 0) {
            notify = true;
        } else {
            //用于判断list中的数量和内容
            for (int i = 0; i < banList.size(); i++) {
                if (banList.get(i).getContent().contains(text.trim()) || banList.size() > HISTORY_NUM) {
                    notify = false;
                } else {
                    notify = true;
                }
            }
        }
        Log.e("QWER", "" + notify);

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
        mCursor = mDatabase.query(HistoryTable.Field.TABLE_NAME, null, null, null, null, null, null);
        while (mCursor.moveToNext()) {
            String name = mCursor.getString(mCursor.getColumnIndex(HistoryTable.Field.HISTORY_NAME));
            bansearchBean bansearchBean = new bansearchBean(name);
            banList.add(bansearchBean);
        }
        Log.e("QWER", banList.size() + "GG");
        if (banList.size() > 0) {
            history_clear.setText("清除历史搜索");//sssssssssssss
        }
        mDatabase.close();
    }


    private void initViews() {
        mGridView = (GridView) findViewById(R.id.hotGridView);
        //绑定gridview
        gridViewSearch();
        history_clear = (Button) findViewById(R.id.history_clear);
        //创建数据库对象N
        myHelper = new MyHelper(SearchActivity.this);
        now_Layout = (LinearLayout) findViewById(R.id.now_linear);
        result_Layout = (LinearLayout) findViewById(R.id.result_linear);
        search_back = (ImageView) findViewById(R.id.search_back);
        search_go = (Button) findViewById(R.id.search_go);
        search_clear = (ImageView) findViewById(R.id.search_clear);
        mEditText = (EditText) findViewById(R.id.search_edit);
        listView = (ListView) findViewById(R.id.bad_list);
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycle);
        mList = new ArrayList<>();
        resultList = new ArrayList<>();
        banList = new ArrayList<>();
        setAdapter();
        //隐藏软键盘
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(mEditText.getWindowToken(), 0);//强制隐藏软键盘
        //设置cycleView的布局管理
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchActivity.this, 2);
        resultRecyclerView.setLayoutManager(gridLayoutManager);
        resultRecyclerView.addItemDecoration(new DividerItemDecoration(SearchActivity.this, 1));
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setFocusableInTouchMode(true);
                //将软键盘弹出
                imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText,InputMethodManager.SHOW_FORCED);
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
        //假数据
        for (int i = 0; i < 9; i++) {
            gridBean gridBean = new gridBean();
            gridBean.setText("热搜" + i);
            mGridList.add(gridBean);
        }
        mGridAdapter = new GridAdapter(SearchActivity.this, mGridList);
        mGridView.setAdapter(mGridAdapter);
    }

    public void show(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(SearchActivity.this, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

}



