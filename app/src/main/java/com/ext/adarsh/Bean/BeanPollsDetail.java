package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ExT-Emp-011 on 1/16/2018.
 */

public class BeanPollsDetail implements Parcelable {

    @SerializedName("PollId")
    public String pollId;

    @SerializedName("PollQuestion")
    public String pollQuestion;

    @SerializedName("Length")
    public String length;

    @SerializedName("Startdate")
    public String startdate;

    @SerializedName("Enddate")
    public String enddate;

    @SerializedName("isShowResult")
    public String IsShowResult;

    @SerializedName("Departments")
    public String departments;

    @SerializedName("Branches")
    public String branches;

    @SerializedName("CreatedBy")
    public String createdBy;

    @SerializedName("PublishFlag")
    public String publishFlag;

    @SerializedName("DepartmentNameList")
    public String departmentNameList;

    @SerializedName("BranchNameList")
    public String branchNameList;

    @SerializedName("DeptFlag")
    public String deptFlag;

    @SerializedName("BranchFlag")
    public String branchFlag;

    @SerializedName("Polls_Choice_Array")
    public ArrayList<BeanPollsDetailChoice> polls_Choice_Array;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pollId);
        dest.writeString(this.pollQuestion);
        dest.writeString(this.length);
        dest.writeString(this.startdate);
        dest.writeString(this.enddate);
        dest.writeString(this.IsShowResult);
        dest.writeString(this.departments);
        dest.writeString(this.branches);
        dest.writeString(this.createdBy);
        dest.writeString(this.publishFlag);
        dest.writeString(this.departmentNameList);
        dest.writeString(this.branchNameList);
        dest.writeString(this.deptFlag);
        dest.writeString(this.branchFlag);
        dest.writeTypedList(this.polls_Choice_Array);
    }

    public BeanPollsDetail() {
    }

    protected BeanPollsDetail(Parcel in) {
        this.pollId = in.readString();
        this.pollQuestion = in.readString();
        this.length = in.readString();
        this.startdate = in.readString();
        this.enddate = in.readString();
        this.IsShowResult = in.readString();
        this.departments = in.readString();
        this.branches = in.readString();
        this.createdBy = in.readString();
        this.publishFlag = in.readString();
        this.departmentNameList = in.readString();
        this.branchNameList = in.readString();
        this.deptFlag = in.readString();
        this.branchFlag = in.readString();
        this.polls_Choice_Array = in.createTypedArrayList(BeanPollsDetailChoice.CREATOR);
    }

    public static final Parcelable.Creator<BeanPollsDetail> CREATOR = new Parcelable.Creator<BeanPollsDetail>() {
        @Override
        public BeanPollsDetail createFromParcel(Parcel source) {
            return new BeanPollsDetail(source);
        }

        @Override
        public BeanPollsDetail[] newArray(int size) {
            return new BeanPollsDetail[size];
        }
    };
}
