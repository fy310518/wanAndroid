package com.gcstorage.parkinggather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.fy.baselibrary.utils.DensityUtils;
import com.gcstorage.parkinggather.bean.DataEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/5/31.
 */

public class BarGraphView extends View {

    private static final float DEFAULT_AXIS_TEXT_SIZE = 12;
    private static final float DEFAULT_VALUE_TEXT_SIZE = 12;
    private static final float DEFAULT_MEAN_TEXT_SIZE = 16;
    private Paint axisLinePaint; //绘制xy轴线的画笔
    private Paint axisTxtPaint; //文本画笔
    private Paint rectPaint; //柱状图画笔
    private Paint valueTxtPaint;  //数据文本画笔
    private Paint meanTxtPaint;   //含义文字画笔

    private List<DataEntry> entryList; //图表数据集合
    private String xAxisMean = "x轴"; //x轴名称

    private String yAxisMean = "y轴"; //y轴名称

    public static final String NUMBER_TYPE_FLOAT = "float"; //数字类型
    public static final String NUMBER_TYPE_INT = "int";

    private String type;
    //轴文字属性
    private float axisTextSize = DensityUtils.dp2px(DEFAULT_AXIS_TEXT_SIZE);
    private int axisTextColor = Color.BLACK;
    //数据文字属性
    private float valueTextSize = DensityUtils.dp2px(DEFAULT_VALUE_TEXT_SIZE);
    private int axisValueColor = Color.BLACK;
    //含义文字属性
    private float meanTextSize = DensityUtils.dp2px(DEFAULT_MEAN_TEXT_SIZE);

    private int axisMeanColor = Color.BLACK;
    //柱状颜色
    private int rectColor = Color.GRAY;


    public BarGraphView(Context context) {
        this(context, null);
    }

    public BarGraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化每个画笔
     */
    private void init() {
        axisLinePaint = new Paint();
        axisTxtPaint = new Paint();
        valueTxtPaint = new Paint();
        meanTxtPaint = new Paint();
        rectPaint = new Paint();

        axisTxtPaint.setAntiAlias(true);
        valueTxtPaint.setAntiAlias(true);
        meanTxtPaint.setAntiAlias(true);
        axisLinePaint.setAntiAlias(true);

        axisLinePaint.setColor(Color.GRAY);
        axisTxtPaint.setColor(axisTextColor);
        valueTxtPaint.setColor(axisValueColor);
        meanTxtPaint.setColor(axisMeanColor);
        rectPaint.setColor(rectColor);

        axisTxtPaint.setTextSize(axisTextSize);
        valueTxtPaint.setTextSize(valueTextSize);
        meanTxtPaint.setTextSize(meanTextSize);

        axisTxtPaint.setTextAlign(Paint.Align.CENTER);
        valueTxtPaint.setTextAlign(Paint.Align.CENTER);
        meanTxtPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 设置图标数据
     * @param entryList
     */
    public void setAxisValue(List<DataEntry> entryList) {
        this.entryList = entryList;
        postInvalidate();
    }

    /**
     * 设置x轴代表的含义
     * @param xAxisMean
     */
    public void setxAxisMean(String xAxisMean) {
        this.xAxisMean = xAxisMean;
    }

    /**
     * 设置y轴的含义
     * @param yAxisMean
     */
    public void setyAxisMean(String yAxisMean) {
        this.yAxisMean = yAxisMean;
    }

    /**
     * 设置含义文字颜色
     * @param axisTextColor
     */
    public void setAxisTextColor(int axisTextColor) {
        this.axisTextColor = axisTextColor;
        axisTxtPaint.setColor(this.axisTextColor);
    }

    /**
     * s设置显示样式
     * @param type type类型为NUMBER_TYPE_INT或者NUMBER_TYPE_FLOAT
     */
    public void setType(String type){
        if(TextUtils.isEmpty(type)){
            this.type = NUMBER_TYPE_INT;
        }else{
            this.type = type;
        }
    }

    /**
     * 设置显示在数据上面的数据颜色
     * @param axisValueColor
     */
    public void setAxisValueColor(int axisValueColor) {
        this.axisValueColor = axisValueColor;
        valueTxtPaint.setColor(this.axisValueColor);
    }

    /**
     * 内容颜色
     * @param axisMeanColor
     */
    public void setAxisMeanColor(int axisMeanColor) {
        this.axisMeanColor = axisMeanColor;
        meanTxtPaint.setColor(this.axisMeanColor);
    }

    /**
     * 设置柱子的颜色
     * @param rectColor
     */
    public void setRectColor(int rectColor) {
        this.rectColor = rectColor;
        rectPaint.setColor(this.rectColor);
    }

    public void setMeanTextSize(float meanTextSize) {
        this.meanTextSize = meanTextSize;
        meanTxtPaint.setTextSize(this.meanTextSize);
    }

    public void setValueTextSize(float valueTextSize) {
        this.valueTextSize = valueTextSize;
        valueTxtPaint.setTextSize(this.valueTextSize);
    }

    public void setAxisTextSize(float axisTextSize) {
        this.axisTextSize = axisTextSize;
        axisTxtPaint.setTextSize(this.axisTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        //绘制横竖轴
        axisLinePaint.setStrokeWidth(2);
        //x轴
        canvas.drawLine(axisTextSize * 2 + 10 ,
                height - axisTextSize - 20,
                width - meanTextSize * 2 - 10, height - axisTextSize - 20, axisLinePaint);
        Path path1 = new Path();
        path1.moveTo(width - meanTextSize * 2 - 20, height - axisTextSize - 25);
        path1.lineTo(width - meanTextSize * 2 - 10, height - axisTextSize - 20);
        path1.lineTo(width - meanTextSize * 2 - 20, height - axisTextSize - 15);
        canvas.drawPath(path1,axisLinePaint);
        //y轴
        canvas.drawLine(axisTextSize * 2 + 10,
                meanTextSize + 10,
                axisTextSize * 2 + 10, height - axisTextSize - 20, axisLinePaint);
        Path path2 = new Path();
        path2.moveTo(axisTextSize * 2 + 5, meanTextSize + 20);
        path2.lineTo(axisTextSize * 2 + 10, meanTextSize + 10);
        path2.lineTo(axisTextSize * 2 + 15, meanTextSize + 20);
        canvas.drawPath(path2,axisLinePaint);
        //绘制xy轴含义文字
        //x轴含义文字
        canvas.drawText(xAxisMean, width - meanTextSize, height - axisTextSize - 5 ,meanTxtPaint);
        //y轴含义文字
        canvas.drawText(yAxisMean, axisTextSize * 2 + 10, meanTextSize, meanTxtPaint);
        //绘制柱状图
        int yAxisLength = height - 50 - 75;
        if(entryList != null && entryList.size() > 0){
            int xAxisLength = width - 100 - 50;
            int eachSpace = xAxisLength / (entryList.size() + 1);
            List<DataEntry> tmp = new ArrayList<>();
            tmp.addAll(entryList);
            Collections.sort(tmp);
            double maxValue = tmp.get(0).getyAxisValue();
            double maxY = maxValue + (NUMBER_TYPE_FLOAT.equals(type) ? 1 : 10);
            for (int i = 0; i < entryList.size(); i++) {
                //绘制x轴上的文字
                canvas.drawText(entryList.get(i).getxAxisValue(),
                        axisTextSize * 2 + 10 + eachSpace * (i +1),
                        height - axisTxtPaint.getTextSize() / 2 + 10, axisTxtPaint);
                Rect rect = new Rect();
                rect.left = (int)(axisTextSize * 2 + 10 + eachSpace * (i + 1) - 10);
                rect.right = (int)(axisTextSize * 2 + 10 + eachSpace * (i + 1) + 10);

                rect.top = (int)(height - entryList.get(i).getyAxisValue() * yAxisLength / maxY - axisTextSize * 2 - 10);
                rect.bottom = (int)(height - axisTextSize - 20 - 2);

                canvas.drawRect(rect, rectPaint);

                //绘制每个柱状图数量文字
                canvas.drawText(changeValue(entryList.get(i).getyAxisValue()),
                        axisTextSize * 2 + 10 + eachSpace * (i +1),
                        rect.top - DensityUtils.dp2px(5), valueTxtPaint);
            }
        }
    }

    private String changeValue(double value) {
        if(TextUtils.isEmpty(type)){
            type = NUMBER_TYPE_INT;
        }
        switch (type){
            case NUMBER_TYPE_FLOAT:
                return String.valueOf(value);
            case NUMBER_TYPE_INT:
                return String.valueOf((int)value);
        }
        return "0";
    }
}
