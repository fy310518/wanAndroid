package com.fy.wanandroid.entity;

import java.io.Serializable;

/**
 * 热词 or 常用网站实体类
 * Created by fangs on 2018/4/11.
 */
public class Bookmark implements Serializable {

    /**
     * item type
     */
    private int itemType;

    /**
     * icon :
     * id : 2
     * link : http://blog.csdn.net/guolin_blog
     * name : 郭霖的博客
     * order : 3
     * visible : 1
     */

    private String icon = "";
    private int id;
    private String link = "";
    private String name = "";
    private int order;
    private int visible;

    public Bookmark(String name, int itemType) {
        this.name = name;
        this.itemType = itemType;
    }

    public Bookmark(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public int getItemType() {
        return itemType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "icon='" + icon + '\'' +
                ", id=" + id +
                ", link='" + link + '\'' +
                ", name='" + name + '\'' +
                ", order=" + order +
                ", visible=" + visible +
                '}';
    }
}
