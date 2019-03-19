package com.gcstorage.circle.bean;

import java.io.Serializable;

/**
 * DESCRIPTION：定义 帖子列表 实体类
 * Created by fangs on 2019/3/19 15:13.
 */
public class CircleListBean implements Serializable {

    private int isShowCamera = 0;   //是否 评论 1：表示评论；0：表示帖子

    private LyCircleListBean lyCircleListBean;
    private boolean isExistenceComment;//是否存在评论；或者是否是最后一条评论

    private CommentListBean  commentListBean;

    public CircleListBean(LyCircleListBean lyCircleListBean) {
        this.lyCircleListBean = lyCircleListBean;
        if (lyCircleListBean.getComment_list().size() > 0)this.isExistenceComment = true;
    }

    public CircleListBean(int isShowCamera, CommentListBean commentListBean, boolean isExistenceComment) {
        this.isShowCamera = isShowCamera;
        this.commentListBean = commentListBean;
        this.isExistenceComment = isExistenceComment;
    }


    public boolean isExistenceComment() {
        return isExistenceComment;
    }

    public void setExistenceComment(boolean existenceComment) {
        isExistenceComment = existenceComment;
    }

    public int getIsShowCamera() {
        return isShowCamera;
    }

    public void setIsShowCamera(int isShowCamera) {
        this.isShowCamera = isShowCamera;
    }

    public LyCircleListBean getLyCircleListBean() {
        return lyCircleListBean;
    }

    public void setLyCircleListBean(LyCircleListBean lyCircleListBean) {
        this.lyCircleListBean = lyCircleListBean;
    }

    public CommentListBean getCommentListBean() {
        return commentListBean;
    }

    public void setCommentListBean(CommentListBean commentListBean) {
        this.commentListBean = commentListBean;
    }

    @Override
    public String toString() {
        return "CircleListBean{" +
                "isShowCamera=" + isShowCamera +
                ", lyCircleListBean=" + lyCircleListBean +
                ", commentListBean=" + commentListBean +
                '}';
    }
}
