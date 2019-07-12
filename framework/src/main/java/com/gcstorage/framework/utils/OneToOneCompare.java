package com.gcstorage.framework.utils;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.arcsoft.facedetection.AFD_FSDKEngine;
import com.arcsoft.facedetection.AFD_FSDKError;
import com.arcsoft.facedetection.AFD_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKMatching;
import com.guo.android_extend.image.ImageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjs on 2018/11/23.
 */

public class OneToOneCompare {

    public static String appid = "CGJj3PzqWp9QccMUxLy3DRXfUHkBfdx7gLHoNUv2qQzE";
    public static String ft_key = "A58T5CrTnjfeKH1GsY7Csgq1aVCeyLqXeEuG7QjBSwF7";
    public static String fd_key = "A58T5CrTnjfeKH1GsY7Csgq8jtTpjJPxLAM4hHqwZZmi";
    public static String fr_key = "A58T5CrTnjfeKH1GsY7CsgqdPVWWVH4NqtJruvqEWxfo";

    private static volatile OneToOneCompare instance;
    private AFR_FSDKEngine mFREngine;
    private AFD_FSDKEngine mFDEngine;


    private OneToOneCompare() {
        init();
    }

    public static OneToOneCompare getInstance() {
        if (instance == null) {
            synchronized (OneToOneCompare.class) {
                if (instance == null) {
                    instance = new OneToOneCompare();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化虹软人脸相关识别引擎
     *
     * @return 初始化成功与否
     * 0:表示初始化成功，-1:表示初始化异常，-2:表示初始化人脸比对失败，-3:表示初始化人脸检测失败
     * 0x0001基础错误起始值，0x0001未知错误，0x0002参数错误，0x0003输入了引擎不支持的参数或者数据，
     * 0x0004内存不足，0x0005 状态错误（未初始化就调用了接口），0x0009内存上溢，
     * 0x000a内存下溢，0x7000校验错误起始值，0x7001 非法APPID，0x7002非法SDKID
     * 0x7003SDKKEY不是于当前APPID名下的，0x7004SDKKEY不是当前SDK所支持的，0x7005不支持的系统版本
     * 0x7006SDK过期，0x12000FR错误起始值，0x12001内存信息错误，0x12002图像信息错误
     * 0x12003人脸信息错误，0x12004GPU不支持，0x12005特征信息版本不匹配
     */
    private int init() {
        try {
            //人脸比对引擎
            mFREngine = new AFR_FSDKEngine();
            //初始化人脸比对类，返回的Error的code为ErrorInfo.MOK时候表示初始化成功
            AFR_FSDKError mFRError = mFREngine.AFR_FSDK_InitialEngine(appid, fr_key);
            if (mFRError == null) {
                return -2;
            }
            if (mFRError.getCode() != AFR_FSDKError.MOK) {
                return mFRError.getCode();
            }
            //人脸检测引擎
            mFDEngine = new AFD_FSDKEngine();
            //人脸检测引擎初始化，倒数第三个参数是定义脸部角度的检测范围，倒数第二个指定支持检测的最小人脸尺寸,有效值范围[2,32],推荐值16，最后一个参数是表示最大人脸检测数量
            AFD_FSDKError mFDError = mFDEngine.AFD_FSDK_InitialFaceEngine(
                    appid, fd_key, AFD_FSDKEngine.AFD_OPF_0_HIGHER_EXT, 16, 5);
            if (mFDError == null) {
                return -3;
            }
            if (mFDError.getCode() != AFD_FSDKError.MOK) {
                return mFDError.getCode();
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取两个人脸图片中人脸相似度
     * @param source 原图片
     * @param target 比对的人脸图片
     * @return 人脸比对后的相似度
     *  -1:比对人脸存在null,-2:获取原图片特征值失败
     *  -3:获取目标图片特征值失败，-4:人脸比对失败
     */
    public float compare(Bitmap source, Bitmap target) {
        if(source == null || target == null){
            return -1;
        }
        AFR_FSDKFace sourceFace = new AFR_FSDKFace();
        try {
            int sourceResult = getFeature(source, sourceFace);
            if(sourceResult != AFR_FSDKError.MOK){
                return -2;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -2;
        }
        AFR_FSDKFace targetFace = new AFR_FSDKFace();
        try {
            int targetResult = getFeature(target, targetFace);
            if(targetResult != AFR_FSDKError.MOK){
                return -3;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -3;
        }
        try {
            AFR_FSDKMatching result = new AFR_FSDKMatching();
            AFR_FSDKError mFRError = mFREngine.AFR_FSDK_FacePairMatching(sourceFace, targetFace, result);
            if(mFRError == null || mFRError.getCode() != AFR_FSDKError.MOK){
                return -4;
            }
            if(mFREngine != null){
                mFREngine.AFR_FSDK_UninitialEngine();
            }
            if(mFDEngine != null){
                mFDEngine.AFD_FSDK_UninitialFaceEngine();
            }
            instance = null;
            return result.getScore();
        }catch (Exception e){
            e.printStackTrace();
            return -4;
        }
    }

    /**
     * 获取对应图片中的人脸特征值
     * @param source 原图片的bitmap
     * @param feature 存放人脸特征值的对象
     * @return 错误码
     *  -1:传参中存在null字符串，-2:将bitmap转成nv21的字节流错误
     *  -3:输出人脸的错误码为null,-4:保存人脸特征错误码为null
     *  -5:没有检查到人脸
     */
    private int getFeature(Bitmap source, AFR_FSDKFace feature) {
        if (source == null || feature == null) {
            return -1;
        }
        //需要将bitmap转换成NV21格式的字节数据
        byte[] data = new byte[source.getWidth() * source.getHeight() * 3 / 2];
        ImageConverter convert = new ImageConverter();
        convert.initial(source.getWidth(), source.getHeight(), ImageConverter.CP_PAF_NV21);
        if (!convert.convert(source, data)) {
            return -2;
        }
        convert.destroy();
        List<AFD_FSDKFace> sourceResult = new ArrayList<>();
        //为检测输入的图像中存在的人脸，输出结果和初始化时设置的参数有密切关系
        AFD_FSDKError mFDError = mFDEngine.AFD_FSDK_StillImageFaceDetection(data, source.getWidth(), source.getHeight(), AFD_FSDKEngine.CP_PAF_NV21, sourceResult);
        if (mFDError == null) {
            return -3;
        }
        if (mFDError.getCode() != AFD_FSDKError.MOK) {
            return mFDError.getCode();
        }
        if (!sourceResult.isEmpty()) {
            //这个类用来保存人脸特征信息
            //输入的 data 数据为 NV21 格式（如 Camera 里 NV21 格式的 preview 数据）；人脸坐标一般使用人脸检测返回的 Rect 传入；人脸角度请按照人脸检测引擎返回的值传入。
            AFR_FSDKError mFRError = mFREngine.AFR_FSDK_ExtractFRFeature(data, source.getWidth()
                    , source.getHeight(), AFR_FSDKEngine.CP_PAF_NV21,
                    new Rect(sourceResult.get(0).getRect()), sourceResult.get(0).getDegree(), feature);
            if (mFRError == null) {
                return -4;
            }
            return mFRError.getCode();

        }
        return -5;
    }

}
