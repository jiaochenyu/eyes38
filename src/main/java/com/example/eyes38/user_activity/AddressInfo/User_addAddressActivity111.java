package com.example.eyes38.user_activity.AddressInfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.eyes38.R;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class User_addAddressActivity111 extends AppCompatActivity {
    private ViewStub mStub1,mStub2;
    private Spinner province, city, area;
    private Map<String, Map<String, List<String>>> data = null;
    private String currentProvince;
    //当前的选择的城市
    private String currentCity;
    private String currentArea;
    private PullProvince pullProvince;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case PullProvince.PARSESUCCWSS://数据解析完毕，加载数据
                    data = (Map<String, Map<String, List<String>>>) msg.obj;
                    initData();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_address);
        province = (Spinner) findViewById(R.id.province);
        city = (Spinner) findViewById(R.id.city);
        area = (Spinner) findViewById(R.id.area);
        pullProvince = new PullProvince(mHandler);
        InputStream inStream = this.getClass().getClassLoader().
                getResourceAsStream("assets/" + "Area.xml");
        pullProvince.getProvinces(inStream);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (data != null) {
            String[] arrStrings = data.keySet().toArray(new String[0]);
            Log.e("TAG",arrStrings+"");
            System.out.println(arrStrings);
            //将省份信息填充到 province Spinner
            province.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, arrStrings));
            currentProvince = getCurrentProvince();
            bindCityAdapter(currentProvince);
            currentCity = getCurrentCity();
            bindAreaAdapter(currentCity);
            setOnItemSelectedListener();
        }
    }

    private void bindAreaAdapter(String currentCity) {
        // TODO Auto-generated method stub
        //根据当前显示的城市将对应的区填充到area Spinner
        area.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,data.
                get(currentProvince).get(currentCity))
        );
    }

    private void bindCityAdapter(String currentProvince) {
        // TODO Auto-generated method stub
        //根据当前显示的省份将对应的城市填充到city Spinner
        city.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, data.
                get(currentProvince).keySet().toArray(new String[0])));
    }

    /**
     * 为Spinner设置监听器
     */
    private void setOnItemSelectedListener() {
        // TODO Auto-generated method stub
        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                currentProvince = getCurrentProvince();
                bindCityAdapter(currentProvince);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                currentCity = getCurrentCity();
                bindAreaAdapter(currentCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }





    /**
     * 获取当前选择的省份
     *
     * @return String:当前选择的省份
     */
    private String getCurrentProvince() {
        // TODO Auto-generated method stub
        return province.getSelectedItem().toString();
    }

    /**
     * 获取当前选择的城市
     *
     * @return String:当前选择的城市
     */
    private String getCurrentCity() {
        // TODO Auto-generated method stub
        return city.getSelectedItem().toString();
    }
}
