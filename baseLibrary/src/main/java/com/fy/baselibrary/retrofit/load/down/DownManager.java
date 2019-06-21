package com.fy.baselibrary.retrofit.load.down;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fy.baselibrary.application.ioc.ConfigUtils;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.load.LoadService;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.GsonUtils;
import com.fy.baselibrary.utils.notify.L;
import com.fy.baselibrary.utils.TransfmtUtils;
import com.fy.baselibrary.utils.cache.ACache;
import com.fy.baselibrary.utils.notify.T;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 文件下载 管理类 (todo 启动一个后台服务执行下载操作)
 * Created by fangs on 2018/6/1.
 */
public class DownManager {

    /** 文件下载 默认下载线程数(目前建议是 3，以后谁知道呢 ^_^) */
    public static final int THREAD_COUNT = 1;

    /** 最大并行下载量 */
//    private final int MAX_COUNT = ThreadUtils.maximumPoolSize / THREAD_COUNT;
    private final int MAX_COUNT = 1;

    /** 是否正在执行下载任务 */
    private boolean isRunDownLoad;
    /** 将要下载的 任务队列 */
    private LinkedList<DownInfo> downQueue;
    /** 正在下载的任务 集合 */
    private List<DownInfo> downInfos;
    /** 所有下载任务的 回调集合 */
    private Map<String, DownLoadObserver> observerMap;

    private volatile static DownManager instentce;

    public static synchronized DownManager getInstentce() {
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
        downQueue = new LinkedList<>();
        downInfos = new ArrayList<>();
        observerMap = new HashMap<>();
    }


    /**
     * 开始下载
     * 1、正在执行下载任务 的数目小于“最大并行下载量”则从下载队列里面取一个任务
     */
    private void startDown() {
        //判断下载列表中是否有 等待下载的任务
        DownInfo taskItem = null;
        for (DownInfo infobean : downInfos) {
            String url = infobean.getUrl();
            if (infobean.getStateInte() == DownInfo.STATUS_NOT_DOWNLOAD && observerMap.containsKey(url)) {
                infobean.setStateInte(DownInfo.STATUS_DOWNLOADING);
                taskItem = infobean;

                L.e("下载", url + "-->" + taskItem.getStateInte() + "-->" + infobean.getStateInte());
                DownLoadObserver observer = observerMap.get(url);
                observer.setDownSwitch(true);
            }
        }

        if (null == taskItem && downQueue.size() > 0){
            taskItem = downQueue.poll();
            downInfos.add(taskItem); //将执行的下载任务 添加到 “正在下载的任务 集合 downInfos 中”
        }

        DownInfo finalTaskItem = taskItem;
        if (null == finalTaskItem) return;
        String url = finalTaskItem.getUrl();

        //开始执行下载
        isRunDownLoad = true;
        DownLoadObserver observer = observerMap.get(url);

        RequestUtils.create(LoadService.class)
                .download("", url)
                .subscribeOn(Schedulers.io())
                .flatMap((Function<ResponseBody, ObservableSource<Object>>) body -> {
                    //第一次请求全部文件长度
                    long sumLength = body.contentLength();
                    File targetFile = FileUtils.createFile(url);
                    L.e("文件下载", sumLength + "--》" + url);

                    finalTaskItem.setCountLength(sumLength);

                    ACache mCache = ACache.get(ConfigUtils.getAppCtx());
                    //计算各个线程下载的数据段
                    long bitLen = sumLength / THREAD_COUNT;
                    List<Observable> sources = new ArrayList<>();
                    for (int i = 0; i < THREAD_COUNT; i++) {
                        //开始位置，获取上次取消下载的进度
                        long startPosition = mCache.getAsLong(url + i + Constant.DownTherad);
                        //如果文件已经删除 则重新下载
                        if (targetFile.length() == 0) {
                            startPosition = 0L;
                            finalTaskItem.getReadLength().set(0L);
                            //删除 每个线程 已经下载的 总长度 缓存
                            mCache.remove(url + i + Constant.DownTherad);
                        }

                        //结束位置，-1是为了防止上一个线程和下一个线程重复下载衔接处数据
                        long endPosition = i + 1 < THREAD_COUNT ? (i + 1) * bitLen - 1 : (i + 1) * bitLen + THREAD_COUNT;

                        Observable service = null;
                        //判断是否下载全部文件
                        if (startPosition < endPosition) {
                            startPosition = startPosition > 0L ? startPosition + bitLen * i : bitLen * i;
                            service = download(url + i, startPosition, endPosition, url, targetFile, observer);
                        }

                        if (null != service) sources.add(service);
                    }

                    RandomAccessFile raf = new RandomAccessFile(targetFile, "rwd");
                    raf.setLength(sumLength);
                    raf.close();

                    Observable[] observables = new Observable[sources.size()];
                    return Observable.mergeArray(sources.toArray(observables));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private Observable download(@NonNull String nameKey,
                                @NonNull final long start,
                                @NonNull final long end,
                                @NonNull final String url,
                                File targetFile, DownLoadObserver observer) {

        String endStr = end == -1 ? "" : end + "";

        return RequestUtils.create(LoadService.class)
                .download("bytes=" + start + "-" + endStr, url)
                .doOnNext((ResponseBody body) -> writeCaches(nameKey, start, body.byteStream(), targetFile, observer))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 异步 写入文件
     * @param nameKey    一个下载线程的 key
     */
    private void writeCaches(String nameKey, final long start,
                             InputStream is, File saveFile,
                             DownLoadObserver observer) throws IOException {
        RandomAccessFile raf = null;
        byte[] buffer = new byte[1024 * 4];

        try {
            raf = new RandomAccessFile(saveFile, "rwd");
            // 定位该线程的下载位置
            raf.seek(start);
            L.e("下载", Thread.currentThread().getName() + "-->" + saveFile.getName() + "---" + start);

            ACache mCache = ACache.get(ConfigUtils.getAppCtx());
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
    public void addDownTask(DownInfo downInfo, DownLoadListener loadListener) {
        String url = downInfo.getUrl();
        if (TextUtils.isEmpty(url)) return;

        //获取缓存中 所有的下载任务列表 并添加到“任务队列”
        ACache mCache = ACache.get(ConfigUtils.getAppCtx());
        String jsonArray = mCache.getAsString(Constant.AllDownTask);
        if (null != jsonArray && !TextUtils.isEmpty(jsonArray)) {
            List<DownInfo> downList = GsonUtils.jsonToList(jsonArray, DownInfo.class);
            if(downQueue.addAll(downList)){
                mCache.put(Constant.AllDownTask, "");
            }
        }

        /**
         * 1、判断缓存的下载任务列表中 是否存在指定url的 下载任务
         * 2、如果存在指定 url 的下载任务，判断该任务的文件是否存在，不存在则替换 下载列表中对应的任务
         */
        boolean isTask = false; //正在下载的集合是否存在 指定的 url 下载任务
        File targetFile = FileUtils.createFile(url);
        if (downQueue.contains(downInfo)){
            isTask = true;
            int position = downQueue.indexOf(downInfo);
            if (targetFile.length() == 0) {
                downQueue.set(position, downInfo);
            } else {
                TransfmtUtils.copyProperties(downQueue.get(position), downInfo);
            }
        }

        if (downInfo.getStateInte() == DownInfo.STATUS_COMPLETE) {
            T.showLong("此任务已完成");
            downQueue.remove(downInfo);
            if (null != loadListener)
                loadListener.onProgress(downInfo.getReadLength().get(),
                        downInfo.getCountLength(),
                        downInfo.getPercent());
            return;
        } else if (downInfo.getStateInte() == DownInfo.STATUS_PAUSED || downInfo.getStateInte() == DownInfo.STATUS_NOT_DOWNLOAD) {
            T.showLong("开始下载");
            if (null != loadListener)
                loadListener.onProgress(downInfo.getReadLength().get(),
                        downInfo.getCountLength(),
                        downInfo.getPercent());

            if (!isTask) downQueue.offer(downInfo);//向下载队列添加下载任务
        }

        //回调集合 添加 数据
        if (null == observerMap.get(url)) {
            DownLoadObserver observer = new DownLoadObserver(downInfo, loadListener);
            observerMap.put(url, observer);
        }
    }

    /**
     * 缓存所有下载信息
     */
    public void saveDownInfo(DownInfo downInfo) {
        List<DownInfo> list = null;
        //获取缓存中 所有的未完成下载任务列表
        ACache mCache = ACache.get(ConfigUtils.getAppCtx());
        String jsonArray = mCache.getAsString(Constant.AllDownTask);
        if (null != jsonArray && !TextUtils.isEmpty(jsonArray)) {
            list = GsonUtils.jsonToList(jsonArray, DownInfo.class);
        }

        if (null == list) {
            list = new ArrayList<>();
            list.add(downInfo);
        } else {
            boolean isCache = false;//缓存列表中 是否存在downInfo 对象（url判断）
            for (int i = 0; i < list.size(); i++) {
                DownInfo infobean = list.get(i);
                if (infobean.equals(downInfo)) {
                    isCache = true;
                    list.set(i, downInfo);
                    break;
                }
            }

            if (!isCache) list.add(downInfo);
        }

        mCache.put(Constant.AllDownTask, GsonUtils.listToJson(list));
    }


    /**
     * 开始下载 (多线程、多任务下载)
     * todo 并行下载有问题
     */
    public synchronized void runDownTask() {
        if (downQueue.size() == 0) return;

        int num = MAX_COUNT;//添加到正在下载列表的数目
        if (isRunDownLoad) num = MAX_COUNT - downInfos.size();

        for (int i = 0; i < num; i++) {
            startDown();
        }
    }

    /**
     * 将一个下载任务从暂停状态改为 等待下载状态,并执行下载任务
     */
    public void startDown(DownInfo downInfo) {
        for (DownInfo infobean : downInfos) {
            if (infobean.getUrl().equals(downInfo.getUrl())) {
                infobean.setStateInte(DownInfo.STATUS_NOT_DOWNLOAD);
                break;
            }
        }

        startDown();
    }

    /**
     * 停止下载（暂停 or 取消）
     *
     * @param url
     */
    public void stop(@NonNull final String url, int downStatus) {
        if (observerMap.containsKey(url)) {
            DownLoadObserver observer = observerMap.get(url);
            observer.disposed();
//            observer.onError(new SocketException("" + downStatus));
        }
    }

    /**
     * 暂停所有下载任务
     */
    public void pauseAll() {
        for (String url : observerMap.keySet()) {
            //暂停下载
            stop(url, DownInfo.STATUS_PAUSED);
        }

        isRunDownLoad = false;
    }

    /**
     * 取消下载
     *
     * @param url
     */
    public void cancle(@NonNull final String url) {
        stop(url, DownInfo.STATUS_CANCEL);

        File targetFile = FileUtils.createFile(url);
        FileUtils.deleteFileSafely(targetFile);

        for (DownInfo infobean : downInfos) {
            if (infobean.getUrl().equals(url)) {
                infobean.setStateInte(DownInfo.STATUS_CANCEL);
                break;
            }
        }
    }

    /**
     * 取消所有下载任务
     */
    public void cancleAll() {
        for (DownInfo infobean : downInfos) {
            stop(infobean.getUrl(), DownInfo.STATUS_CANCEL);

            File targetFile = FileUtils.createFile(infobean.getUrl());
            FileUtils.deleteFileSafely(targetFile);
            infobean.setStateInte(DownInfo.STATUS_CANCEL);
        }

        isRunDownLoad = false;
    }

    /**
     * 清除 指定的下载任务 回调
     * @param downInfo    下载任务对象
     */
    public void removeTask(DownInfo downInfo) {
        if (null != downInfo && observerMap.get(downInfo.getUrl()) != null) {
            observerMap.remove(downInfo.getUrl());
            boolean isRemove = downInfos.remove(downInfo);

            if (downInfos.size() == 0) isRunDownLoad = false;
            L.e("清除下载完成的任务", downInfo.getUrl() + "--->" + isRemove + "-->" + Thread.currentThread().getName());
        }
    }

    /**
     * 清除下载管理器 下载信息
     */
    public void clieanDownData() {
        downQueue.clear();
        observerMap.clear();
        downInfos.clear();
        isRunDownLoad = false;
    }

    /**
     * 获取所有 未下载任务 集合
     *
     * @return
     */
    public List<DownInfo> getDownTask() {
        List<DownInfo> data = new ArrayList<>();
        data.addAll(downQueue);
        return data;
    }

    /**
     * 获取 正在下载的任务 集合
     *
     * @return
     */
    public List<DownInfo> getDownloading() {
        List<DownInfo> data = new ArrayList<>();
        data.addAll(downInfos);

        return data;
    }
}
