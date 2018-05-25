package wanandroid.fy.com.api;

import android.support.annotation.NonNull;

import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.upload.UpLoadCallBack;
import com.fy.baselibrary.retrofit.upload.UploadOnSubscribe;
import com.fy.baselibrary.retrofit.upload.UploadRequestBody;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.L;

import org.reactivestreams.Subscriber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
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


    public Observable download(@NonNull final long start, @NonNull final long end,
                               @NonNull final String url,
                               final File file,
                               final Observer subscriber) {
        String str = "";
        if (end == -1) {
            str = "";
        } else {
            str = end + "";
        }

        return RequestUtils.create(ApiService.class)
                .download("bytes=" + start + "-" + str, url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(body -> body)
                .observeOn(Schedulers.computation())
                .doOnNext(body -> {
                    //第一次请求全部文件长度
                    if (end == -1) {
                        try {
                            RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
                            randomFile.setLength(body.contentLength());
                            long one = body.contentLength() / 3;

                            download(0, one, url, file, subscriber)
                                    .mergeWith(download(one, one * 2, url, file, subscriber))
                                    .mergeWith(download(one * 2, body.contentLength(), url, file, subscriber))
                                    .subscribe(subscriber);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        writeCaches(start, end, body.byteStream(), file);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 写入文件
     *
     * @param file
     * @throws IOException
     */
    public void writeCaches(final long start, @NonNull final long end,
                            InputStream inputStream, File file) {

        byte[] buffer = new byte[1024 * 4];
        RandomAccessFile randomAccessFile = null;
        FileChannel channelOut = null;

        try {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            long allLength = 0 == info.getCountLength() ? responseBody.contentLength() : info.getReadLength() + responseBody
                    .contentLength();

            randomAccessFile = new RandomAccessFile(file, "rwd");
            channelOut = randomAccessFile.getChannel();
            MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                    info.getReadLength(), allLength - info.getReadLength());

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                mappedBuffer.put(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inputStream) inputStream.close();
                if (null != channelOut) channelOut.close();
                if (null != randomAccessFile) randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
