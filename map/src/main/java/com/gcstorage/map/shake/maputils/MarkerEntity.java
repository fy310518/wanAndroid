package com.gcstorage.map.shake.maputils;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 所有标记的实体对象
 *
 * @auther 邱坤
 * @date 2016/6/28.
 */
public class MarkerEntity implements Serializable, Comparable<MarkerEntity> {

    static final long serialVersionUID = -2215166914986085942L;
    /**
     * 摄像头标记
     */
    public static final String MARKER_MODE_CAMERA = "2";
    /**
     * 走访标记
     */
    public static final String MARKER_MODE_SIGN = "0";
    /**
     * 案发地
     */
    public static final String MARKER_MODE_CRIME_PLACE = "3";

    //接口信息相关
    public String token;
    public String action;

    //人员信息部分
    public String alarm;        //警号
    public String headpic;      //警察的头像
    public String realname;     //警察名称
    public String department;   //警察所在的单位名称

    public long _id;            //本地数据库主键

    //标记信息部分

    public String title;        //标题
    public String content;      //内容
    public String longitude;    //经度
    public String latitude;     //纬度
    public String position;     //位置
    public String picture;      //图片 url,url,...
    public String video;        //视频 url,url,...
    public String audio;        //录音 url,url,...
    public String create_time;  //创建时间
    public String push_time;  //创建时间
    public String workid;       //任务id
    public String work_name;    //任务名称
    public String orderid;      //指令id
    public String order_name;   //指令名称
    public String gid;          //群id
    public String cid;          //案件id
    public String parent_id;    //上一级id 目前不用
    public String height;       //摄像头高度 目前不用
    public String canera_number;//摄像头编号 目前不用
    //    public String camera_id;    //摄像头id   目前不用
    public String interid;      //远程id
    public String address;


    public String marker_id;

    //标记的类型
    public String mode;     //标记第一级类型: 0 走访标记 1 快速记录 2 摄像头标记 3 案发地 4 中间点 5突发事件
    public String type;     //标记第二级类型: 0 走访标记(0：嫌疑物 1：案发地 2：嫌疑车 3：嫌疑人 4：受伤者 5：集合点)
    // 1 快速记录（0：走访记录 1：追踪记录 2：从聊天进入的记录）
    // 2 摄像头标记（0：公安枪机 1：公安球机 2：非公枪机 3：非公球机 4：移动拍摄）
    public String direction;    //标记第三级类型: 摄像头方向
    //(0：下 1：左 2：上 3：右 4：左下 5：左上 6：右上 7：右下)

    public int entityType = 0;//草稿箱实体类型, 0 表示标记数据 ; 1 表示轨迹数据

    public Object extraData;//扩展数据 ,比如轨迹数据

    // 2017/01/11 新增字段
    public String input_time; //用户输入的逻辑时间
    public String dataid;
    public String plan_num;  //巡控进展的条目数

    public String end_time; //用户输入的结束时间
    public String supspects_ids; //嫌疑人id,字符串,用逗号分隔进行拼接


    public String suspects_ids; //嫌疑人id,字符串,用逗号分隔进行拼接
    public String camera_id; // 关联的摄像头id


    public String big_type;//区分摄像头的一类点和二类点 "1":标示一类点
    public String camera_time;//摄像头时间
    public String time_error;//时间的误差
    public String record_count;//评论数量
    public String previous_distance = "";//当前点和前一个点的距离
    public String previous_time = "";//当前点和前一个点的时间差
    public String velocity; //速度
    public int color_type; //当前点与下一个点,线的颜色 , 0 案发前, 1 案发后 2 无案发地情况
    public String camera_name;//关联的摄像头名称
    public String emid;//巡控突发疑情

    public String picture1;      //新的图片参数（[{"url":"http1","url1":"http2"},{"url":"http3","url1":"http4"}]）

    public String line_type = "0"; //"0" 标示实线; "1" 标示虚线 默认是实线

    public String validity = "0";    //0无效不连线，1有效连线. 表示打标页面的标记人/车 是否连线起来..默认传的有效
    public boolean isPline;     //标记点是否连线，用于点击高亮效果后的样式更改
    public boolean isSmall;     //标记点是否连线，用于点击高亮效果后的样式更改
    public MarkerEntity nextME; //标记点的下一个点的数据（用于判断是否可以创建中间点）



    private String name;
    private String phonenum;
    private String gps_h;
    private String gps_w;
    private String angle;

    public String getSuspects_ids() {
        return suspects_ids;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String s) {
        this.address = s;
    }

    public void setSuspects_ids(String suspects_ids) {
        this.suspects_ids = suspects_ids;
    }

    public String getBig_type() {
        return big_type;
    }

    public void setBig_type(String big_type) {
        this.big_type = big_type;
    }

    public String getDataid() {
        return dataid;
    }

    public void setDataid(String dataid) {
        this.dataid = dataid;
    }

    public void setSupspects_ids(String supspects_ids) {
        this.supspects_ids = supspects_ids;
    }

    public String getPreviousTime() {
        return previous_time;
    }

    public String getPicture1() {
        return picture1;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }

    public void setPreviousTime(String previous_time) {
        this.previous_time = previous_time;
    }

    public String getPreviousDistance() {
        return previous_distance;
    }

    public void setPreviousDistance(String previous_distance) {
        this.previous_distance = previous_distance;
    }

    public String getLine_type() {
        return line_type;
    }

    public void setLine_type(String line_type) {
        this.line_type = line_type;
    }

    public String getPush_time() {
        return push_time;
    }

    public void setPush_time(String push_time) {
        this.push_time = push_time;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public int getEntityType() {
        return entityType;
    }

    public String getInput_time() {
        return input_time;
    }

    public void setInput_time(String input_time) {
        this.input_time = input_time;
    }

    public String getSupspects_ids() {
        return supspects_ids;
    }


    public String getCamera_time() {
        return camera_time;
    }

    public String getTime_error() {
        return time_error;
    }

    public String getRecord_count() {
        return record_count;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getCamera_id() {
        return camera_id;
    }

    public void setCamera_id(String camera_id) {
        this.camera_id = camera_id;
    }

    public String getCanera_number() {
        return canera_number;
    }

    public void setCanera_number(String canera_number) {
        this.canera_number = canera_number;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getInterid() {
        return interid;
    }

    public void setInterid(String interid) {
        this.interid = interid;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMode() {
        return mode;
    }


    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getWork_name() {
        return work_name;
    }

    public void setWork_name(String work_name) {
        this.work_name = work_name;
    }

    public String getWorkid() {
        return workid;
    }

    public void setWorkid(String workid) {
        this.workid = workid;
    }

    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }

    public void setTime_error(String time_error) {
        this.time_error = time_error;
    }

    public void setCamera_time(String camera_time) {
        this.camera_time = camera_time;
    }

    public String getCamera_name() {
        return camera_name;
    }

    public void setCamera_name(String camera_name) {
        this.camera_name = camera_name;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getMarker_id() {
        return marker_id;
    }

    public void setMarker_id(String marker_id) {
        this.marker_id = marker_id;
    }

    public String create_user_name;
    public String create_user_id;
    public String patrol_task_detail;
    public String patrol_task_autio;
    public String patrol_task_video;
    public String patrol_status;
    public String patrol_task_address;
    public String patrol_task_img;
    public String patrol_task_name;
    public String patrol_task_id;

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public void setRecord_count(String record_count) {
        this.record_count = record_count;
    }

    public String getPrevious_distance() {
        return previous_distance;
    }

    public void setPrevious_distance(String previous_distance) {
        this.previous_distance = previous_distance;
    }

    public String getPrevious_time() {
        return previous_time;
    }

    public void setPrevious_time(String previous_time) {
        this.previous_time = previous_time;
    }

    public String getVelocity() {
        return velocity;
    }

    public void setVelocity(String velocity) {
        this.velocity = velocity;
    }


    public int getColor_type() {
        return color_type;
    }

    public void setColor_type(int color_type) {
        this.color_type = color_type;
    }


    public boolean isPline() {
        return isPline;
    }

    public void setPline(boolean pline) {
        isPline = pline;
    }

    public boolean isSmall() {
        return isSmall;
    }

    public void setSmall(boolean small) {
        isSmall = small;
    }

    public MarkerEntity getNextME() {
        return nextME;
    }

    public void setNextME(MarkerEntity nextME) {
        this.nextME = nextME;
    }


    public String getCreate_user_name() {
        return create_user_name;
    }

    public void setCreate_user_name(String create_user_name) {
        this.create_user_name = create_user_name;
    }

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getPatrol_task_detail() {
        return patrol_task_detail;
    }

    public void setPatrol_task_detail(String patrol_task_detail) {
        this.patrol_task_detail = patrol_task_detail;
    }

    public String getPatrol_task_autio() {
        return patrol_task_autio;
    }

    public void setPatrol_task_autio(String patrol_task_autio) {
        this.patrol_task_autio = patrol_task_autio;
    }

    public String getPatrol_task_video() {
        return patrol_task_video;
    }

    public void setPatrol_task_video(String patrol_task_video) {
        this.patrol_task_video = patrol_task_video;
    }

    public String getPatrol_status() {
        return patrol_status;
    }

    public void setPatrol_status(String patrol_status) {
        this.patrol_status = patrol_status;
    }


    public String getPatrol_task_address() {
        return patrol_task_address;
    }

    public void setPatrol_task_address(String patrol_task_address) {
        this.patrol_task_address = patrol_task_address;
    }

    public String getPatrol_task_img() {
        return patrol_task_img;
    }

    public void setPatrol_task_img(String patrol_task_img) {
        this.patrol_task_img = patrol_task_img;
    }


    public String getPatrol_task_name() {
        return patrol_task_name;
    }

    public void setPatrol_task_name(String patrol_task_name) {
        this.patrol_task_name = patrol_task_name;
    }

    public String getPatrol_task_id() {
        return patrol_task_id;
    }

    public void setPatrol_task_id(String patrol_task_id) {
        this.patrol_task_id = patrol_task_id;
    }

    @Override
    public int compareTo(MarkerEntity another) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(this.getCreate_time());
            d2 = format.parse(another.getCreate_time());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        c1.roll(Calendar.DAY_OF_MONTH, 1);
        c2.roll(Calendar.DAY_OF_MONTH, 1);

        return c2.compareTo(c1);
    }


    @Override
    public String toString() {
        return "MarkerEntity{" +
                "token='" + token + '\'' +
                ", action='" + action + '\'' +
                ", alarm='" + alarm + '\'' +
                ", headpic='" + headpic + '\'' +
                ", realname='" + realname + '\'' +
                ", department='" + department + '\'' +
                ", _id=" + _id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", position='" + position + '\'' +
                ", picture='" + picture + '\'' +
                ", video='" + video + '\'' +
                ", audio='" + audio + '\'' +
                ", create_time='" + create_time + '\'' +
                ", push_time='" + push_time + '\'' +
                ", workid='" + workid + '\'' +
                ", work_name='" + work_name + '\'' +
                ", orderid='" + orderid + '\'' +
                ", order_name='" + order_name + '\'' +
                ", gid='" + gid + '\'' +
                ", cid='" + cid + '\'' +
                ", parent_id='" + parent_id + '\'' +
                ", height='" + height + '\'' +
                ", canera_number='" + canera_number + '\'' +
                ", interid='" + interid + '\'' +
                ", address='" + address + '\'' +
                ", marker_id='" + marker_id + '\'' +
                ", mode='" + mode + '\'' +
                ", type='" + type + '\'' +
                ", direction='" + direction + '\'' +
                ", entityType=" + entityType +
                ", extraData=" + extraData +
                ", input_time='" + input_time + '\'' +
                ", dataid='" + dataid + '\'' +
                ", end_time='" + end_time + '\'' +
                ", supspects_ids='" + supspects_ids + '\'' +
                ", suspects_ids='" + suspects_ids + '\'' +
                ", camera_id='" + camera_id + '\'' +
                ", big_type='" + big_type + '\'' +
                ", camera_time='" + camera_time + '\'' +
                ", time_error='" + time_error + '\'' +
                ", record_count='" + record_count + '\'' +
                ", previous_distance='" + previous_distance + '\'' +
                ", previous_time='" + previous_time + '\'' +
                ", velocity='" + velocity + '\'' +
                ", color_type=" + color_type +
                ", camera_name='" + camera_name + '\'' +
                ", emid='" + emid + '\'' +
                ", picture1='" + picture1 + '\'' +
                ", line_type='" + line_type + '\'' +
                ", validity='" + validity + '\'' +
                ", isPline=" + isPline +
                ", isSmall=" + isSmall +
                ", nextME=" + nextME +
                ", create_user_name='" + create_user_name + '\'' +
                ", create_user_id='" + create_user_id + '\'' +
                ", patrol_task_detail='" + patrol_task_detail + '\'' +
                ", patrol_task_autio='" + patrol_task_autio + '\'' +
                ", patrol_task_video='" + patrol_task_video + '\'' +
                ", patrol_status='" + patrol_status + '\'' +
                ", patrol_task_address='" + patrol_task_address + '\'' +
                ", patrol_task_img='" + patrol_task_img + '\'' +
                ", patrol_task_name='" + patrol_task_name + '\'' +
                ", patrol_task_id='" + patrol_task_id + '\'' +
                '}';
    }

}
