package com.gcstorage.circle.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * DESCRIPTION： 战友圈 列表实体类
 * Created by fangs on 2019/3/11 17:54.
 */
public class LyCircleListBean implements Serializable {


    /**
     * tag_list : [{"tag_type":"tie","tag_count":"2"}]
     * istop : 0
     * mode : dynamic
     * is_share : 0
     * praise_list : [{"praise_name":"刷脸登录演示专用1","praise_alarm":"SLDLYSZY001"}]
     * rewardtask :
     * alarm : SLDLYSZY001
     * share_content :
     * praise_count : 1
     * ispraise : 1
     * number :
     * comment_list : []
     * is_accept : 0
     * ispravite : 1
     * create_time : 2019-02-22 14:07:26
     * read_count : 0
     * content : 你好
     * online_level : 2

     * certificate : 0
     * comment_count : 0
     * name : 刷脸登录演示专用1
     * postid : 6
     * headpic : http://113.57.174.98:13201/tax05/M00/08/05/NUQPAFvamBSAVVLXAAIaEpCbEuM443.jpg
     * department : 技术支持
     * position : 广西壮族自治区南宁市青秀区东葛路靠近广西壮族自治区公安厅
     * picture :
     * pushtime : 1550815646
     * integral_count : 0
     */

    private String istop= "";
    private String mode= "";
    private String is_share= "";
    private String rewardtask= "";
    private String alarm= "";
    private String share_content= "";
    private String praise_count= "0";
    private String ispraise= "0";
    private String number= "";
    private String is_accept= "";
    private String ispravite= "";
    private String create_time= "";
    private String read_count= "1";
    private String content= "";
    private String online_level= "0";
    private String certificate= "";
    private String comment_count= "";
    private String name= "";
    private String address= "";
    private String postid= "";
    private String headpic= "";
    private String department= "";
    private String position= "";
    private String picture= "";
    private long pushtime;
    private String integral_count= "0";
    private List<TagListBean> tag_list;
    private List<PraiseListBean> praise_list;
    private List<?> comment_list;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIstop() {
        return istop;
    }

    public void setIstop(String istop) {
        this.istop = istop;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getIs_share() {
        return is_share;
    }

    public void setIs_share(String is_share) {
        this.is_share = is_share;
    }

    public String getRewardtask() {
        return rewardtask;
    }

    public void setRewardtask(String rewardtask) {
        this.rewardtask = rewardtask;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getShare_content() {
        return share_content;
    }

    public void setShare_content(String share_content) {
        this.share_content = share_content;
    }

    public String getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(String praise_count) {
        this.praise_count = praise_count;
    }

    public String getIspraise() {
        return ispraise;
    }

    public void setIspraise(String ispraise) {
        this.ispraise = ispraise;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIs_accept() {
        return is_accept;
    }

    public void setIs_accept(String is_accept) {
        this.is_accept = is_accept;
    }

    public String getIspravite() {
        return ispravite;
    }

    public void setIspravite(String ispravite) {
        this.ispravite = ispravite;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getRead_count() {
        return read_count;
    }

    public void setRead_count(String read_count) {
        this.read_count = read_count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOnline_level() {
        return online_level;
    }

    public void setOnline_level(String online_level) {
        this.online_level = online_level;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public long getPushtime() {
        return pushtime;
    }

    public void setPushtime(long pushtime) {
        this.pushtime = pushtime;
    }

    public String getIntegral_count() {
        return integral_count;
    }

    public void setIntegral_count(String integral_count) {
        this.integral_count = integral_count;
    }

    public List<TagListBean> getTag_list() {
        return tag_list;
    }

    public void setTag_list(List<TagListBean> tag_list) {
        this.tag_list = tag_list;
    }

    public List<PraiseListBean> getPraise_list() {
        return praise_list;
    }

    public void setPraise_list(List<PraiseListBean> praise_list) {
        this.praise_list = praise_list;
    }

    public List<?> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<?> comment_list) {
        this.comment_list = comment_list;
    }

    @Override
    public String toString() {
        return "LyCircleListBean{" +
                "istop='" + istop + '\'' +
                ", mode='" + mode + '\'' +
                ", is_share='" + is_share + '\'' +
                ", rewardtask='" + rewardtask + '\'' +
                ", alarm='" + alarm + '\'' +
                ", share_content='" + share_content + '\'' +
                ", praise_count='" + praise_count + '\'' +
                ", ispraise='" + ispraise + '\'' +
                ", number='" + number + '\'' +
                ", is_accept='" + is_accept + '\'' +
                ", ispravite='" + ispravite + '\'' +
                ", create_time='" + create_time + '\'' +
                ", read_count='" + read_count + '\'' +
                ", content='" + content + '\'' +
                ", online_level='" + online_level + '\'' +
                ", certificate='" + certificate + '\'' +
                ", comment_count='" + comment_count + '\'' +
                ", name='" + name + '\'' +
                ", postid='" + postid + '\'' +
                ", headpic='" + headpic + '\'' +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", picture='" + picture + '\'' +
                ", pushtime=" + pushtime +
                ", integral_count='" + integral_count + '\'' +
                ", tag_list=" + tag_list +
                ", praise_list=" + praise_list +
                ", comment_list=" + comment_list +
                '}';
    }

    public static class TagListBean implements Serializable{
        /**
         * tag_type : tie
         * tag_count : 2
         */

        private String tag_type= "";
        private String tag_count= "";

        public String getTag_type() {
            return tag_type;
        }

        public void setTag_type(String tag_type) {
            this.tag_type = tag_type;
        }

        public String getTag_count() {
            return tag_count;
        }

        public void setTag_count(String tag_count) {
            this.tag_count = tag_count;
        }

        @Override
        public String toString() {
            return "TagListBean{" +
                    "tag_type='" + tag_type + '\'' +
                    ", tag_count='" + tag_count + '\'' +
                    '}';
        }
    }

    public static class PraiseListBean implements Serializable{
        /**
         * praise_name : 刷脸登录演示专用1
         * praise_alarm : SLDLYSZY001
         */

        private String praise_name= "";
        private String praise_alarm= "";

        public String getPraise_name() {
            return praise_name;
        }

        public void setPraise_name(String praise_name) {
            this.praise_name = praise_name;
        }

        public String getPraise_alarm() {
            return praise_alarm;
        }

        public void setPraise_alarm(String praise_alarm) {
            this.praise_alarm = praise_alarm;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PraiseListBean that = (PraiseListBean) o;
            return Objects.equals(praise_name, that.praise_name) &&
                    Objects.equals(praise_alarm, that.praise_alarm);
        }

        @Override
        public int hashCode() {
            return Objects.hash(praise_name, praise_alarm);
        }

        @Override
        public String toString() {
            return "PraiseListBean{" +
                    "praise_name='" + praise_name + '\'' +
                    ", praise_alarm='" + praise_alarm + '\'' +
                    '}';
        }
    }
}
