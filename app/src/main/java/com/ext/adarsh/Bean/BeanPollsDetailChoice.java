package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 1/16/2018.
 */

public class BeanPollsDetailChoice implements Parcelable {

    @SerializedName("ChoiceId")
    public String choiceId;

    @SerializedName("PollId")
    public String pollId;

    @SerializedName("Choice")
    public String choice;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.choiceId);
        dest.writeString(this.pollId);
        dest.writeString(this.choice);
    }

    public BeanPollsDetailChoice() {
    }

    protected BeanPollsDetailChoice(Parcel in) {
        this.choiceId = in.readString();
        this.pollId = in.readString();
        this.choice = in.readString();
    }

    public static final Parcelable.Creator<BeanPollsDetailChoice> CREATOR = new Parcelable.Creator<BeanPollsDetailChoice>() {
        @Override
        public BeanPollsDetailChoice createFromParcel(Parcel source) {
            return new BeanPollsDetailChoice(source);
        }

        @Override
        public BeanPollsDetailChoice[] newArray(int size) {
            return new BeanPollsDetailChoice[size];
        }
    };
}
