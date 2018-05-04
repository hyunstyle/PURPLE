package com.hyunstyle.inhapet.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by sh on 2018-05-05.
 */

public class DynamicLoadingGridView extends GridView {

    public interface OnEndScrollListener {
        void onEndScroll();
    }

    private boolean isFling;
    private OnEndScrollListener onEndScrollListener;

    public DynamicLoadingGridView(Context context) {
        super(context);
    }

    public DynamicLoadingGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicLoadingGridView(Context context, AttributeSet attrs, int defStyleAttr) {
       super(context, attrs, defStyleAttr);
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);
        isFling = true;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (!isFling) {
            if (Math.abs(y - oldY) < 2 || y >= getMeasuredHeight() || y == 0) {
                if (onEndScrollListener != null) {
                    onEndScrollListener.onEndScroll();
                }
                isFling = false;
            }
        }
    }

    public void setOnEndScrollListener(OnEndScrollListener onEndScrollListener) {
        this.onEndScrollListener = onEndScrollListener;
    }
}
