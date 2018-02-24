package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BeanApprovalsReviewPeople implements Parcelable {

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

    public BeanApprovalsReviewPeople() {
    }

    public BeanApprovalsReviewPeople(String approvalFromId, String approvalFromName) {
        this.approvalFromName = approvalFromName;
        this.approvalFromId = approvalFromId;
    }

    protected BeanApprovalsReviewPeople(Parcel in) {
        this.approvalFromName = in.readString();
        this.approvalFromId = in.readString();
    }

    public static final Creator<BeanApprovalsReviewPeople> CREATOR = new Creator<BeanApprovalsReviewPeople>() {
        @Override
        public BeanApprovalsReviewPeople createFromParcel(Parcel source) {
            return new BeanApprovalsReviewPeople(source);
        }

        @Override
        public BeanApprovalsReviewPeople[] newArray(int size) {
            return new BeanApprovalsReviewPeople[size];
        }
    };
}
