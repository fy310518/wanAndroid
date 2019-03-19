package com.fy.img.picker;

/**
 * 图片选择器 常量
 */
public class ImagePicker {

    /**
     * 选择的图片文件夹 key
     */
    public static final String imgFolderkey = "ImageFolder";
    /**
     * 图片选择 请求码
     */
    public static final int Picture_Selection = 10001;
    /**
     * 图片预览 请求码
     */
    public static final int Picture_Preview = 10002;
    /**
     * 拍照 请求码
     */
    public static final int Photograph = 10003;
    /**
     * 图片裁剪 请求码
     */
    public static final int KJNova_Clipper = 10005;
    /**
     * 手机号码 请求码
     */
    public static final int Phone_Number = 11001;

    /**
     * 生成 文件路径（比如拍照生成的文件）
     */
    public static String newFilePath = "";

    private int selectLimit;

    public int getSelectLimit() {
        return selectLimit;
    }

    public void setSelectLimit(int selectLimit) {
        this.selectLimit = selectLimit;
    }

}