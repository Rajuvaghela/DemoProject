package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BeanTask implements Parcelable {

    @SerializedName("TaskId")
    public String taskId;

    @SerializedName("TaskTitle")
    public String taskTitle;

    @SerializedName("TaskDescription")
    public String TaskDescription;

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("MatchId")
    public String matchId;

    @SerializedName("Task_To_Do_Array")
    public ArrayList<BeanTODO> task_To_Do_Array;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.taskId);
        dest.writeString(this.taskTitle);
        dest.writeString(this.TaskDescription);
        dest.writeString(this.peopleId);
        dest.writeString(this.matchId);
        dest.writeTypedList(this.task_To_Do_Array);
    }

    public BeanTask() {
    }

    protected BeanTask(Parcel in) {
        this.taskId = in.readString();
        this.taskTitle = in.readString();
        this.TaskDescription = in.readString();
        this.peopleId = in.readString();
        this.matchId = in.readString();
        this.task_To_Do_Array = in.createTypedArrayList(BeanTODO.CREATOR);
    }

    public static final Parcelable.Creator<BeanTask> CREATOR = new Parcelable.Creator<BeanTask>() {
        @Override
        public BeanTask createFromParcel(Parcel source) {
            return new BeanTask(source);
        }

        @Override
        public BeanTask[] newArray(int size) {
            return new BeanTask[size];
        }
    };
}
