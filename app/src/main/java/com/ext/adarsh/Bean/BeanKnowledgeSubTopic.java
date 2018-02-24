package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 31-10-2017.
 */

public class BeanKnowledgeSubTopic implements Parcelable {
    @SerializedName("TopicId")
    public String topicId;

    @SerializedName("SubTopicId")
    public String subTopicId;

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("Title")
    public String title;

    @SerializedName("Description")
    public String description;

    @SerializedName("ModifiedDate")
    public String modifiedDate;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("LikeStatus")
    public String likeStatus;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.topicId);
        dest.writeString(this.subTopicId);
        dest.writeString(this.peopleId);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.modifiedDate);
        dest.writeString(this.fullName);
        dest.writeString(this.likeStatus);
    }

    public BeanKnowledgeSubTopic() {
    }

    protected BeanKnowledgeSubTopic(Parcel in) {
        this.topicId = in.readString();
        this.subTopicId = in.readString();
        this.peopleId = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.modifiedDate = in.readString();
        this.fullName = in.readString();
        this.likeStatus = in.readString();
    }

    public static final Parcelable.Creator<BeanKnowledgeSubTopic> CREATOR = new Parcelable.Creator<BeanKnowledgeSubTopic>() {
        @Override
        public BeanKnowledgeSubTopic createFromParcel(Parcel source) {
            return new BeanKnowledgeSubTopic(source);
        }

        @Override
        public BeanKnowledgeSubTopic[] newArray(int size) {
            return new BeanKnowledgeSubTopic[size];
        }
    };
}
