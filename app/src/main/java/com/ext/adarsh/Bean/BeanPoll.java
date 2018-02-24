package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ExT-Emp-011 on 10/3/2017.
 */

public class BeanPoll implements Parcelable {

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
    public String isShowResult;

    @SerializedName("Departments")
    public String departments;

    @SerializedName("Branches")
    public String branches;

    @SerializedName("CreatedBy")
    public String createdBy;

    @SerializedName("PublishFlag")
    public String publishFlag;

    @SerializedName("CreatedByName")
    public String createdByName;

    @SerializedName("TotalVotes")
    public String totalVotes;

    @SerializedName("isPollActive")
    public String isPollActive;

    @SerializedName("isVoted")
    public String isVoted;

    @SerializedName("Polls_Choice_Array")
    public ArrayList<BeanPollsChoice> polls_Choice_Array;


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
        dest.writeString(this.isShowResult);
        dest.writeString(this.departments);
        dest.writeString(this.branches);
        dest.writeString(this.createdBy);
        dest.writeString(this.publishFlag);
        dest.writeString(this.createdByName);
        dest.writeString(this.totalVotes);
        dest.writeString(this.isPollActive);
        dest.writeString(this.isVoted);
        dest.writeTypedList(this.polls_Choice_Array);
    }

    public BeanPoll() {
    }

    protected BeanPoll(Parcel in) {
        this.pollId = in.readString();
        this.pollQuestion = in.readString();
        this.length = in.readString();
        this.startdate = in.readString();
        this.enddate = in.readString();
        this.isShowResult = in.readString();
        this.departments = in.readString();
        this.branches = in.readString();
        this.createdBy = in.readString();
        this.publishFlag = in.readString();
        this.createdByName = in.readString();
        this.totalVotes = in.readString();
        this.isPollActive = in.readString();
        this.isVoted = in.readString();
        this.polls_Choice_Array = in.createTypedArrayList(BeanPollsChoice.CREATOR);
    }

    public static final Parcelable.Creator<BeanPoll> CREATOR = new Parcelable.Creator<BeanPoll>() {
        @Override
        public BeanPoll createFromParcel(Parcel source) {
            return new BeanPoll(source);
        }

        @Override
        public BeanPoll[] newArray(int size) {
            return new BeanPoll[size];
        }
    };
}
