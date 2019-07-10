package com.gcstorage.parkinggather;

/**
 * DESCRIPTION：网络请求地址 常量
 * Created by fangs on 2019/3/20 15:14.
 */
public interface ApiConstant {

    //    47环境
    String mapHOST = "47.107.134.212";
    String mapPORT = "13201";
    //    String HOST_ADDRESS_VIDEO = "http://" + mapHOST + ":20003/"; //互联网+  IP和端口
    String HOST_ADDRESS = "http://" + mapHOST + ":" + mapPORT + "/";
    //    String SHAKE_URL = HOST_ADDRESS + "MicroRecon/1.2/sharkgetdata";//互联网摇一摇的接口
    String SHAKE_URL = "http://20.51.3.43:8768/falcon_transfer/MicroRecon/1.2/sharkgetdata";//警务通猎鹰摇一摇的接口
    String LYHOST = HOST_ADDRESS + "Falcon/2.0/";//猎鹰 战友圈 互联网请求地址
    String zyq_HOST = "http://47.107.134.212:13202/Falcon/1.1/";//门户 战友圈 互联网请求地址
    String SHAKE_RTSP_URL = "http://120.24.1.46:13201/Falcon/2.0/dfwl/videoService";//摇一摇的接口
    String HEART_BEAT_URL = HOST_ADDRESS + "Falcon/2.0/main/heartbeat";//心跳包

    //演示环境
//    String mapHOST = "120.25.62.193";
//    String mapPORT = "13201";
//    String HOST_ADDRESS = "http://" + mapHOST + ":" + mapPORT + "/";
//    String SHAKE_URL = HOST_ADDRESS + "MicroRecon/1.2/sharkgetdata";//摇一摇的接口
//    String SHAKE_URL = "http://20.51.3.43:8768/falcon_transfer/MicroRecon/1.2/sharkgetdata";//警务通猎鹰摇一摇的接口
//    String LYHOST = HOST_ADDRESS + "Falcon/2.0/";//猎鹰 战友圈 互联网请求地址
//    String zyq_HOST = "http://120.25.62.193:13202/Falcon/1.1/";//门户 战友圈 互联网请求地址
//    String SHAKE_RTSP_URL = "http://120.24.1.46:13201/Falcon/2.0/dfwl/videoService";//摇一摇的接口
//    String HEART_BEAT_URL = HOST_ADDRESS + "Falcon/2.0/main/heartbeat";//心跳包


    //    20环境
//    String mapHOST = "20.51.3.43";
//    String mapPORT = "8768";
//    String HOST_ADDRESS = "http://" + mapHOST + ":" + mapPORT + "/";
//    //    String SHAKE_URL = HOST_ADDRESS + "TYMH_XX/MicroRecon/1.2/sharkgetdata";//摇一摇的接口
//    String SHAKE_URL = "http://20.51.3.43:8768/falcon_transfer/MicroRecon/1.2/sharkgetdata";//警务通猎鹰摇一摇的接口
//    String LYHOST = HOST_ADDRESS + "TYMH_XX/Falcon/2.0/";//猎鹰 战友圈 互联网请求地址
//    String zyq_HOST = HOST_ADDRESS + "TYMH_ZYQ/Falcon/1.1/";//门户 战友圈 互联网请求地址
//    String SHAKE_RTSP_URL = "http://120.24.1.46:13201/Falcon/2.0/dfwl/videoService";//摇一摇的接口
//    String HEART_BEAT_URL = HOST_ADDRESS + "TYMH_XX/Falcon/2.0/main/heartbeat";//心跳包


    String HOST_ADDRESS_VIDEO = "http://www.doclever.cn:8090/mock/5d07594c4a9da91cd64be43c/";
    //视频查看:根据经纬度获取摄像头资源
    String INTERNET_VIDEO_LIST = HOST_ADDRESS_VIDEO + "InternetVideo/getVideoListByLL";
    //视频查看:筛选视频搜索条件
    String INTERNET_VIDEO_SEARCH = HOST_ADDRESS_VIDEO + "InternetVideo/getDic";
    //视频查看:获取摄像头历史视频
    String INTERNET_VIDEO_HISTORY = HOST_ADDRESS_VIDEO + "InternetVideo/getHistorageStream";
    //视频查看:获取摄像头当前视频流
    String INTERNET_VIDEO_CURRENT = HOST_ADDRESS_VIDEO + "InternetVideo/getVideoStream";
    //删除收藏(可一次删除多个)
    String INTERNET_VIDEO_COLLECTION_DEL = HOST_ADDRESS_VIDEO + "InternetVideo/deleteCollection";
    //查询收藏列表
    String INTERNET_VIDEO_COLLECTION_QUERY = HOST_ADDRESS_VIDEO + "InternetVideo/serchCollectionList";
    //收藏摄像头
    String INTERNET_VIDEO_COLLECTION_ADD = HOST_ADDRESS_VIDEO + "InternetVideo/addCollection";
}
