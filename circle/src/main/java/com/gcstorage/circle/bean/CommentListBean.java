package com.gcstorage.circle.bean;

import android.os.Parcel;

import java.io.Serializable;
import java.util.Objects;

/**
 * DESCRIPTION： 帖子 评论 实体类
 * Created by fangs on 2019/3/19 15:08.
 */
public class CommentListBean implements Serializable {

    /**
     * publish_alarm : 发布评论的人的警号
     * publish_name : 发布评论的人的姓名
     * reply_alarm : 回复人的警号
     * reply_name : 回复人的姓名
     * post_id : 帖子的ID
     * comment_id : 评论的ID
     * content : 评论内容
     * picture : 评论的图片
     * type : 帖子的类型
     * visible_alarm : 评论可见的人的警号（逗号隔开）
     */

    private String publish_alarm = "";
    private String publish_name= "";
    private String reply_alarm= "";
    private String reply_name= "";
    private String post_id= "";
    private String comment_id= "";
    private String content= "";
    private String picture= "";
    private String type= "";
    private String is_visible= "";
    private String is_accept_answer= "";

    public String getIs_accept_answer() {
        return is_accept_answer;
    }

    public void setIs_accept_answer(String is_accept_answer) {
        this.is_accept_answer = is_accept_answer;
    }

    public String getPublish_alarm() {
        return publish_alarm;
    }

    public void setPublish_alarm(String publish_alarm) {
        this.publish_alarm = publish_alarm;
    }

    public String getPublish_name() {
        return publish_name;
    }

    public void setPublish_name(String publish_name) {
        this.publish_name = publish_name;
    }

    public String getReply_alarm() {
        return reply_alarm;
    }

    public void setReply_alarm(String reply_alarm) {
        this.reply_alarm = reply_alarm;
    }

    public String getReply_name() {
        return reply_name;
    }

    public void setReply_name(String reply_name) {
        this.reply_name = reply_name;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_visible() {
        return is_visible;
    }

    public void setIs_visible(String is_visible) {
        this.is_visible = is_visible;
    }

    @Override
    public String toString() {
        return "CommentListBean{" +
                "publish_alarm='" + publish_alarm + '\'' +
                ", publish_name='" + publish_name + '\'' +
                ", reply_alarm='" + reply_alarm + '\'' +
                ", reply_name='" + reply_name + '\'' +
                ", post_id='" + post_id + '\'' +
                ", comment_id='" + comment_id + '\'' +
                ", content='" + content + '\'' +
                ", picture='" + picture + '\'' +
                ", type='" + type + '\'' +
                ", is_visible='" + is_visible + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentListBean that = (CommentListBean) o;
        return Objects.equals(publish_alarm, that.publish_alarm) &&
                Objects.equals(publish_name, that.publish_name) &&
                Objects.equals(reply_alarm, that.reply_alarm) &&
                Objects.equals(reply_name, that.reply_name) &&
                Objects.equals(post_id, that.post_id) &&
                Objects.equals(comment_id, that.comment_id) &&
                Objects.equals(content, that.content) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(type, that.type) &&
                Objects.equals(is_visible, that.is_visible) &&
                Objects.equals(is_accept_answer, that.is_accept_answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publish_alarm, publish_name, reply_alarm, reply_name, post_id, comment_id, content, picture, type, is_visible, is_accept_answer);
    }
}
