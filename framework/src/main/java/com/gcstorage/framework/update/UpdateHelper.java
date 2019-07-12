package com.gcstorage.framework.update;

import android.text.TextUtils;

import com.gcstorage.framework.net.SimpleDownloadListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.net.SocketTimeoutException;

/**
 * Created by zjs on 2018/12/26.
 */

public class UpdateHelper {

    private static volatile UpdateHelper mInstance;

    /**
     * 下载地址
     */
    private String mDownloadUrl;

    /**
     * 下载过程中的监听
     */
    private OnUpdateListener mListener;

    /**
     * 保存的文件夹
     */
    private String mFolder;

    /**
     * 下载后的文件名
     */
    private String mFileName;

    private UpdateHelper() {
    }

    public static UpdateHelper getInstance(){
        if(mInstance == null){
            synchronized (UpdateHelper.class){
                if(mInstance == null){
                    mInstance = new UpdateHelper();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置下载地址
     * @param url 下载路径
     */
    public UpdateHelper setDownloadUrl(String url){
        this.mDownloadUrl = url;
        return this;
    }

    /**
     * 设置下载监听
     * @param listener 监听器
     */
    public UpdateHelper setOnUpdateListener(OnUpdateListener listener){
        this.mListener = listener;
        return this;
    }

    /**
     * 设置下载后文件保存的文件夹
     * @param folder 文件夹路径
     */
    public UpdateHelper setUpdateFolder(String folder){
        this.mFolder = folder;
        return this;
    }

    /**
     * 设置保存的文件名
     * @param fileName 文件名
     */
    public UpdateHelper setFileName(String fileName){
        this.mFileName = fileName;
        return this;
    }

    /**
     * 开启更新
     */
    public void startUpdate(){
        PostRequest<File> post = OkGo.post(mDownloadUrl);
        DownloadTask request = OkDownload.request(UpdateHelper.class.getName(), post);
        if(!TextUtils.isEmpty(mFolder)){
            request.folder(mFolder);
        }
        if(!TextUtils.isEmpty(mFileName)){
            request.fileName(mFileName);
        }
        request.save()
                .register(new SimpleDownloadListener(UpdateHelper.class.getName()){

                    @Override
                    public void onProgress(Progress progress) {
                        if(mListener != null){
                            mListener.onProgress(progress.fraction);
                        }
                    }

                    @Override
                    public void onFinish(File file, Progress progress) {
                        if(mListener != null){
                            mListener.onFinish(file);
                        }
                    }

                    @Override
                    public void onError(Progress progress) {
                        if(mListener != null){
                            mListener.onError();
                        }
                    }
                }).start();
    }
}
