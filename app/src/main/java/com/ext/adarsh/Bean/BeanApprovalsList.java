package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ExT-Emp-001 on 27-10-2017.
 */

public class BeanApprovalsList implements Parcelable {
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
    public  String statusFlag;


    @SerializedName("Approval_Attachments_Array")
    public ArrayList<BeanApprovalAttachments> approval_Attachments_Array;

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
        dest.writeTypedList(this.approval_Attachments_Array);
    }

    public BeanApprovalsList() {
    }

    protected BeanApprovalsList(Parcel in) {
        this.approvalId = in.readString();
        this.peopleId = in.readString();
        this.taskApprovalName = in.readString();
        this.note = in.readString();
        this.filePath = in.readString();
        this.approvalCategoryId = in.readString();
        this.statusFlag = in.readString();
        this.approval_Attachments_Array = in.createTypedArrayList(BeanApprovalAttachments.CREATOR);
    }

    public static final Parcelable.Creator<BeanApprovalsList> CREATOR = new Parcelable.Creator<BeanApprovalsList>() {
        @Override
        public BeanApprovalsList createFromParcel(Parcel source) {
            return new BeanApprovalsList(source);
        }

        @Override
        public BeanApprovalsList[] newArray(int size) {
            return new BeanApprovalsList[size];
        }
    };
}
