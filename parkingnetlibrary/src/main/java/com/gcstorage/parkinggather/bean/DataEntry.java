package com.gcstorage.parkinggather.bean;

/**
 * 图表中单个数据
 * Created by 郑家双 on 2017/5/31.
 */
public class DataEntry implements Comparable<DataEntry> {

    private String xAxisValue;
    private double yAxisValue;

    public DataEntry(String xAxisValue, double yAxisValue) {
        this.xAxisValue = xAxisValue;
        this.yAxisValue = yAxisValue;
    }

    public String getxAxisValue() {
        return xAxisValue;
    }

    public double getyAxisValue() {
        return yAxisValue;
    }

    @Override
    public int compareTo(DataEntry another) {
        return this.yAxisValue > another.getyAxisValue() ? -1 : 1;
    }
}
