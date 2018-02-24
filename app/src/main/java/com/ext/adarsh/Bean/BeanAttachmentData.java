package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-008 on 03-02-2018.
 */

public class BeanAttachmentData  {

    @SerializedName("MsgAttachmentId")
    public String msgAttachmentId;

    @SerializedName("MessageId")
    public String MessageId;

    @SerializedName("FilePath")
    public String FilePath;

    @SerializedName("MainFilePath")
    public String mainFilePath;

    public String getMsgAttachmentId() {
        return msgAttachmentId;
    }

    public void setMsgAttachmentId(String msgAttachmentId) {
        this.msgAttachmentId = msgAttachmentId;
    }

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String messageId) {
        MessageId = messageId;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getMainFilePath() {
        return mainFilePath;
    }

    public void setMainFilePath(String mainFilePath) {
        this.mainFilePath = mainFilePath;
    }
}
