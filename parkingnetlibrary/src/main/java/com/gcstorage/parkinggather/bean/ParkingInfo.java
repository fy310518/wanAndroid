package com.gcstorage.parkinggather.bean;

import java.util.List;

/**
 * DESCRIPTION： 驻车列表 实体类
 * Created by fangs on 2019/7/10 18:02.
 */
public class ParkingInfo {

    private List<ParkingInfoEntity.DataBean> imageInfoList;

    public List<ParkingInfoEntity.DataBean> getData() {
        return imageInfoList;
    }

    public void setData(List<ParkingInfoEntity.DataBean> data) {
        this.imageInfoList = data;
    }
}
