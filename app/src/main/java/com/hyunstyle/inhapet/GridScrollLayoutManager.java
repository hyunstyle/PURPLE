package com.hyunstyle.inhapet;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by SangHyeon on 2018-04-22.
 */

public class GridScrollLayoutManager extends GridLayoutManager {

    private static final float MILLISECONDS_PER_INCH = 5f; //default is 25f (bigger = slower)

    public GridScrollLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public GridScrollLayoutManager(Context context, int spanCount,
                             @RecyclerView.Orientation int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public GridScrollLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int findFirstVisibleItemPosition() {
        return super.findFirstVisibleItemPosition();
    }

    @Override
    public int computeVerticalScrollOffset(RecyclerView.State state) {

        Log.e("vertical", "" + state.getTargetScrollPosition());
        return super.computeVerticalScrollOffset(state);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {

        final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return super.computeScrollVectorForPosition(targetPosition*2);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        };

        linearSmoothScroller.setTargetPosition(position*2);
        startSmoothScroll(linearSmoothScroller);
    }
}
