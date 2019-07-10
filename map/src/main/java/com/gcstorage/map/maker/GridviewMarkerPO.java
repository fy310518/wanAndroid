package com.gcstorage.map.maker;

import java.io.Serializable;

public class GridviewMarkerPO implements Serializable {


    public String mType;
    public String cameraDireation;//摄像头方向
    public String markerTitle;
    public int iconRes;
    public int iconPressedRes;
    public int iconNormalRes;
    public boolean isSelect;
    public boolean isCamera = false;
    public boolean isMiddle = false;

    public GridviewMarkerPO() {
    }

    public GridviewMarkerPO(String mType, String markerTitle, int iconPressedRes, int iconNormalRes) {
        this.mType = mType;
        this.markerTitle = markerTitle;
        this.iconPressedRes = iconPressedRes;
        this.iconNormalRes = iconNormalRes;
    }
    public GridviewMarkerPO(String mType,String cameraDireation, String markerTitle, int iconPressedRes, int iconNormalRes) {
        this.mType = mType;
        this.markerTitle = markerTitle;
        this.iconPressedRes = iconPressedRes;
        this.iconNormalRes = iconNormalRes;
    }

    @Override
    public String toString() {
        return "GridviewMarkerPO{" +
                "mType='" + mType + '\'' +
                ", markerTitle='" + markerTitle + '\'' +
                ", iconRes=" + iconRes +
                ", iconPressedRes=" + iconPressedRes +
                ", iconNormalRes=" + iconNormalRes +
                '}';
    }
}
