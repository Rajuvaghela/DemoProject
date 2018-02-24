package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 18-11-2017.
 */

public class BeanApprovalAttachments implements Parcelable {


    @SerializedName("AttachmentId")
    public  String attachmentId;

    @SerializedName("ApprovalId")
    public  String approvalId;

    @SerializedName("FilePath")
    public  String filePath;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.attachmentId);
        dest.writeString(this.approvalId);
        dest.writeString(this.filePath);
    }

    public BeanApprovalAttachments() {
    }

    protected BeanApprovalAttachments(Parcel in) {
        this.attachmentId = in.readString();
        this.approvalId = in.readString();
        this.filePath = in.readString();
    }

    public static final Parcelable.Creator<BeanApprovalAttachments> CREATOR = new Parcelable.Creator<BeanApprovalAttachments>() {
        @Override
        public BeanApprovalAttachments createFromParcel(Parcel source) {
            return new BeanApprovalAttachments(source);
        }

        @Override
        public BeanApprovalAttachments[] newArray(int size) {
            return new BeanApprovalAttachments[size];
        }
    };
}
