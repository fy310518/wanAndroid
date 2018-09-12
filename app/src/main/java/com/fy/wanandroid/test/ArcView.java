package com.fy.wanandroid.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.fy.baselibrary.utils.DensityUtils;
import com.fy.wanandroid.R;

/**
 * 不要在 View 的 onMeasure,onLayout,onDraw方法内部做任何的构建对象的操作，因为它们可能执行多次，影响程序的性能
 * Created by fangs on 2018/9/11 11:25.
 */
public class ArcView extends View {

    private int mWidth;
    private int mHeight;

    private int circleR;//大圆半径
    private int circleX;//大圆圆心 x坐标
    private int circleY;//大圆圆心 y坐标

    private int smailCircleR;//小圆半径
    private int centerCircleX; //中间小圆 圆心 x坐标
    private int centerCircleY; //中间小圆 圆心 y坐标


    /** 背景颜色 */
    private int mBgColor;

    private RectF mRect;
    private Bitmap mBitmap;
    private Paint mPaint;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
//        smailCircleR = (mWidth - arcMaginLeft - arcMarginRight - 2 * arcMargin) / 6;

        smailCircleR = DensityUtils.dp2px(45);

        circleR = mWidth;
        circleX = mWidth / 2;
        circleY = mHeight - smailCircleR - circleR;

        centerCircleX = circleX;
        centerCircleY = mHeight - smailCircleR;

        mRect = new RectF(0, 0, mWidth, centerCircleY);
    }

    public ArcView(Context context) {
        this(context, null);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcView);
        mBgColor = typedArray.getColor(R.styleable.ArcView_bgColor, Color.parseColor("#303F9F"));

        mBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.diet_bg);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        //新建Canvas层级
        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawCircle(circleX, circleY, circleR, mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mBitmap, null, mRect, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);


//        setLayerType(LAYER_TYPE_SOFTWARE, null);//对单独的View在运行时阶段禁用硬件加速
        mPaint.setShadowLayer(2, 2, 2, Color.DKGRAY);
        mPaint.setColor(mBgColor);
        canvas.drawCircle(centerCircleX, centerCircleY, smailCircleR, mPaint);

        int leftCircleX = (int) (circleX - circleR * Math.sin(Math.toRadians(20)));
        int leftCircleY = (int) (centerCircleY - (circleR - circleR * Math.cos(Math.toRadians(20))));
        canvas.drawCircle(leftCircleX, leftCircleY, smailCircleR - 20, mPaint);

        int rightCircleX = (int) (circleX + circleR * Math.sin(Math.toRadians(20)));
        int rightCircleY = leftCircleY;
        canvas.drawCircle(rightCircleX, rightCircleY, smailCircleR - 10, mPaint);


        mPaint.setShadowLayer(0, 2, 2, Color.DKGRAY);
        mPaint.setTextSize(DensityUtils.sp2px(22));
        mPaint.setColor(Color.parseColor("#f39b39"));
        mPaint.setTextAlign(Paint.Align.CENTER);

//        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
//        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
//        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
//        int baseLineY = (int) (centerCircleY - top/2 - bottom/2);//基线中间点的y轴计算公式
        canvas.drawText("92", centerCircleX - DensityUtils.dp2px(5), centerCircleY, mPaint);
        canvas.drawText("67", leftCircleX - 15, leftCircleY, mPaint);
        canvas.drawText("87", rightCircleX - 15, rightCircleY, mPaint);

        mPaint.setTextSize(DensityUtils.sp2px(14));
        mPaint.setColor(Color.parseColor("#ccf39b39"));
        canvas.drawText("分", centerCircleX + DensityUtils.dp2px(15), centerCircleY, mPaint);
        canvas.drawText("分", leftCircleX + 45, leftCircleY, mPaint);
        canvas.drawText("分", rightCircleX + 45, rightCircleY, mPaint);

        mPaint.setColor(Color.parseColor("#666666"));
        canvas.drawText("总分", centerCircleX + DensityUtils.dp2px(2), centerCircleY + DensityUtils.dp2px(20), mPaint);
        canvas.drawText("生理", leftCircleX + 5, leftCircleY + 50, mPaint);
        canvas.drawText("心理", rightCircleX + 5, rightCircleY + 50, mPaint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        }
        setMeasuredDimension(mWidth, mHeight);
    }



}
