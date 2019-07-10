package com.gcstorage.map.shake.maputils;


import com.ishowmap.api.location.IMLocation;

/**
 * 立得地图的接口 2019/1/24 0024.
 */

public interface OnLocationListener {
    /**
     * 根据地址换算坐标
     *
     * @param entity
     */
    void onLocationGet(PositionEntity entity);

    /**
     * 获取定位失败的回调
     *
     * @param aMapLocation
     */
    void onError(IMLocation aMapLocation);
}