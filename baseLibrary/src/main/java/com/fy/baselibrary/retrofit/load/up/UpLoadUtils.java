package com.fy.baselibrary.retrofit.load.up;

import com.fy.baselibrary.retrofit.load.LoadService;
import com.fy.baselibrary.retrofit.load.up.UploadOnSubscribe;
import com.fy.baselibrary.retrofit.load.up.UploadRequestBody;

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
    public static Observable<Object> uploadFiles(List<File> files, LoadService apiService) {
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

        Observable<Object> uploadFile = apiService.uploadFile2(fileParts);

        return Observable.merge(progressObservale, uploadFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
