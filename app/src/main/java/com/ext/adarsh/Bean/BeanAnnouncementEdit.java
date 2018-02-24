package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 20-11-2017.
 */

public class BeanAnnouncementEdit implements Parcelable {


    @SerializedName("AnnouncementId")
    public  String announcementId;

    @SerializedName("AnnouncementTitle")
    public  String announcementTitle;

    @SerializedName("AnnouncementDetail")
    public  String announcementDetail;

    @SerializedName("ReferenceToPost")
    public  String referenceToPost;

    @SerializedName("Departments")
    public  String departments;

    @SerializedName("Branches")
    public  String branches;

    @SerializedName("AnnouncementDate")
    public  String announcementDate;

    @SerializedName("SchedulePublishOn")
    public  String schedulePublishOn;

    @SerializedName("PeopleId")
    public  String peopleId;

    @SerializedName("PublishFlag")
    public  String publishFlag;

    @SerializedName("DepartmentNameList")
    public  String departmentNameList;

    @SerializedName("BranchNameList")
    public  String branchNameList;

    @SerializedName("ReferenceToPostTitle")
    public  String referenceToPostTitle;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.announcementId);
        dest.writeString(this.announcementTitle);
        dest.writeString(this.announcementDetail);
        dest.writeString(this.referenceToPost);
        dest.writeString(this.departments);
        dest.writeString(this.branches);
        dest.writeString(this.announcementDate);
        dest.writeString(this.schedulePublishOn);
        dest.writeString(this.peopleId);
        dest.writeString(this.publishFlag);
        dest.writeString(this.departmentNameList);
        dest.writeString(this.branchNameList);
        dest.writeString(this.referenceToPostTitle);
    }

    public BeanAnnouncementEdit() {
    }

    protected BeanAnnouncementEdit(Parcel in) {
        this.announcementId = in.readString();
        this.announcementTitle = in.readString();
        this.announcementDetail = in.readString();
        this.referenceToPost = in.readString();
        this.departments = in.readString();
        this.branches = in.readString();
        this.announcementDate = in.readString();
        this.schedulePublishOn = in.readString();
        this.peopleId = in.readString();
        this.publishFlag = in.readString();
        this.departmentNameList = in.readString();
        this.branchNameList = in.readString();
        this.referenceToPostTitle = in.readString();
    }

    public static final Parcelable.Creator<BeanAnnouncementEdit> CREATOR = new Parcelable.Creator<BeanAnnouncementEdit>() {
        @Override
        public BeanAnnouncementEdit createFromParcel(Parcel source) {
            return new BeanAnnouncementEdit(source);
        }

        @Override
        public BeanAnnouncementEdit[] newArray(int size) {
            return new BeanAnnouncementEdit[size];
        }
    };
}
