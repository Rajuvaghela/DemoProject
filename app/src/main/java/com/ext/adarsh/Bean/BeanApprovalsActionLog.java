package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 03-12-2017.
 */

public class BeanApprovalsActionLog implements Parcelable {


    @SerializedName("ApprovalLogId")
    public String approvalLogId;

    @SerializedName("ApprovalId")
    public String approvalId;

    @SerializedName("LoginId")
    public String loginId;

    @SerializedName("RequestSendToId")
    public String requestSendToId;

    @SerializedName("RequestSendTo")
    public String requestSendTo;

    @SerializedName("Date")
    public String date;

    @SerializedName("Time")
    public String time;

    @SerializedName("LogTitle")
    public String logTitle;

    @SerializedName("FilePath")
    public String filePath;

    @SerializedName("Description")
    public String description;

    @SerializedName("TaskAction")
    public String taskAction;

    @SerializedName("ApprovalCategoryId")
    public String approvalCategoryId;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("RFlag")
    public String rFlag;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.approvalLogId);
        dest.writeString(this.approvalId);
        dest.writeString(this.loginId);
        dest.writeString(this.requestSendToId);
        dest.writeString(this.requestSendTo);
        dest.writeString(this.date);
        dest.writeString(this.time);
        dest.writeString(this.logTitle);
        dest.writeString(this.filePath);
        dest.writeString(this.description);
        dest.writeString(this.taskAction);
        dest.writeString(this.approvalCategoryId);
        dest.writeString(this.fullName);
        dest.writeString(this.rFlag);
    }

    public BeanApprovalsActionLog() {
    }

    protected BeanApprovalsActionLog(Parcel in) {
        this.approvalLogId = in.readString();
        this.approvalId = in.readString();
        this.loginId = in.readString();
        this.requestSendToId = in.readString();
        this.requestSendTo = in.readString();
        this.date = in.readString();
        this.time = in.readString();
        this.logTitle = in.readString();
        this.filePath = in.readString();
        this.description = in.readString();
        this.taskAction = in.readString();
        this.approvalCategoryId = in.readString();
        this.fullName = in.readString();
        this.rFlag = in.readString();
    }

    public static final Parcelable.Creator<BeanApprovalsActionLog> CREATOR = new Parcelable.Creator<BeanApprovalsActionLog>() {
        @Override
        public BeanApprovalsActionLog createFromParcel(Parcel source) {
            return new BeanApprovalsActionLog(source);
        }

        @Override
        public BeanApprovalsActionLog[] newArray(int size) {
            return new BeanApprovalsActionLog[size];
        }
    };
}
