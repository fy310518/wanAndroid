package com.fy.img.picker.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 图片文件夹
 * Created by fangs on 2017/6/29.
 */
public class ImageFolder implements Serializable {

    public String name;  //当前文件夹的名字
    public String path;  //当前文件夹的路径
    public ImageItem cover;   //当前文件夹需要要显示的缩略图，默认为最近的一次图片
    public List<ImageItem> images;  //当前文件夹下所有图片的集合

    public ImageFolder() {
    }

    public ImageFolder(List<ImageItem> images) {
        this.images = images;
    }

    /**
     * 只要文件夹的路径和名字相同，就认为是相同的文件夹
     */
    @Override
    public boolean equals(Object o) {
        try {
            ImageFolder other = (ImageFolder) o;
            return this.path.equalsIgnoreCase(other.path) && this.name.equalsIgnoreCase(other.name);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
