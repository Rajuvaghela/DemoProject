package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ExT-Emp-001 on 13-11-2017.
 */

public class BeanAnnoucementDetail implements Parcelable {


    @SerializedName("AnnouncementId")
    public String announcementId;


    @SerializedName("AnnouncementTitle")
    public String announcementTitle;


    @SerializedName("AnnouncementDetail")
    public String announcementDetail;


    @SerializedName("AnnouncementDate")
    public String announcementDate;


    @SerializedName("DepartmentList")
    public String departmentList;


    @SerializedName("BranchList")
    public String branchList;


    @SerializedName("Day")
    public String day;


    @SerializedName("Month")
    public String month;


    @SerializedName("PublishFlag")
    public String publishFlag;


    @SerializedName("isAttachment")
    public String isAttachment;


    @SerializedName("ReferenceToPost")
    public String referenceToPost;


    @SerializedName("ReferenceToPostTitle")
    public String referenceToPostTitle;


    @SerializedName("PublishBy")
    public String publishBy;

    @SerializedName("DepartmentIdList")
    public String departmentIdList;

    @SerializedName("BranchIdList")
    public String branchIdList;



    @SerializedName("Announcement_Attachments_Array")
    public ArrayList<BeanAnnoucementAttchment> announcement_Attachments_Array;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.announcementId);
        dest.writeString(this.announcementTitle);
        dest.writeString(this.announcementDetail);
        dest.writeString(this.announcementDate);
        dest.writeString(this.departmentList);
        dest.writeString(this.branchList);
        dest.writeString(this.day);
        dest.writeString(this.month);
        dest.writeString(this.publishFlag);
        dest.writeString(this.isAttachment);
        dest.writeString(this.referenceToPost);
        dest.writeString(this.referenceToPostTitle);
        dest.writeString(this.publishBy);
        dest.writeString(this.departmentIdList);
        dest.writeString(this.branchIdList);
        dest.writeTypedList(this.announcement_Attachments_Array);
    }

    public BeanAnnoucementDetail() {
    }

    protected BeanAnnoucementDetail(Parcel in) {
        this.announcementId = in.readString();
        this.announcementTitle = in.readString();
        this.announcementDetail = in.readString();
        this.announcementDate = in.readString();
        this.departmentList = in.readString();
        this.branchList = in.readString();
        this.day = in.readString();
        this.month = in.readString();
        this.publishFlag = in.readString();
        this.isAttachment = in.readString();
        this.referenceToPost = in.readString();
        this.referenceToPostTitle = in.readString();
        this.publishBy = in.readString();
        this.departmentIdList = in.readString();
        this.branchIdList = in.readString();
        this.announcement_Attachments_Array = in.createTypedArrayList(BeanAnnoucementAttchment.CREATOR);
    }

    public static final Parcelable.Creator<BeanAnnoucementDetail> CREATOR = new Parcelable.Creator<BeanAnnoucementDetail>() {
        @Override
        public BeanAnnoucementDetail createFromParcel(Parcel source) {
            return new BeanAnnoucementDetail(source);
        }

        @Override
        public BeanAnnoucementDetail[] newArray(int size) {
            return new BeanAnnoucementDetail[size];
        }
    };
}
