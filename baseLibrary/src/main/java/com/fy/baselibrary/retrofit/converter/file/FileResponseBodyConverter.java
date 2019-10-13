package com.fy.baselibrary.retrofit.converter.file;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import com.fy.baselibrary.retrofit.load.down.FileResponseBody;
import com.fy.baselibrary.retrofit.load.up.UploadOnSubscribe;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.notify.L;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * describe: 文件下载 转换器
 * Created by fangs on 2019/8/28 22:03.
 */
public class FileResponseBodyConverter implements Converter<ResponseBody, File> {

    //进度发射器
    static UploadOnSubscribe uploadOnSubscribe;

    @Override
    public File convert(ResponseBody value) throws IOException {
        String downUrl = null;
        try {
            //使用反射获得我们自定义的response
            Class aClass = value.getClass();
            Field field = aClass.getDeclaredField("delegate");
            field.setAccessible(true);
            ResponseBody body = (ResponseBody) field.get(value);
            if (body instanceof FileResponseBody) {
                FileResponseBody prBody = ((FileResponseBody) body);
                downUrl = prBody.getDownUrl();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        String filePath = FileUtils.folderIsExists("wanAndroid.down", 2).getPath();
        return saveFile(value, downUrl, filePath);
    }

    /**
     * 根据ResponseBody 写文件
     * @param responseBody
     * @param url
     * @param filePath   文件保存路径
     * @return
     */
    private static File saveFile(final ResponseBody responseBody, String url, final String filePath) {
        boolean downloadSuccss = false;
        final File tempFile = FileUtils.getTempFile(url, filePath);

        File file = null;
        try {
            file = writeFileToDisk(responseBody, tempFile.getAbsolutePath());
            FileUtils.reNameFile(url, tempFile.getPath());
            downloadSuccss = true;
        } catch (Exception e) {
            e.printStackTrace();
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

        return file;
    }

    @SuppressLint("DefaultLocale")
    private static File writeFileToDisk(ResponseBody responseBody, String filePath) throws IOException {
        long totalByte = responseBody.contentLength();

        L.e("fy_file_FileDownInterceptor", totalByte + "---" + Thread.currentThread().getName());

        long downloadByte = 0;
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
        long tempFileLen = file.length();
        randomAccessFile.seek(tempFileLen);

        byte[] buffer = new byte[1024 * 4];
        InputStream is = responseBody.byteStream();

        while (true) {
            int len = is.read(buffer);
            if (len == -1) {//下载完成
                break;
            }
            randomAccessFile.write(buffer, 0, len);
            downloadByte += len;
            callbackProgress(tempFileLen + downloadByte);
        }

        is.close();
        randomAccessFile.close();

        return file;
    }

    private static void callbackProgress(final long downloadByte) {
        L.e("fy_file", downloadByte + "--");

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
