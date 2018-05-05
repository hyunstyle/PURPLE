package com.hyunstyle.inhapet.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by SangHyeon on 2018-05-05.
 */

public class DynamicLoadingRecyclerView extends RecyclerView {

    public interface OnEndScrollListener {
        void onEndScroll();
    }

    private boolean isFling;
    private OnEndScrollListener onEndScrollListener;

    public DynamicLoadingRecyclerView(Context context) {
        super(context);
    }

    public DynamicLoadingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicLoadingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        isFling = true;
        return super.fling(velocityX, velocityY);
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
