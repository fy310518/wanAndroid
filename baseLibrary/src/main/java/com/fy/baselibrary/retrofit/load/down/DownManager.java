package com.fy.baselibrary.retrofit.load.down;

import android.support.annotation.NonNull;

import com.fy.baselibrary.application.BaseApp;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.load.LoadService;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.GsonUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.cache.ACache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /*记录下载数据*/
    private List<DownInfo> downInfos;
    /*回调sub队列*/
    private Map<String, DownLoadObserver> observerMap;

    public volatile static DownManager instentce;

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
        downInfos   = new ArrayList<>();
        observerMap = new HashMap<>();
    }

    /**
     * 开始下载
     */
    public void startDown(DownInfo info, DownLoadListener loadListener) {
        //正在下载不处理
        if (null == info || observerMap.get(info.getUrl()) != null) return;
        String url = info.getUrl();

        DownLoadObserver observer = new DownLoadObserver(info, loadListener);
        observerMap.put(url, observer);
        downInfos.add(info);

        RequestUtils.create(LoadService.class)
                .download("", url)
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<ResponseBody, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(ResponseBody body) throws Exception {
                        //第一次请求全部文件长度
                        long sumLength = body.contentLength();
                        info.setCountLength(sumLength);
                        File targetFile = FileUtils.createFile(url);

                        ACache mCache = ACache.get(BaseApp.getAppCtx());
                        //计算各个线程下载的数据段
                        long bitLen = sumLength / Constant.THREAD_COUNT;
                        List<Observable> sources = new ArrayList<>();
                        for (int i = 0; i < Constant.THREAD_COUNT; i++){
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
                            long endPosition = i + 1 < Constant.THREAD_COUNT ? (i + 1) * bitLen - 1 : (i + 1) * bitLen + 1;

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

    public static Observable download(@NonNull String nameKey,
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
    public static void writeCaches(@NonNull String nameKey, @NonNull final long start,
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
                observer.onRead(len);

                loaded += len;
                //缓存当前线程 下载内容 总长度
                mCache.put(nameKey + Constant.DownTherad, loaded);
            }
        } finally {
            if (null != is) is.close();
            if (null != raf) raf.close();
        }
    }

    /**
     * 缓存所有下载信息
     */
    public void saveDownInfo(){
        if (null != downInfos && downInfos.size() > 0) {
            ACache mCache = ACache.get(BaseApp.getAppCtx());
            mCache.put(Constant.AllDownTask, GsonUtils.listToJson(downInfos));
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
            observerMap.remove(url);
        }
    }
}
