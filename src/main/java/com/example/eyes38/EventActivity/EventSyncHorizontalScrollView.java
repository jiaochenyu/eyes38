package com.example.eyes38.EventActivity;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

/**
 * Created by weixiao on 2016/5/24.
 */
public class EventSyncHorizontalScrollView extends HorizontalScrollView {
    private View view;
    private ImageView leftImage;
    private ImageView rightImage;
    private int windowWitdh=0;
    private Activity mContext;

   public void setSomeParam(View view,ImageView leftImage,ImageView rightImage,Activity context){
       this.mContext=context;
       this.view=view;
       this.leftImage=leftImage;
       this.rightImage=rightImage;
        //获取当前屏幕的大小
       DisplayMetrics dm=new DisplayMetrics();
       this.mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
       windowWitdh=dm.widthPixels;

   }
    public EventSyncHorizontalScrollView(Context context) {
        super(context);
    }

    public EventSyncHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     *
     * @param l X
     * @param t  Y
     * @param oldl
     * @param oldt
     */
    protected void onScrollChanged(int l,int t, int oldl, int oldt){
        if (!mContext.isFinishing()&&view!=null&&leftImage!=null&&rightImage!=null){
            if (view.getWidth()<=windowWitdh){
                leftImage.setVisibility(View.GONE);
                rightImage.setVisibility(View.GONE);
            }else {
                if (l==0){
                    leftImage.setVisibility(View.GONE);
                    rightImage.setVisibility(View.VISIBLE);
                }else if (view.getWidth()-1==windowWitdh){
                    leftImage.setVisibility(View.VISIBLE);
                    rightImage.setVisibility(View.VISIBLE);
                }else if ((l+windowWitdh)==view.getWidth()){
                    //到了最右面
                    leftImage.setVisibility(View.VISIBLE);
                    rightImage.setVisibility(View.GONE);
                }
                else {
                    leftImage.setVisibility(View.VISIBLE);
                    rightImage.setVisibility(View.VISIBLE);
                }
            }
        }

    }
}
