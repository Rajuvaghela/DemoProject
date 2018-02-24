package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 11-08-2017.
 */

public class BeanKnowledge implements Parcelable {

    @SerializedName("TopicId")
    public  String topicId;

    @SerializedName("Title")
    public  String title;

    @SerializedName("Description")
    public String description;

    @SerializedName("ModifiedDate")
    public  String modifiedDate;
    @SerializedName("TotalArticale")
    public  String totalArticale;

    @SerializedName("SubTotalArticale")
    public String subTotalArticale;
    BeanKnowledge(){

    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.topicId);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.modifiedDate);
        dest.writeString(this.totalArticale);
        dest.writeString(this.subTotalArticale);
    }

    protected BeanKnowledge(Parcel in) {
        this.topicId = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.modifiedDate = in.readString();
        this.totalArticale = in.readString();
        this.subTotalArticale = in.readString();
    }

    public static final Parcelable.Creator<BeanKnowledge> CREATOR = new Parcelable.Creator<BeanKnowledge>() {
        @Override
        public BeanKnowledge createFromParcel(Parcel source) {
            return new BeanKnowledge(source);
        }

        @Override
        public BeanKnowledge[] newArray(int size) {
            return new BeanKnowledge[size];
        }
    };
}
