package com.example.eyes38.EventActivity;

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.example.eyes38.R;
import com.example.eyes38.adapter.EventFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {
    RadioGroup mRadioGroup;
    View view;
    ViewPager mViewPager;
    List<Fragment> mFragmentList;
    EventDay1 mEventDay1;
    EventDay2 mEventDay2;
    EventDay3 mEventDay3;
    EventDay4 mEventDay4;
    EventDay5 mEventDay5;
    EventDay6 mEventDay6;
    EventDay7 mEventDay7;
    EventFragmentAdapter mEventFragmentAdapter;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private EventSyncHorizontalScrollView mEventSyncHorizontalScrollView;
    private RelativeLayout mRelativeLayout;
    private ImageView mImageViewIndicator;
    private ImageView mImageViewScrollLeft;
    private ImageView mImageViewScrollRight;
    private int indicatorWidth;//下划线的宽度
    public static String[] tabTitle = {"A1", "B2", "C3", "D4", "E5", "F6", "G7"};
    private LayoutInflater mInflater;
    private int currentIndicatorLeft=0;//当前下划线和左边的距离

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        initViews();
        initData();
        initListener();
    }

    private void initListener() {
        //radioGroup 事件监听
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mRadioGroup.getChildAt(checkedId) != null) {
                    //将按钮移动
                    TranslateAnimation animation = new TranslateAnimation(
                            currentIndicatorLeft,
                            (mRadioGroup.getChildAt(checkedId)).getLeft(), 0f, 0f);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setDuration(100);
                    animation.setFillAfter(true);

                    //执行位移动画
                    mImageViewIndicator.startAnimation(animation);
                    mViewPager.setCurrentItem(checkedId);    //ViewPager 跟随一起 切换
                    //记录当前 下标的距最左侧的 距离
                    currentIndicatorLeft = ( mRadioGroup.getChildAt(checkedId)).getLeft();

                    mEventSyncHorizontalScrollView.smoothScrollTo(
                            (checkedId > 1 ? ( mRadioGroup.getChildAt(checkedId)).getLeft() : 0) - ( mRadioGroup.getChildAt(2)).getLeft(), 0);
                }
            }

        });

        //viewpager事件监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onPageSelected(int position) {
                if (mRadioGroup!=null&&mRadioGroup.getChildCount()>position){
                    mRadioGroup.getChildAt(position).performClick();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initData() {
        mFragmentList=new ArrayList<>();
        mEventDay1=new EventDay1();
        mEventDay2=new EventDay2();
        mEventDay3=new EventDay3();
        mEventDay4=new EventDay4();
        mEventDay5=new EventDay5();
        mEventDay6=new EventDay6();
        mEventDay7=new EventDay7();
        mFragmentList.add(mEventDay1);
        mFragmentList.add(mEventDay2);
        mFragmentList.add(mEventDay3);
        mFragmentList.add(mEventDay4);
        mFragmentList.add(mEventDay5);
        mFragmentList.add(mEventDay6);
        mFragmentList.add(mEventDay7);
        //获得屏幕分辨率
        DisplayMetrics dm=new DisplayMetrics();//获取屏幕分辨率
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        indicatorWidth=dm.widthPixels/4;//显示四个标题

        //设置标签下划线的属性
        ViewGroup.LayoutParams cursor_Params=mImageViewIndicator.getLayoutParams();
        cursor_Params.width=indicatorWidth;//初始化滑动下标的宽度
        mImageViewIndicator.setLayoutParams(cursor_Params);
        mEventSyncHorizontalScrollView.setSomeParam(mRelativeLayout,mImageViewScrollLeft,mImageViewScrollRight,this);
        //获取布局填充器
        mInflater=LayoutInflater.from(this);
        //标签在horizonScollView中应该移动的位置
        initNavigationHSV();
        mFragmentManager=getSupportFragmentManager();
        //初始化适配器
        mEventFragmentAdapter=new EventFragmentAdapter(mFragmentManager,mFragmentList);
        mViewPager.setAdapter(mEventFragmentAdapter);
        mViewPager.setOffscreenPageLimit(6);







    }
    //标签在horizonScrollView中应该移动的位置
    private void initNavigationHSV() {
        mRadioGroup.removeAllViews();
        for (int i=0;i<tabTitle.length;i++){
            RadioButton rb= (RadioButton) mInflater.inflate(R.layout.event_day_radiogroup_item,null);
            rb.setId(i);
            rb.setText(tabTitle[i]);
            rb.setLayoutParams(new ViewGroup.LayoutParams(indicatorWidth,ViewGroup.LayoutParams.MATCH_PARENT));
            mRadioGroup.addView(rb);
        }
    }

    private void initViews() {
        mViewPager= (ViewPager) findViewById(R.id.event_content_viewpager);
        mRadioGroup= (RadioGroup) findViewById(R.id.event_day_radiogroup);
        mRelativeLayout= (RelativeLayout) findViewById(R.id.event_day_relativeLayout);
        mEventSyncHorizontalScrollView= (EventSyncHorizontalScrollView) findViewById(R.id.horizontalScrollView);
        mImageViewIndicator= (ImageView) findViewById(R.id.event_day_indicator);
        mImageViewScrollLeft= (ImageView) findViewById(R.id.event_day_scroll_left);
        mImageViewScrollRight= (ImageView) findViewById(R.id.event_day_scroll_right);
    }

    public void Event_back(View view) {
        finish();
    }
}
