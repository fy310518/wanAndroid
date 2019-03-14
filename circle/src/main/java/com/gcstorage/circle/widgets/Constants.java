package com.gcstorage.circle.widgets;

/**
 * @author KCrason
 * @date 2018/4/27
 */
public class Constants {

    public final static class FriendCircleType {
        //纯文字
        public final static int FRIEND_CIRCLE_TYPE_ONLY_WORD = 0;
        //文字和图片
        public final static int FRIEND_CIRCLE_TYPE_WORD_AND_IMAGES = 1;
        //分享链接
        public final static int FRIEND_CIRCLE_TYPE_WORD_AND_URL = 2;
    }

    public final static class CommentType {
        //单一评论
        public final static int COMMENT_TYPE_SINGLE = 0;
        //回复评论
        public final static int COMMENT_TYPE_REPLY = 1;
    }

    public final static class EmojiType {
        //单一评论
        public final static int EMOJI_TYPE_01 = 1;
        //回复评论
        public final static int EMOJI_TYPE_02 = 2;
    }

    /**
     * 点赞的action
     */
    public static final String POST_PRAISE_ADD = "setpraise";
    /**
     * 取消点赞的action
     */
    public static final String POST_PRAISE_DEL = "delpraise";

    /**
     * 主页地区模块列表
     */
    public static final String TYPE_CIRCLE_MODEPOST = "modepost";
    /**
     * 个人所有动态列表
     */
    public static final String TYPE_CIRCLE_SELFPUBLISH = "selfall";
}
