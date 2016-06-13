package com.example.eyes38.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by JCY on 2016/6/11.
 */
public class EventSpacesItemDecoration extends RecyclerView.ItemDecoration {
    //Event 瀑布流的 间隔
    private int space;

    public EventSpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left=space;
        outRect.right=space;
        outRect.bottom=space;
        if (parent.getChildAdapterPosition(view) == 0){
            outRect.top =space;
        }
    }
}
