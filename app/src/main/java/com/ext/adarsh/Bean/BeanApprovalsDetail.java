package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
public class BeanApprovalsDetail implements Parcelable {


    @SerializedName("ApprovalId")
    public String approvalId;

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("TaskApprovalName")
    public String taskApprovalName;

    @SerializedName("Note")
    public String note;

    @SerializedName("FilePath")
    public String filePath;

    @SerializedName("ApprovalCategoryId")
    public String approvalCategoryId;

    @SerializedName("StatusFlag")
    public String statusFlag;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("FileName")
    public String fileName;

    @SerializedName("ProfileImage")
    public String profileImage;

    @SerializedName("Designation")
    public String designation;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.approvalId);
        dest.writeString(this.peopleId);
        dest.writeString(this.taskApprovalName);
        dest.writeString(this.note);
        dest.writeString(this.filePath);
        dest.writeString(this.approvalCategoryId);
        dest.writeString(this.statusFlag);
        dest.writeString(this.fullName);
        dest.writeString(this.fileName);
        dest.writeString(this.profileImage);
        dest.writeString(this.designation);
    }

    public BeanApprovalsDetail() {
    }

    protected BeanApprovalsDetail(Parcel in) {
        this.approvalId = in.readString();
        this.peopleId = in.readString();
        this.taskApprovalName = in.readString();
        this.note = in.readString();
        this.filePath = in.readString();
        this.approvalCategoryId = in.readString();
        this.statusFlag = in.readString();
        this.fullName = in.readString();
        this.fileName = in.readString();
        this.profileImage = in.readString();
        this.designation = in.readString();
    }

    public static final Parcelable.Creator<BeanApprovalsDetail> CREATOR = new Parcelable.Creator<BeanApprovalsDetail>() {
        @Override
        public BeanApprovalsDetail createFromParcel(Parcel source) {
            return new BeanApprovalsDetail(source);
        }

        @Override
        public BeanApprovalsDetail[] newArray(int size) {
            return new BeanApprovalsDetail[size];
        }
    };
}
