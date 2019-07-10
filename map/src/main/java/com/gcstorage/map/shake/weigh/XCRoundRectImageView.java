package com.gcstorage.map.shake.weigh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 自定义的圆角矩形ImageView，可以直接当组件在布局中使用。
 * Created by 康伟
 * on 2016/6/2.
 */
public class XCRoundRectImageView extends ImageView {

    public XCRoundRectImageView(Context context) {
        super(context);
        init();
    }

    public XCRoundRectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XCRoundRectImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private final RectF roundRect = new RectF();//圆角矩形

    private float rect_adius = 5;//角度

    private final Paint maskPaint = new Paint();


    private final Paint zonePaint = new Paint();



    private void init() {

        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //
        zonePaint.setAntiAlias(true);
        zonePaint.setColor(Color.WHITE);
        //
        float density = getResources().getDisplayMetrics().density;
        rect_adius = rect_adius * density;
    }



    public void setRectAdius(float adius) {
        rect_adius = adius;
        invalidate();

    }



    @Override

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = getWidth();
        int h = getHeight();
//        roundRect.set(0, 0, w, h + rect_adius);
//        roundRect.set(0, rect_adius, w, h ); 仅下边有圆角
        roundRect.set(0, 0, w, h ); //全圆角
//        roundRect.set(0, 0, w + rect_adius, h + rect_adius); //仅 是圆角
    }



    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
        //单独分配了一个画布用于绘制图层 它定义了一个画布区域（可设置透明度），此方法之后的所有绘制都在此区域中绘制，直到调用canvas.restore()方法
        canvas.drawRoundRect(roundRect, rect_adius, rect_adius, zonePaint);
        //
        canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();

    }



}
