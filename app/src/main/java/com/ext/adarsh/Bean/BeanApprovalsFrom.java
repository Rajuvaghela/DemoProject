package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 11/11/2017.
 */

public class BeanApprovalsFrom implements Parcelable {

    @SerializedName("Id")
    public String id;

    @SerializedName("ApprovalId")
    public String approvalId;

    @SerializedName("ApprovalFromId")
    public String approvalFromId;

    @SerializedName("ApprovalFromName")
    public String approvalFromName;

    @SerializedName("StatusFlag")
    public String statusFlag;

    @SerializedName("ProfileImage")
    public String profileImage;

    @SerializedName("PeopleFullName")
    public String peopleFullName;

    @SerializedName("Designation")
    public String designation;

    @SerializedName("Priority")
    public String priority;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.approvalId);
        dest.writeString(this.approvalFromId);
        dest.writeString(this.approvalFromName);
        dest.writeString(this.statusFlag);
        dest.writeString(this.profileImage);
        dest.writeString(this.peopleFullName);
        dest.writeString(this.designation);
        dest.writeString(this.priority);
    }

    public BeanApprovalsFrom() {
    }

    protected BeanApprovalsFrom(Parcel in) {
        this.id = in.readString();
        this.approvalId = in.readString();
        this.approvalFromId = in.readString();
        this.approvalFromName = in.readString();
        this.statusFlag = in.readString();
        this.profileImage = in.readString();
        this.peopleFullName = in.readString();
        this.designation = in.readString();
        this.priority = in.readString();
    }

    public static final Parcelable.Creator<BeanApprovalsFrom> CREATOR = new Parcelable.Creator<BeanApprovalsFrom>() {
        @Override
        public BeanApprovalsFrom createFromParcel(Parcel source) {
            return new BeanApprovalsFrom(source);
        }

        @Override
        public BeanApprovalsFrom[] newArray(int size) {
            return new BeanApprovalsFrom[size];
        }
    };
}
