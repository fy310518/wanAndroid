package com.fy.wanandroid.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 文章列表 实体类
 * Created by fangs on 2018/4/16.
 */
public class ArticleBean implements Serializable {

    /**
     * curPage : 1
     * datas : [{"apkLink":"","author":"scsfwgy","chapterId":339,"chapterName":"K线图","collect":false,"courseId":13,"desc":"各种金融类的自定义View,基金走势图、分时图、蜡烛图、各种指标等，一步一步构建庞大的基金自定View... http://blog.csdn.net/wgyscsf\r\n","envelopePic":"http://www.wanandroid.com/blogimgs/3a9d2cbb-24d7-4c85-8497-9e1af6b64d23.png","fresh":false,"id":2799,"link":"http://www.wanandroid.com/blog/show/2105","niceDate":"2018-04-09","origin":"","projectLink":"https://github.com/scsfwgy/FinancialCustomerView","publishTime":1523265010000,"superChapterId":294,"superChapterName":"开源项目主Tab","tags":[{"name":"项目","url":"/project/list/1?cid=339"}],"title":"走势图、分时图、蜡烛图 FinancialCustomerView","type":0,"visible":1,"zan":0}]
     * offset : 0
     * over : false
     * pageCount : 61
     * size : 20
     * total : 1213
     */

    private int curPage;
    private int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;
    private List<DatasBean> datas;

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean implements Serializable {
        /**
         * apkLink :
         * author : scsfwgy
         * chapterId : 339
         * chapterName : K线图
         * collect : false
         * courseId : 13
         * desc : 各种金融类的自定义View,基金走势图、分时图、蜡烛图、各种指标等，一步一步构建庞大的基金自定View... http://blog.csdn.net/wgyscsf
         * <p>
         * envelopePic : http://www.wanandroid.com/blogimgs/3a9d2cbb-24d7-4c85-8497-9e1af6b64d23.png
         * fresh : false
         * id : 2799
         * link : http://www.wanandroid.com/blog/show/2105
         * niceDate : 2018-04-09
         * origin :
         * projectLink : https://github.com/scsfwgy/FinancialCustomerView
         * publishTime : 1523265010000
         * superChapterId : 294
         * superChapterName : 开源项目主Tab
         * tags : [{"name":"项目","url":"/project/list/1?cid=339"}]
         * title : 走势图、分时图、蜡烛图 FinancialCustomerView
         * type : 0
         * visible : 1
         * "originId": 2829,
         * "userId": 4267,
         * zan : 0
         */

        private String apkLink = "";
        private String author = "";
        private int chapterId;
        private String chapterName = "";
        private boolean collect = true;//设置默认值true （方便 个人收藏界面使用）
        private int courseId;
        private String desc = "";
        private String envelopePic = "";
        private boolean fresh;
        private int id;
        private String link = "";
        private String niceDate = "";
        private String origin = "";
        private int originId = -1;
        private int userId = -1;
        private String projectLink = "";
        private long publishTime;
        private int superChapterId;
        private String superChapterName = "";
        private String title = "";
        private int type;
        private int visible;
        private int zan;
        private List<TagsBean> tags;

        public String getApkLink() {
            return apkLink;
        }

        public void setApkLink(String apkLink) {
            this.apkLink = apkLink;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getChapterId() {
            return chapterId;
        }

        public void setChapterId(int chapterId) {
            this.chapterId = chapterId;
        }

        public String getChapterName() {
            return chapterName;
        }

        public void setChapterName(String chapterName) {
            this.chapterName = chapterName;
        }

        public boolean isCollect() {
            return collect;
        }

        public void setCollect(boolean collect) {
            this.collect = collect;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getEnvelopePic() {
            return envelopePic;
        }

        public void setEnvelopePic(String envelopePic) {
            this.envelopePic = envelopePic;
        }

        public boolean isFresh() {
            return fresh;
        }

        public void setFresh(boolean fresh) {
            this.fresh = fresh;
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

        public String getNiceDate() {
            return niceDate;
        }

        public void setNiceDate(String niceDate) {
            this.niceDate = niceDate;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getProjectLink() {
            return projectLink;
        }

        public void setProjectLink(String projectLink) {
            this.projectLink = projectLink;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }

        public int getSuperChapterId() {
            return superChapterId;
        }

        public void setSuperChapterId(int superChapterId) {
            this.superChapterId = superChapterId;
        }

        public String getSuperChapterName() {
            return superChapterName;
        }

        public void setSuperChapterName(String superChapterName) {
            this.superChapterName = superChapterName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getVisible() {
            return visible;
        }

        public void setVisible(int visible) {
            this.visible = visible;
        }

        public int getZan() {
            return zan;
        }

        public void setZan(int zan) {
            this.zan = zan;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public int getOriginId() {
            return originId;
        }

        public void setOriginId(int originId) {
            this.originId = originId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public static class TagsBean implements Serializable {
            /**
             * name : 项目
             * url : /project/list/1?cid=339
             */

            private String name = "";
            private String url = "";

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
