package com.fy.wanandroid.loadfile;


import com.fy.baselibrary.retrofit.load.down.DownInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aspsine on 2015/7/8.
 */
public class DataSource {

    private static DataSource sDataSource = new DataSource();

    private static final String[] NAMES = {
            "网易云音乐",
            "王者荣耀",
            "腾讯视频",
//            "斗鱼",
//            "前程无忧51job",
            "搜狐视频",
//            "微信电话本",
//            "淘宝"
    };

    private static final String[] IMAGES = {
            "http://img.wdjimg.com/mms/icon/v1/d/f1/1c8ebc9ca51390cf67d1c3c3d3298f1d_512_512.png",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528459943851&di=4ae7d508ea81db9fec68472bf3e5539c&imgtype=0&src=http%3A%2F%2Ftx.haiqq.com%2Fuploads%2Fallimg%2F170813%2F05350M031-6.jpg",
            "http://img.wdjimg.com/mms/icon/v1/8/10/1b26d9f0a258255b0431c03a21c0d108_512_512.png",
//            "http://www.qingpingshan.com/uploads/allimg/160927/21040012P-0.jpg",
//            "http://img.wdjimg.com/mms/icon/v1/e/d0/03a49009c73496fb8ba6f779fec99d0e_512_512.png",
            "http://img.wdjimg.com/mms/icon/v1/2/bf/939a67b179e75326aa932fc476cbdbf2_512_512.png",
//            "http://img.wdjimg.com/mms/icon/v1/b/fe/718d7c213ce633fd4e25c278c19acfeb_512_512.png",
//            "http://img.wdjimg.com/mms/icon/v1/f/29/cf90d1294ac84da3b49561a6f304029f_512_512.png"
    };

    private static final String[] URLS = {
            "http://s1.music.126.net/download/android/CloudMusic_2.8.1_official_4.apk",
            "http://dlied5.myapp.com/myapp/1104466820/sgame/2017_com.tencent.tmgp.sgame_h7916_1.42.1.20_5eb4f3.apk",
            "http://dldir1.qq.com/qqmi/TencentVideo_V4.1.0.8897_51.apk",
//            "http://dl001.liqucn.com/upload/2017/286/d/air.tv.douyu.android_4.2.0_liqucn.com.apk",
//            "http://www.51job.com/client/51job_51JOB_1_AND2.9.3.apk",
            "http://upgrade.m.tv.sohu.com/channels/hdv/5.0.0/SohuTV_5.0.0_47_201506112011.apk",
//            "http://dldir1.qq.com/qqcontacts/100001_phonebook_4.0.0_3148.apk",
//            "http://download.alicdn.com/wireless/taobao4android/latest/702757.apk"

    };

    public static DataSource getInstance() {
        return sDataSource;
    }

    public List<DownInfo> getData() {
        List<DownInfo> appInfos = new ArrayList<>();
        for (int i = 0; i < NAMES.length; i++) {
            DownInfo appInfo = new DownInfo(NAMES[i], IMAGES[i], URLS[i]);
            appInfos.add(appInfo);
        }
        return appInfos;
    }
}
