package com.fy.wanandroid.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * describe： todo 描述</br>
 * Created by fangs on 2018/12/26 17:11.
 */
public class RingView extends View {

    private int mWidth;
    private int mHeight;

    private Paint mPaint;

    public RingView(Context context) {
        super(context);
    }

    public RingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int min = Math.min(mWidth, mHeight);
        int circleR = min / 2 - 10;

        mPaint.setStyle(Paint.Style.STROKE);  //设置画笔模式为填充
        mPaint.setStrokeWidth(min / 20);           //设置画笔宽度为10px
        mPaint.setColor(Color.parseColor("#aaaccc"));

        canvas.drawCircle(mWidth / 2, mHeight / 2, circleR, mPaint);
    }
}
