package com.gcstorage.map.shake.maputils;


/**
 * Function: 逆地理编码或者定位完成后回调接口<br/>
 */
public interface OnLocationGetListener {


    /**
     * 根据地址换算坐标
     * @param entity
     */
    void onLocationGet(PositionEntity entity);

    /**
     * 根据坐标变成地址
     * @param entity
     */
    void onRegecodeGet(PositionEntity entity);
}
