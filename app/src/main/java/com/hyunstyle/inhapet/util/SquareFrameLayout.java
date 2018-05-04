package com.hyunstyle.inhapet.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by sh on 2018-05-04.
 */

public class SquareFrameLayout extends FrameLayout {
    public SquareFrameLayout(Context context) {
        super(context);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(final int width, final int height) {
        super.onMeasure(width, height);
        setMeasuredDimension(width, width); // 가로에 맞춰서 정사각형으로
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, w, oldw, oldh);
    }
}
