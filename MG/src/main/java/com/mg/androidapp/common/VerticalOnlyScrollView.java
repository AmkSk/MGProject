package com.mg.androidapp.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Custom ScrollView, used in BuildingActivity because of Google Maps Fragment
 * (Without this the user would not be able to scroll the scrollview with the map)
 */
public class VerticalOnlyScrollView extends ScrollView {
    private Context context;
    private float xDistance, yDistance, lastX, lastY;

    public VerticalOnlyScrollView(Context context) {
        super(context);
        this.context = context;
    }

    public VerticalOnlyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalOnlyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - lastX);
                yDistance += Math.abs(curY - lastY);
                lastX = curX;
                lastY = curY;
                if(xDistance > yDistance)
                    return false;
        }

        return super.onInterceptTouchEvent(ev);
    }
}
