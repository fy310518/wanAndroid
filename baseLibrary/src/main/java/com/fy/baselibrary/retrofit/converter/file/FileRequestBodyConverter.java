package com.fy.baselibrary.retrofit.converter.file;

import com.fy.baselibrary.retrofit.load.up.UpLoadUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * 上传文件 请求转换器
 * Created by fangs on 2018/11/12.
 */
public class FileRequestBodyConverter implements Converter<List<String>, RequestBody> {

    @Override
    public RequestBody convert(List<String> files) throws IOException {
        return UpLoadUtils.filesToMultipartBody(files);
    }
}
