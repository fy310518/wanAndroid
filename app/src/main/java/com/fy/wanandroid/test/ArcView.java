package com.fy.wanandroid.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.fy.baselibrary.utils.DensityUtils;
import com.fy.wanandroid.R;

/**
 * 不要在 View 的 onMeasure,onLayout,onDraw方法内部做任何的构建对象的操作，因为它们可能执行多次，影响程序的性能
 * Created by fangs on 2018/9/11 11:25.
 */
public class ArcView extends View {
    Context context;
    private RectF mRect;
    private Bitmap mBitmap;
    private LinearGradient mLinearGradient;
    private Paint mPaint;

    private int mWidth;
    private int mHeight;
    int shadow;//小圆 阴影偏移量

    private int circleR;//大圆半径
    private int circleX;//大圆圆心 x坐标
    private int circleY;//大圆圆心 y坐标

    private int smailCircleR;  //中间小圆 半径
    private int centerCircleX; //中间小圆 圆心 x坐标
    private int centerCircleY; //中间小圆 圆心 y坐标

    /**
     * 背景颜色
     */
    private int arcBackground;
    private int mBgColor;

    private int numColor;
    private int unitColor;
    private int itemColor;


    private String leftCircleNum;
    private String leftCircleUnit;
    private String leftCircleItem;

    private String centerCircleNum;
    private String centerCircleUnit;
    private String centerCircleItem;

    private String rightCircleNum;
    private String rightCircleUnit;
    private String rightCircleItem;


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        smailCircleR = DensityUtils.dp2px(45);
        shadow = DensityUtils.dp2px(2);

        circleR = mWidth;
        circleX = mWidth / 2;
        circleY = mHeight - smailCircleR - shadow - circleR;

        centerCircleX = circleX;
        centerCircleY = mHeight - smailCircleR - shadow;

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

        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcView);
        mBgColor = typedArray.getColor(R.styleable.ArcView_bgColor, Color.parseColor("#cccccc"));

        numColor = typedArray.getColor(R.styleable.ArcView_numColor, Color.parseColor("#303F9F"));
        unitColor = typedArray.getColor(R.styleable.ArcView_unitColor, Color.parseColor("#303F9F"));
        itemColor = typedArray.getColor(R.styleable.ArcView_itemColor, Color.parseColor("#303F9F"));

        leftCircleNum = typedArray.getString(R.styleable.ArcView_leftCircleNum);
        leftCircleUnit = typedArray.getString(R.styleable.ArcView_leftCircleUnit);
        leftCircleItem = typedArray.getString(R.styleable.ArcView_leftCircleItem);

        centerCircleNum = typedArray.getString(R.styleable.ArcView_centerCircleNum);
        centerCircleUnit = typedArray.getString(R.styleable.ArcView_centerCircleUnit);
        centerCircleItem = typedArray.getString(R.styleable.ArcView_centerCircleItem);

        rightCircleNum = typedArray.getString(R.styleable.ArcView_rightCircleNum);
        rightCircleUnit = typedArray.getString(R.styleable.ArcView_rightCircleUnit);
        rightCircleItem = typedArray.getString(R.styleable.ArcView_rightCircleItem);

        arcBackground = typedArray.getResourceId(R.styleable.ArcView_arcBackground, -1);

        if (arcBackground != -1) {
            mBitmap = BitmapFactory.decodeResource(getResources(), arcBackground);
        } else {
            mLinearGradient = new LinearGradient(mWidth / 2, 0, mWidth / 2, mHeight,
                    Color.parseColor("#303F9F"), Color.parseColor("#303F9F"), Shader.TileMode.MIRROR);
        }
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
        if (null != mBitmap){
            canvas.drawBitmap(mBitmap, null, mRect, mPaint);
        } else {
            mPaint.setShader(mLinearGradient);//绘制渐变色
            canvas.drawRect(mRect, mPaint);
            mPaint.setShader(null);
        }

        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);


//        画小圆
        setLayerType(LAYER_TYPE_SOFTWARE, null);//对单独的View在运行时阶段禁用硬件加速
        mPaint.setShadowLayer(3, 0, shadow / 2, Color.parseColor("#FF4081"));
        mPaint.setColor(mBgColor);
        canvas.drawCircle(centerCircleX, centerCircleY, smailCircleR, mPaint);

        int leftCircleX = (int) (circleX - circleR * Math.sin(Math.toRadians(20)));
        int leftCircleY = (int) (centerCircleY - (circleR - circleR * Math.cos(Math.toRadians(20))));
        canvas.drawCircle(leftCircleX, leftCircleY, smailCircleR - 20, mPaint);

        int rightCircleX = (int) (circleX + circleR * Math.sin(Math.toRadians(20)));
        int rightCircleY = leftCircleY;
        canvas.drawCircle(rightCircleX, rightCircleY, smailCircleR - 20, mPaint);
        mPaint.clearShadowLayer();


        //绘制圆上的 文本
        mPaint.setTextAlign(Paint.Align.CENTER);

        mPaint.setColor(numColor);
        mPaint.setTextSize(DensityUtils.sp2px(22));
        int left = DensityUtils.dp2px(5);
        if (!TextUtils.isEmpty(centerCircleNum))
            canvas.drawText(centerCircleNum, centerCircleX - left, centerCircleY, mPaint);
        if (!TextUtils.isEmpty(leftCircleNum))
            canvas.drawText(leftCircleNum, leftCircleX - left, leftCircleY, mPaint);
        if (!TextUtils.isEmpty(rightCircleNum))
            canvas.drawText(rightCircleNum, rightCircleX - left, rightCircleY, mPaint);

        mPaint.setTextSize(DensityUtils.sp2px(14));
        mPaint.setColor(unitColor);
        int right = DensityUtils.dp2px(15);
        if (!TextUtils.isEmpty(centerCircleUnit))
            canvas.drawText(centerCircleUnit, centerCircleX + right, centerCircleY, mPaint);
        if (!TextUtils.isEmpty(leftCircleUnit))
            canvas.drawText(leftCircleUnit, leftCircleX + right, leftCircleY, mPaint);
        if (!TextUtils.isEmpty(rightCircleUnit))
            canvas.drawText(rightCircleUnit, rightCircleX + right, rightCircleY, mPaint);

        mPaint.setColor(itemColor);
        int bottom = DensityUtils.dp2px(2);
        int bottomY = DensityUtils.dp2px(20);
        if (!TextUtils.isEmpty(centerCircleItem))
            canvas.drawText(centerCircleItem, centerCircleX + bottom, centerCircleY + bottomY, mPaint);
        if (!TextUtils.isEmpty(leftCircleItem))
            canvas.drawText(leftCircleItem, leftCircleX + bottom, leftCircleY + bottomY, mPaint);
        if (!TextUtils.isEmpty(rightCircleItem))
            canvas.drawText(rightCircleItem, rightCircleX + bottom, rightCircleY + bottomY, mPaint);
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


    //绘制左边圆上的 文本
    public void setLeftText(String leftNum, String leftUnit, String leftItem) {
        leftCircleNum = leftNum;
        leftCircleUnit = leftUnit;
        leftCircleItem = leftItem;

        invalidate();
    }

    //绘制中间圆上的 文本
    public void setCenterText(String centerNum, String centerUnit, String centerItem) {
        centerCircleNum = centerNum;
        centerCircleUnit = centerUnit;
        centerCircleItem = centerItem;

        invalidate();
    }

    //绘制右边圆上的 文本
    public void setRightText(String rightNum, String rightUnit, String rightItem) {
        rightCircleNum = rightNum;
        rightCircleUnit = rightUnit;
        rightCircleItem = rightItem;

        invalidate();
    }
}
