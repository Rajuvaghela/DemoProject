package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BeanTODO implements Parcelable {

    @SerializedName("TaskDoId")
    public String taskDoId;

    @SerializedName("NameOfToDo")
    public String nameOfToDo;

    @SerializedName("PersonId")
    public String personId;

    @SerializedName("Name")
    public String name;

    @SerializedName("DueDate")
    public String dueDate;

    @SerializedName("Note")
    public String note;

    @SerializedName("TaskId")
    public String taskId;

    @SerializedName("StatusFlag")
    public String statusFlag;

    @SerializedName("MatchId")
    public String matchId;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("AssignById")
    public String assignById;

    @SerializedName("isPersonExit")
    public String IsPersonExit;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.taskDoId);
        dest.writeString(this.nameOfToDo);
        dest.writeString(this.personId);
        dest.writeString(this.name);
        dest.writeString(this.dueDate);
        dest.writeString(this.note);
        dest.writeString(this.taskId);
        dest.writeString(this.statusFlag);
        dest.writeString(this.matchId);
        dest.writeString(this.fullName);
        dest.writeString(this.assignById);
        dest.writeString(this.IsPersonExit);
    }

    public BeanTODO() {
    }

    protected BeanTODO(Parcel in) {
        this.taskDoId = in.readString();
        this.nameOfToDo = in.readString();
        this.personId = in.readString();
        this.name = in.readString();
        this.dueDate = in.readString();
        this.note = in.readString();
        this.taskId = in.readString();
        this.statusFlag = in.readString();
        this.matchId = in.readString();
        this.fullName = in.readString();
        this.assignById = in.readString();
        this.IsPersonExit = in.readString();
    }

    public static final Parcelable.Creator<BeanTODO> CREATOR = new Parcelable.Creator<BeanTODO>() {
        @Override
        public BeanTODO createFromParcel(Parcel source) {
            return new BeanTODO(source);
        }

        @Override
        public BeanTODO[] newArray(int size) {
            return new BeanTODO[size];
        }
    };
}
