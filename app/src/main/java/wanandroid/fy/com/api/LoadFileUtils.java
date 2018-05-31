package wanandroid.fy.com.api;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fy.baselibrary.application.BaseApp;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.load.UpLoadCallBack;
import com.fy.baselibrary.retrofit.load.up.UploadOnSubscribe;
import com.fy.baselibrary.retrofit.load.up.UploadRequestBody;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.SpfUtils;
import com.fy.baselibrary.utils.cache.ACache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * retrofit2 上传、下载文件工具类
 * Created by fangs on 2018/5/17.
 */
public class LoadFileUtils {

    private LoadFileUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 把File集合转化成MultipartBody.Part 集合（retrofit 多文件文件上传）
     *
     * @param files
     * @return
     */
    public static List<MultipartBody.Part> fileToMultipartBodyParts(List<String> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (String path : files) {
            File file = new File(path);//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件

            String fileStr = path.substring(path.lastIndexOf(".") + 1);

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + fileStr), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("files", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

    /**
     * 把File集合转化成MultipartBody.Part 集合（retrofit 多文件文件上传）
     *
     * @param files
     * @return
     */
    public static List<MultipartBody.Part> fileToMultipartBodyPart(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件
            String path = file.getPath();
            String fileStr = path.substring(path.lastIndexOf(".") + 1);

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + fileStr), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("files", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

    /**
     * 用于把File 集合对象转化成MultipartBody （retrofit 多文件文件上传）
     *
     * @param files
     * @return
     */
    public static MultipartBody filesToMultipartBody(List<String> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (String path : files) {
            File file = new File(path);//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件

            String fileStr = path.substring(path.lastIndexOf(".") + 1);

            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("file", file.getName(), requestBody);
        }

        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();

        return multipartBody;
    }


    /**
     * 上传多个文件
     */
    public static Observable<Object> uploadFiles(List<File> files, ApiService apiService) {
//        总长度
        long sumLength = 0l;
        for (File file : files) sumLength += file.length();

//      进度Observable
        UploadOnSubscribe uploadOnSubscribe = new UploadOnSubscribe(sumLength);
        Observable<Integer> progressObservale = Observable.create(uploadOnSubscribe);

        ArrayList<MultipartBody.Part> fileParts = new ArrayList<>();
        for (File file : files) {
            UploadRequestBody uploadRequestBody = new UploadRequestBody(file);
//          设置进度监听
            uploadRequestBody.setUploadOnSubscribe(uploadOnSubscribe);

            fileParts.add(MultipartBody.Part.createFormData("upload", file.getName(), uploadRequestBody));
        }

        Observable uploadFile = apiService.uploadFile2(fileParts);

        return Observable.merge(progressObservale, uploadFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 单线程下载文件（带进度回调）
     *
     * @param url
     * @param callBack
     */
    public static void downLoadFile(String url, UpLoadCallBack callBack) {
        RequestUtils.create(ApiService.class)
                .downloadFile(url)
                .map((Function<ResponseBody, Object>) body -> {
                    long contLen = body.contentLength();
                    callBack.setmSumLength(contLen);
                    return writeResponseBodyToDisk(url, body.byteStream(), contLen, callBack);
                })
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribeOn(Schedulers.io())//指定的是上游发送事件的线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    /**
     * 下载文件 将输入流保存为文件
     *
     * @param inputStream
     * @param fileSize
     * @return
     */
    public static boolean writeResponseBodyToDisk(String url, InputStream inputStream, long fileSize, UpLoadCallBack subscribe) {
        String fileName = url.substring(url.lastIndexOf("/"));
        File futureStudioIconFile = new File(FileUtils.getPath("down"), fileName);

//        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            byte[] fileReader = new byte[4096];
//            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;
//            inputStream = body.byteStream();
            outputStream = new FileOutputStream(futureStudioIconFile);

            while (true) {
                int read = inputStream.read(fileReader);

                if (read == -1) break;

                outputStream.write(fileReader, 0, read);

                fileSizeDownloaded += read;

                L.e("文件下载", "file download: " + fileSizeDownloaded + " of " + fileSize);
                if (null != subscribe) subscribe.onRead(fileSizeDownloaded);
            }

            outputStream.flush();
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (null != inputStream) inputStream.close();
                if (null != outputStream) outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * rxJava 多线程 断点续传
     *
     * @param url
     * @param subscriber
     * @return
     */
    public static void downFile(@NonNull final String url, UpLoadCallBack subscriber) {
        RequestUtils.create(ApiService.class)
                .download("", url)
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<ResponseBody, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(ResponseBody body) throws Exception {
                        //第一次请求全部文件长度
                        long sumLength = body.contentLength();

                        File targetFile = FileUtils.createFile(url);

                        ACache mCache = ACache.get(BaseApp.getAppCtx());
                        //计算各个线程下载的数据段
                        long bitLen = sumLength / Constant.THREAD_COUNT;
                        List<Observable> sources = new ArrayList<>();
                        for (int i = 0; i < Constant.THREAD_COUNT; i++){
                            Observable servece = null;

                            //开始位置，获取上次取消下载的进度
                            long startPosition = mCache.getAsLong(url + i + Constant.DownTherad);
                            //如果文件已经删除 则从头开始下载
                            if (targetFile.length() == 0){
                                startPosition = 0L;
                                subscriber.loaded.set(0L);
                                //删除 每个线程 已经下载的 总长度 缓存
                                mCache.remove(url + i + Constant.DownTherad);
                                mCache.remove(url  + Constant.DownTask);
                            }

                            //结束位置，-1是为了防止上一个线程和下一个线程重复下载衔接处数据
                            long endPosition = i + 1 < Constant.THREAD_COUNT ? (i + 1) * bitLen - 1 : (i + 1) * bitLen + 1;

                            //判断是否下载全部文件
                            if (startPosition < endPosition) {
                                startPosition = startPosition > 0L ? startPosition + bitLen * i : bitLen * i;
                                servece = download(url + i, startPosition, endPosition, url, targetFile, subscriber);
                            }
                            
                            if (null != servece) sources.add(servece);
                        }

                        RandomAccessFile raf = new RandomAccessFile(targetFile, "rwd");
                        raf.setLength(sumLength);
                        raf.close();

                        subscriber.setmSumLength(sumLength);
                        Observable[] observables = new Observable[sources.size()];
                        return Observable.mergeArray(sources.toArray(observables));//todo 被观察者 不到三个的时候 没有测试
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public static Observable download(@NonNull String nameKey,
                                      @NonNull final long start,
                                      @NonNull final long end,
                                      @NonNull final String url,
                                      File targetFile, UpLoadCallBack subscriber) {

        String str = end == -1 ? "" : end + "";

        return RequestUtils.create(ApiService.class)
                .download("bytes=" + start + "-" + str, url)
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribeOn(Schedulers.io())
                .doOnNext((ResponseBody body) -> writeCaches(nameKey, start, body.byteStream(), targetFile, subscriber))
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
                                   UpLoadCallBack subscriber) throws IOException {
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
                subscriber.onRead(len);

                loaded += len;
                //缓存当前线程 下载内容 总长度
                mCache.put(nameKey + Constant.DownTherad, loaded);
                L.e("Thread", Thread.currentThread().getName() + "-->" + Thread.currentThread().getId());
            }
        } finally {
            if (null != is) is.close();
            if (null != raf) raf.close();
        }
    }
}
