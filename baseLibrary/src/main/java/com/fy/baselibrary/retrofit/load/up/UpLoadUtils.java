package com.fy.baselibrary.retrofit.load.up;

import com.fy.baselibrary.retrofit.load.LoadService;
import com.fy.baselibrary.utils.L;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * retrofit2 上传文件工具类
 * Created by fangs on 2018/5/17.
 */
public class UpLoadUtils {

    private UpLoadUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 把 File集合转化成 MultipartBody.Part集合
     * @param files File列表或者 File 路径列表
     * @param <T> 泛型
     * @return MultipartBody.Part列表（retrofit 多文件文件上传）
     */
    public static <T> List<MultipartBody.Part> filesToMultipartBodyPart(List<T> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());

        File file;
        for (T t : files) {//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件

            if (t instanceof File) file = (File) t;
            else if (t instanceof String) file = new File((String) t);//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件
            else break;

            String path = file.getPath();
            String fileStr = path.substring(path.lastIndexOf(".") + 1);

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + fileStr), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("files", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

    /**
     * 用于把 File集合 或者 File路径集合 转化成 MultipartBody
     * @param files File列表或者 File 路径列表
     * @param <T> 泛型（File 或者 String）
     * @return MultipartBody（retrofit 多文件文件上传）
     */
    public static <T> MultipartBody filesToMultipartBody(List<T> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        File file;
        for (T t : files) {
            if (t instanceof File) file = (File) t;
            else if (t instanceof String) file = new File((String) t);//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件
            else break;

            // TODO 为了简单起见，没有判断file的类型
            FileProgressRequestBody requestBody = new FileProgressRequestBody(file, "multipart/form-data");
            builder.addFormDataPart("file", file.getName(), requestBody);
        }

        builder.setType(MultipartBody.FORM);

        return builder.build();
    }

    /**
     * 上传多个文件
     */
    public static Observable<Object> uploadFiles(List<String> files, LoadService apiService) {
//        总长度
        long sumLength = 0L;

//      进度Observable
        UploadOnSubscribe uploadOnSubscribe = new UploadOnSubscribe();
        Observable<Double> progressObservale = Observable.create(uploadOnSubscribe);

        ArrayList<MultipartBody.Part> fileParts = new ArrayList<>();
        File file;
        for (String path : files) {
            file = new File(path);
            sumLength += file.length();

            FileProgressRequestBody requestBody = new FileProgressRequestBody(file, "multipart/form-data", uploadOnSubscribe);
            fileParts.add(MultipartBody.Part.createFormData("upload", file.getName(), requestBody));
        }

        uploadOnSubscribe.setmSumLength(sumLength);

        Observable<Object> uploadFile = apiService.uploadFile2(fileParts);

        return Observable.merge(progressObservale, uploadFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
