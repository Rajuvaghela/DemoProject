package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 11/21/2017.
 */

public class BeanApprovalsAttachmentArray implements Parcelable {


    @SerializedName("AttachmentId")
    public String attachmentId;

    @SerializedName("ApprovalId")
    public String approvalId;


    @SerializedName("FilePath")
    public String filePath;


    @SerializedName("FileName")
    public String fileName;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.attachmentId);
        dest.writeString(this.approvalId);
        dest.writeString(this.filePath);
        dest.writeString(this.fileName);
    }

    public BeanApprovalsAttachmentArray() {
    }

    protected BeanApprovalsAttachmentArray(Parcel in) {
        this.attachmentId = in.readString();
        this.approvalId = in.readString();
        this.filePath = in.readString();
        this.fileName = in.readString();
    }

    public static final Parcelable.Creator<BeanApprovalsAttachmentArray> CREATOR = new Parcelable.Creator<BeanApprovalsAttachmentArray>() {
        @Override
        public BeanApprovalsAttachmentArray createFromParcel(Parcel source) {
            return new BeanApprovalsAttachmentArray(source);
        }

        @Override
        public BeanApprovalsAttachmentArray[] newArray(int size) {
            return new BeanApprovalsAttachmentArray[size];
        }
    };
}
