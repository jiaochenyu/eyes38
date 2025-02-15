package com.example.eyes38.utils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eyes38.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by jqchen on 2016/5/26.
 */
public class LoadMoreFooterView extends FrameLayout implements PtrUIHandler {

    // 下拉刷新文字
    private TextView tvHeadTitle;

    // 下拉图标
    private ImageView ivWindmill;
    private int mLastMotionY;
    private int mLastMotionX;

    public LoadMoreFooterView(Context context) {
        this(context, null);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        /**
         * 头部
         */
        ViewGroup headView = (ViewGroup) inflater.inflate(R.layout.widget_header, this, true);
        ivWindmill = (ImageView) headView.findViewById(R.id.iv_windmill);
        tvHeadTitle = (TextView) headView.findViewById(R.id.tv_head_title);
        //设置动画
        ivWindmill.setBackgroundResource(R.drawable.anim);
        final AnimationDrawable animDrawable = (AnimationDrawable) ivWindmill
                .getBackground();
        ivWindmill.post(new Runnable() {
            @Override
            public void run() {
                animDrawable.start();
            }
        });
        ivWindmill.setVisibility(VISIBLE);
        //ivWindmill.setImageResource(R.mipmap.ic_launcher);
        tvHeadTitle.setText("下拉刷新");
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int y = (int) e.getRawY();
        int x = (int) e.getRawX();
        boolean resume = false;
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 首先拦截down事件,记录y坐标
                mLastMotionY = y;
                mLastMotionX = x;
                resume = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // deltaY > 0 是向下运动,< 0是向上运动
                int deltaY = y - mLastMotionY;
                int deleaX = x - mLastMotionX;

                if (Math.abs(deleaX) > Math.abs(deltaY)) {
                    resume = false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        tvHeadTitle.setText("下拉刷新");
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        tvHeadTitle.setText("下拉刷新");
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        tvHeadTitle.setText("正在刷新");
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        ivWindmill.clearAnimation();
        tvHeadTitle.setText("刷新完成");
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                tvHeadTitle.setText("下拉刷新");
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                tvHeadTitle.setText("松开刷新");
            }
        }
    }

}
