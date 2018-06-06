package com.fy.baselibrary.retrofit.load.down;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fy.baselibrary.application.BaseApp;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.load.LoadService;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.GsonUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.T;
import com.fy.baselibrary.utils.ThreadUtils;
import com.fy.baselibrary.utils.TransfmtUtils;
import com.fy.baselibrary.utils.cache.ACache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 文件下载 管理类
 * Created by fangs on 2018/6/1.
 */
public class DownManager {

    /** 文件下载 默认下载线程数(目前建议是 3，以后谁知道呢 ^_^) */
    public static final int THREAD_COUNT = 3;

    /** 最大并行下载量 */
    private final int MAX_COUNT = ThreadUtils.maximumPoolSize / THREAD_COUNT;

    /** 将要下载的 任务队列 */
    private Queue<DownInfo> downQueue;
    /** 正在下载的任务 集合 */
    private List<DownInfo> downInfos;
    /** 所有下载任务的 回调集合*/
    private Map<String, DownLoadObserver> observerMap;


    private volatile static DownManager instentce;

    public static synchronized DownManager getInstentce(){
        if (null == instentce) {
            synchronized (RequestUtils.class) {
                if (null == instentce) {
                    instentce = new DownManager();
                }
            }
        }
        return instentce;
    }

    private DownManager() {
        downQueue   = new LinkedList<>();
        downInfos   = new ArrayList<>();
        observerMap = new HashMap<>();
    }


    /**
     * 开始下载
     * 1、正在执行下载任务 的数目小于“最大并行下载量”则从下载队列里面取一个任务
     */
    private void startDown() {
        DownInfo info = downQueue.poll();

        //正在下载不处理
        if (null == info) return;
        String url = info.getUrl();
        L.e("文件下载", "--》" + url);

        DownLoadObserver observer = observerMap.get(url);
        boolean isCache = false;
        for (DownInfo infobean : downInfos){
            if (infobean.getUrl().equals(url)){
                isCache = true;
                break;
            }
        }
        if (!isCache)downInfos.add(info);

        RequestUtils.create(LoadService.class)
                .download("", url)
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<ResponseBody, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(ResponseBody body) throws Exception {
                        //第一次请求全部文件长度
                        long sumLength = body.contentLength();
                        L.e("文件下载", sumLength + "--》" + url);

                        info.setCountLength(sumLength);
                        File targetFile = FileUtils.createFile(url);

                        ACache mCache = ACache.get(BaseApp.getAppCtx());
                        //计算各个线程下载的数据段
                        long bitLen = sumLength / THREAD_COUNT;
                        List<Observable> sources = new ArrayList<>();
                        for (int i = 0; i < THREAD_COUNT; i++){
                            Observable servece = null;

                            //开始位置，获取上次取消下载的进度
                            long startPosition = mCache.getAsLong(url + i + Constant.DownTherad);
                            //如果文件已经删除 则重新下载
                            if (targetFile.length() == 0){
                                startPosition = 0L;
                                info.getReadLength().set(0L);
                                //删除 每个线程 已经下载的 总长度 缓存
                                mCache.remove(url + i + Constant.DownTherad);
                            }

                            //结束位置，-1是为了防止上一个线程和下一个线程重复下载衔接处数据
                            long endPosition = i + 1 < THREAD_COUNT ? (i + 1) * bitLen - 1 : (i + 1) * bitLen + THREAD_COUNT;

                            //判断是否下载全部文件
                            if (startPosition < endPosition) {
                                startPosition = startPosition > 0L ? startPosition + bitLen * i : bitLen * i;
                                servece = download(url + i, startPosition, endPosition, url, targetFile, observer);
                            }

                            if (null != servece) sources.add(servece);
                        }

                        RandomAccessFile raf = new RandomAccessFile(targetFile, "rwd");
                        raf.setLength(sumLength);
                        raf.close();

                        Observable[] observables = new Observable[sources.size()];
                        return Observable.mergeArray(sources.toArray(observables));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private Observable download(@NonNull String nameKey,
                                      @NonNull final long start,
                                      @NonNull final long end,
                                      @NonNull final String url,
                                      File targetFile, DownLoadObserver observer) {

        String str = end == -1 ? "" : end + "";

        return RequestUtils.create(LoadService.class)
                .download("bytes=" + start + "-" + str, url)
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribeOn(Schedulers.io())
                .doOnNext((ResponseBody body) -> writeCaches(nameKey, start, body.byteStream(), targetFile, observer))
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 异步 写入文件
     *
     * @param saveFile
     */
    private void writeCaches(@NonNull String nameKey, @NonNull final long start,
                                   InputStream is,
                                   File saveFile,
                                   DownLoadObserver observer) throws IOException {
        RandomAccessFile raf = null;
        byte[] buffer = new byte[1024 * 4];

        try {
            raf = new RandomAccessFile(saveFile, "rwd");
            // 定位该线程的下载位置
            raf.seek(start);
            L.e("Thread", start + "---》" + Thread.currentThread().getName() + "-->" + Thread.currentThread().getId());

            ACache mCache = ACache.get(BaseApp.getAppCtx());
            //当前线程下载 总长度
            long loaded = mCache.getAsLong(nameKey + Constant.DownTherad);

            int len;
            while ((len = is.read(buffer)) != -1) {
                raf.write(buffer, 0, len);
                loaded += len;
                observer.onRead(len);

                //缓存当前线程 下载内容 总长度
                mCache.put(nameKey + Constant.DownTherad, loaded);
            }
        } finally {
            if (null != is) is.close();
            if (null != raf) raf.close();
        }
    }

    /**
     * 增加一个下载任务 到 下载队列
     * @param downInfo
     */
    public DownManager addDownTask(DownInfo downInfo, DownLoadListener loadListener) {
        String url = downInfo.getUrl();
        if (TextUtils.isEmpty(url)) return getInstentce();

        //获取缓存中 所有的下载任务列表
        ACache mCache = ACache.get(BaseApp.getAppCtx());
        String jsonArray = mCache.getAsString(Constant.AllDownTask);
        if (null != jsonArray && !TextUtils.isEmpty(jsonArray)) {
            downInfos =  GsonUtils.jsonToList(jsonArray, DownInfo.class);
        }

        /**
         * 1、判断缓存的下载任务列表中 是否存在指定url的 下载任务
         * 2、如果存在指定 url 的下载任务，判断该任务的文件是否存在，不存在则替换 下载列表中对应的任务
         */
        File targetFile = FileUtils.createFile(url);
        for (int i = 0; i < downInfos.size(); i++){
            DownInfo infobean = downInfos.get(i);
            if (infobean.getUrl().equals(url)){
                if (targetFile.length() == 0) {
                    downInfos.set(i, downInfo);
                    break;
                }

                downInfo = infobean;
                break;
            }
        }

        if (downInfo.getStateInte() == DownInfo.STATUS_COMPLETE) {
            T.showLong("此任务已完成");
            if (null != loadListener)
                loadListener.onProgress(downInfo.getReadLength().get(),
                        downInfo.getCountLength(),
                        downInfo.getPercent());
            return getInstentce();
        } else if (downInfo.getStateInte() == DownInfo.STATUS_PAUSED || downInfo.getStateInte() == DownInfo.STATUS_NOT_DOWNLOAD) {
            T.showLong("开始下载");
            if (null != loadListener)
                loadListener.onProgress(downInfo.getReadLength().get(),
                        downInfo.getCountLength(),
                        downInfo.getPercent());
        }

        if (observerMap.get(url) == null) {
            //向下载队列添加下载任务
            DownLoadObserver observer = new DownLoadObserver(downInfo, loadListener);
            observerMap.put(url, observer);
            downQueue.offer(downInfo);
        }

        return getInstentce();
    }

    /**
     * 开始下载
     * @Desc 多线程、多任务下载
     */
    public void runDownTask() {
        for (int i = 0; i < MAX_COUNT; i++) {
            //开始下载
            startDown();
        }
    }

    /**
     * 暂停下载
     *
     * @param url
     */
    public void pause(@NonNull final String url) {
        if (observerMap.containsKey(url)) {
            DownLoadObserver observer = observerMap.get(url);
            observer.getDisposed().dispose();//切断当前的订阅
        }
    }

    /**
     * 暂停所有下载任务
     */
    public void pauseAll() {
        for (String url : observerMap.keySet()) {
            //暂停下载
            pause(url);
        }
    }

    /**
     * 取消下载
     * @param url
     */
    public void cancle(@NonNull final String url){
        for (DownInfo infobean : downInfos){
            if (infobean.getUrl().equals(url)){
                infobean.setStateInte(DownInfo.STATUS_CANCEL);
                break;
            }
        }
        pause(url);
    }

    /**
     * 取消所有下载任务
     */
    public void cancleAll() {
        for (DownInfo infobean : downInfos) {
            infobean.setStateInte(DownInfo.STATUS_CANCEL);
            pause(infobean.getUrl());
        }
    }



    /**
     * 清除 指定的下载任务 回调
     * @param downInfo
     */
    public void removeTask(DownInfo downInfo){
        if (null == downInfo || observerMap.get(downInfo.getUrl()) == null) return;
        observerMap.remove(downInfo.getUrl());

        boolean isRemove = downInfos.remove(downInfo);
        L.e("清除下载完成的任务", downInfo.getUrl() + "--->" + isRemove + "-->" + Thread.currentThread().getName());
    }

    /**
     * 缓存所有下载信息
     */
    public void saveDownInfo(DownInfo downInfo) {
        List<DownInfo> list = null;
        //获取缓存中 所有的未完成下载任务列表
        ACache mCache = ACache.get(BaseApp.getAppCtx());
        String jsonArray = mCache.getAsString(Constant.AllDownTask);
        if (null != jsonArray && !TextUtils.isEmpty(jsonArray)) {
            list = GsonUtils.jsonToList(jsonArray, DownInfo.class);
        }

        if (null == list) {
            list = new ArrayList<>();
            list.add(downInfo);
        } else {
            boolean isCache = false;
            for (int i = 0; i < list.size(); i++){
                DownInfo infobean = list.get(i);
                if (infobean.equals(downInfo)){
                    list.set(i, downInfo);
                    isCache = true;
                    break;
                }
            }

            if (!isCache)list.add(downInfo);
        }

        mCache.put(Constant.AllDownTask, GsonUtils.listToJson(list));
    }


    /**
     * 获取所有 未下载任务 集合
     * @return
     */
    public List<DownInfo> getDownTask(){
        List<DownInfo> data = new ArrayList<>();
        data.addAll(downQueue);
        return data;
    }

    /**
     * 获取 正在下载的任务 集合
     * @return
     */
    public List<DownInfo> getDownloading(){
        List<DownInfo> data = new ArrayList<>();
        data.addAll(downInfos);

        return data;
    }

    /**
     * 获取 所有下载任务 集合
     * @return
     */
    public List<DownInfo> getAllTask(){
        List<DownInfo> data = new ArrayList<>();
        data.addAll(getDownloading());
        data.addAll(getDownTask());

        return data;
    }
}
