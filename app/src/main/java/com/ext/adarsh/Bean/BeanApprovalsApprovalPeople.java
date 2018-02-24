package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 11/13/2017.
 */

public class BeanApprovalsApprovalPeople implements Parcelable {

    @SerializedName("ApprovalFromName")
    public String approvalFromName;

    @SerializedName("ApprovalFromId")
    public String approvalFromId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.approvalFromName);
        dest.writeString(this.approvalFromId);
    }

    public BeanApprovalsApprovalPeople() {
    }

    public BeanApprovalsApprovalPeople(String approvalFromId, String approvalFromName) {
        this.approvalFromName = approvalFromName;
        this.approvalFromId = approvalFromId;
    }

    protected BeanApprovalsApprovalPeople(Parcel in) {
        this.approvalFromName = in.readString();
        this.approvalFromId = in.readString();
    }

    public static final Creator<BeanApprovalsApprovalPeople> CREATOR = new Creator<BeanApprovalsApprovalPeople>() {
        @Override
        public BeanApprovalsApprovalPeople createFromParcel(Parcel source) {
            return new BeanApprovalsApprovalPeople(source);
        }

        @Override
        public BeanApprovalsApprovalPeople[] newArray(int size) {
            return new BeanApprovalsApprovalPeople[size];
        }
    };
}
