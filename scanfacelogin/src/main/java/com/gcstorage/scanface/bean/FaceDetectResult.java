package com.gcstorage.scanface.bean;

import java.io.Serializable;

/**
 * Created by zjs on 2018/12/19.
 */

public class FaceDetectResult implements Serializable {
    private boolean isSuccess;
    private boolean updatePic;
    private String resultMessage;
    private String key;

    public FaceDetectResult() {
    }

    public FaceDetectResult(boolean isSuccess, String resultMessage) {
        this.isSuccess = isSuccess;
        this.resultMessage = resultMessage;
    }

    public boolean isUpdatePic() {
        return updatePic;
    }

    public void setUpdatePic(boolean updatePic) {
        this.updatePic = updatePic;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    @Override
    public String toString() {
        return "FaceDetectResult{" +
                "isSuccess=" + isSuccess +
                ", updatePic=" + updatePic +
                ", resultMessage='" + resultMessage + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
