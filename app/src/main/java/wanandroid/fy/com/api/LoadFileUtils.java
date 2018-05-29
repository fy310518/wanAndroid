package wanandroid.fy.com.api;

import android.support.annotation.NonNull;

import com.fy.baselibrary.application.BaseApp;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.upload.UpLoadCallBack;
import com.fy.baselibrary.retrofit.upload.UploadOnSubscribe;
import com.fy.baselibrary.retrofit.upload.UploadRequestBody;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.L;

import org.reactivestreams.Subscriber;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.ProtocolException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
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
                .map(body -> body)
                .flatMap(new Function<ResponseBody, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(ResponseBody body) throws Exception {

                        //第一次请求全部文件长度
                        long len = body.contentLength();
                        L.e("Thread", len + "---》" + Thread.currentThread().getName() + "-->" + Thread.currentThread().getId());
                        File targetFile = FileUtils.createFile(url);
                        RandomAccessFile raf = new RandomAccessFile(targetFile, "rwd");
                        raf.setLength(len);
                        raf.close();

                        subscriber.setmSumLength(len);
                        long one = len / 3;
                        Observable servece1 = download(0, one, url, targetFile, subscriber);
                        Observable servece2 = download(one, one * 2, url, targetFile, subscriber);
                        Observable servece3 = download(one * 2, len, url, targetFile, subscriber);

                        return Observable.merge(servece1, servece2, servece3);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public static Observable download(@NonNull final long start,
                                      @NonNull final long end,
                                      @NonNull final String url,
                                      File targetFile, UpLoadCallBack subscriber) {

        String str = end == -1 ? "" : end + "";

        return RequestUtils.create(ApiService.class)
                .download("bytes=" + start + "-" + str, url)
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribeOn(Schedulers.io())
                .map(body -> body)
                .doOnNext((ResponseBody body) -> writeCaches(start, body.byteStream(), targetFile, subscriber));
    }

    /**
     * 异步 写入文件
     *
     * @param saveFile
     */
    public static void writeCaches(@NonNull final long start, InputStream is, File saveFile, UpLoadCallBack subscriber) {
        RandomAccessFile raf = null;
        byte[] buffer = new byte[1024 * 4];

        try {
            raf = new RandomAccessFile(saveFile, "rwd");
            // 定位该线程的下载位置
            raf.seek(start);
            L.e("Thread", start + "---》" + Thread.currentThread().getName() + "-->" + Thread.currentThread().getId());

            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                raf.write(buffer, 0, len);

                subscriber.onRead(len);
            }

        } catch (IOException e){
            subscriber.onError(e);
        } finally {
            try {
                if (null != is) is.close();
                if (null != raf) raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
