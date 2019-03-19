package com.fy.img.picker.bean;

import java.io.Serializable;

/**
 * 图片信息
 * Created by fangs on 2017/6/29.
 */
public class ImageItem implements Serializable {

    public String name;       //图片的名字
    public String path;       //图片的路径
    public long size = 0;     //图片的大小
    public int width;         //图片的宽度
    public int height;        //图片的高度
    public String mimeType;   //图片的类型
    public long addTime;      //图片的创建时间
    public boolean isSelect;  //是否选中

    public int isShowCamera = 1;   //是否显示拍照按钮 1：表示不显示；0：显示

    public ImageItem() {
    }

    public ImageItem(int isShowCamera) {
        this.isShowCamera = isShowCamera;
    }

    public ImageItem(String path, boolean isSelect) {
        this.path = path;
        this.isSelect = isSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public int isShowCamera() {
        return isShowCamera;
    }

    public void setShowCamera(int showCamera) {
        isShowCamera = showCamera;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getIsShowCamera() {
        return isShowCamera;
    }

    public void setIsShowCamera(int isShowCamera) {
        this.isShowCamera = isShowCamera;
    }

    /**
     * 图片的路径和创建时间相同就认为是同一张图片
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof ImageItem) {
            ImageItem item = (ImageItem) o;
            return this.path.equalsIgnoreCase(item.path) && this.addTime == item.addTime;
        }

        return super.equals(o);
    }
}
