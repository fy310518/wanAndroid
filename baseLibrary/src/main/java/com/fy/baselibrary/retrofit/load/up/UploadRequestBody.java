package com.fy.baselibrary.retrofit.load.up;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * 扩展OkHttp的请求体，实现上传时的进度提示
 * Created by fangs on 2018/5/21.
 */
public class UploadRequestBody extends RequestBody {

    private File mFile;
    private static final int DEFAULT_BUFFER_SIZE = 2048;

    public UploadRequestBody(File mFile) {
        this.mFile = mFile;
    }

    private UploadOnSubscribe mUploadOnSubscribe;

    public void setUploadOnSubscribe(UploadOnSubscribe uploadOnSubscribe) {
        this.mUploadOnSubscribe = uploadOnSubscribe;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("multipart/form-data");
    }

    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);

        try {
            int read;
            while ((read = in.read(buffer)) != -1) {
                // update progress on UI thread
                if (null != mUploadOnSubscribe) mUploadOnSubscribe.onRead(read);

                sink.write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
    }
}
