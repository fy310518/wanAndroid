package com.fy.baselibrary.retrofit.converter.file;

import android.annotation.SuppressLint;

import com.fy.baselibrary.retrofit.load.up.UploadOnSubscribe;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.fy.baselibrary.utils.notify.L;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.ResponseBody;

/**
 * describe: 文件下载 转换器
 * Created by fangs on 2019/8/28 22:03.
 */
public class FileResponseBodyConverter {

    /**
     * 根据ResponseBody 写文件
     * @param responseBody
     * @param url
     * @param filePath   文件保存路径
     * @return
     */
    public static File saveFile(UploadOnSubscribe uploadOnSubscribe, final ResponseBody responseBody, String url, final String filePath) {
        final File tempFile = FileUtils.createTempFile(url, filePath);

        File file = null;
        try {
            file = writeFileToDisk(uploadOnSubscribe, responseBody, tempFile.getAbsolutePath());

            int FileDownStatus = SpfAgent.init("").getInt(file.getName() + Constant.FileDownStatus);
            if (FileDownStatus == 4) {
                boolean renameSuccess = FileUtils.reNameFile(url, tempFile.getPath());
                return FileUtils.getFile(url, filePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * 单线程 断点下载
     * @param uploadOnSubscribe
     * @param responseBody
     * @param filePath
     * @return
     * @throws IOException
     */
    @SuppressLint("DefaultLocale")
    public static File writeFileToDisk(UploadOnSubscribe uploadOnSubscribe, ResponseBody responseBody, String filePath) throws IOException {
        long totalByte = responseBody.contentLength();

        L.e("fy_file_FileDownInterceptor", totalByte + "---" + Thread.currentThread().getName());

        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        } else {
            uploadOnSubscribe.setmSumLength(file.length() + totalByte);
            uploadOnSubscribe.onRead(file.length());
        }


        SpfAgent.init("").saveInt(file.getName() + Constant.FileDownStatus, 1).commit(false);//正在下载

        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
        long tempFileLen = file.length();
        randomAccessFile.seek(tempFileLen);

        byte[] buffer = new byte[1024 * 4];
        InputStream is = responseBody.byteStream();

        long downloadByte = 0;
        while (true) {
            int len = is.read(buffer);
            if (len == -1) {//下载完成
                uploadOnSubscribe.clean();

                SpfAgent.init("").saveInt(file.getName() + Constant.FileDownStatus, 4).commit(false);//下载完成
                break;
            }

            int FileDownStatus = SpfAgent.init("").getInt(file.getName() + Constant.FileDownStatus);
            if (FileDownStatus == 2 || FileDownStatus == 3) break;//暂停或者取消 停止下载

            randomAccessFile.write(buffer, 0, len);
            downloadByte += len;

            uploadOnSubscribe.onRead(len);
        }

        is.close();
        randomAccessFile.close();

        return file;
    }

}
