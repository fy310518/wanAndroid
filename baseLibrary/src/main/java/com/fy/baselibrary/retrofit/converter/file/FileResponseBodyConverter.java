package com.fy.baselibrary.retrofit.converter.file;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import com.fy.baselibrary.retrofit.load.DownLoadFileType;
import com.fy.baselibrary.retrofit.load.up.UploadOnSubscribe;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.notify.L;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * describe: 文件下载 转换器
 * Created by fangs on 2019/8/28 22:03.
 */
public class FileResponseBodyConverter implements Converter<ResponseBody, Object> {

    //进度发射器
    static UploadOnSubscribe uploadOnSubscribe;
    Annotation[] annotations;

    public FileResponseBodyConverter(Annotation[] annotations) {
        this.annotations = annotations;
        for( Annotation annotation : annotations) {
                L.e("文件下载", annotation.toString());
        }
    }

    @Override
    public Object convert(ResponseBody value) throws IOException {
        saveFile(value, "", "");
        return "200";
    }

    private static void saveFile(final ResponseBody responseBody, String url, final String filePath) {
        boolean downloadSuccss = true;
        final File tempFile = FileUtils.getTempFile(url, filePath);
        try {
            writeFileToDisk(responseBody, tempFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            downloadSuccss = false;
        }

        if (downloadSuccss) {
            final boolean renameSuccess = tempFile.renameTo(new File(filePath));
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (null != uploadOnSubscribe && renameSuccess) {
                        uploadOnSubscribe.clean();
                    }
                }
            });
        }
    }

    @SuppressLint("DefaultLocale")
    private static void writeFileToDisk(ResponseBody responseBody, String filePath) throws IOException {
        long totalByte = responseBody.contentLength();
        long downloadByte = 0;
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        byte[] buffer = new byte[1024 * 4];
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
        long tempFileLen = file.length();
        randomAccessFile.seek(tempFileLen);
        while (true) {
            int len = responseBody.byteStream().read(buffer);
            if (len == -1) {
                break;
            }
            randomAccessFile.write(buffer, 0, len);
            downloadByte += len;
            callbackProgress(tempFileLen + totalByte, tempFileLen + downloadByte);
        }
        randomAccessFile.close();
    }

    private static void callbackProgress(final long totalByte, final long downloadByte) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                if (null != uploadOnSubscribe) {
                    uploadOnSubscribe.onRead(downloadByte);
                }
            }
        });
    }
}
