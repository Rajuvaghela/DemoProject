package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 1/2/2018.
 */

public class BeanForwardForCommentRequestArray implements Parcelable {
    @SerializedName("ApprovalLogId")
    public  String approvalLogId;

    @SerializedName("RequestSendToId")
    public  String requestSendToId;

    @SerializedName("RequestSendToName")
    public  String requestSendToName;

    @SerializedName("RequestFromId")
    public  String requestFromId;

    @SerializedName("RequestFromName")
    public  String requestFromName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.approvalLogId);
        dest.writeString(this.requestSendToId);
        dest.writeString(this.requestSendToName);
        dest.writeString(this.requestFromId);
        dest.writeString(this.requestFromName);
    }

    public BeanForwardForCommentRequestArray() {
    }

    protected BeanForwardForCommentRequestArray(Parcel in) {
        this.approvalLogId = in.readString();
        this.requestSendToId = in.readString();
        this.requestSendToName = in.readString();
        this.requestFromId = in.readString();
        this.requestFromName = in.readString();
    }

    public static final Parcelable.Creator<BeanForwardForCommentRequestArray> CREATOR = new Parcelable.Creator<BeanForwardForCommentRequestArray>() {
        @Override
        public BeanForwardForCommentRequestArray createFromParcel(Parcel source) {
            return new BeanForwardForCommentRequestArray(source);
        }

        @Override
        public BeanForwardForCommentRequestArray[] newArray(int size) {
            return new BeanForwardForCommentRequestArray[size];
        }
    };
}
