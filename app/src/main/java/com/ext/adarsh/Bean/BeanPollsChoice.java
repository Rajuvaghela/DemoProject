package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 10/3/2017.
 */

public class BeanPollsChoice implements Parcelable {

    @SerializedName("ChoiceId")
    public String choiceId;

    @SerializedName("PollId")
    public String pollId;

    @SerializedName("Choice")
    public String choice;

    @SerializedName("ChoicePer")
    public String choicePer;


    @SerializedName("ChoiceCount")
    public String choiceCount;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.choiceId);
        dest.writeString(this.pollId);
        dest.writeString(this.choice);
        dest.writeString(this.choicePer);
        dest.writeString(this.choiceCount);
    }

    public BeanPollsChoice() {
    }

    protected BeanPollsChoice(Parcel in) {
        this.choiceId = in.readString();
        this.pollId = in.readString();
        this.choice = in.readString();
        this.choicePer = in.readString();
        this.choiceCount = in.readString();
    }

    public static final Parcelable.Creator<BeanPollsChoice> CREATOR = new Parcelable.Creator<BeanPollsChoice>() {
        @Override
        public BeanPollsChoice createFromParcel(Parcel source) {
            return new BeanPollsChoice(source);
        }

        @Override
        public BeanPollsChoice[] newArray(int size) {
            return new BeanPollsChoice[size];
        }
    };
}
