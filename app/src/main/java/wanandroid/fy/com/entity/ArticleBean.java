package wanandroid.fy.com.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 文章列表 实体类
 * Created by fangs on 2018/4/16.
 */
public class ArticleBean implements Serializable {

    /**
     * apkLink :
     * author : kangdongpu
     * chapterId : 294
     * chapterName : 完整项目
     * collect : false
     * courseId : 13
     * desc : 玩安卓客户端(Java版)

     * envelopePic : http://www.wanandroid.com/blogimgs/f3be8cc0-6b4d-4ee2-8f45-fe132949d640.png
     * fresh : false
     * id : 2797
     * link : http://www.wanandroid.com/blog/show/2104
     * niceDate : 2018-04-08
     * origin :
     * projectLink : https://github.com/kangdongpu/WanAndroid
     * publishTime : 1523187585000
     * superChapterId : 294
     * superChapterName : 开源项目主Tab
     * tags : [{"name":"项目","url":"/project/list/1?cid=294"}]
     * title : 玩安卓客户端(Java版) WanAndroid
     * type : 0
     * visible : 1
     * zan : 0
     */

    private String apkLink = "";
    private String author = "";
    private int chapterId;
    private String chapterName = "";
    private boolean collect;
    private int courseId;
    private String desc = "";
    private String envelopePic = "";
    private boolean fresh;
    private int id;
    private String link = "";
    private String niceDate = "";
    private String origin = "";
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

    public static class TagsBean implements Serializable{
        /**
         * name : 项目
         * url : /project/list/1?cid=294
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
