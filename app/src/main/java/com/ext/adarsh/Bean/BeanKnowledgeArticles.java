package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 11-08-2017.
 */

public class BeanKnowledgeArticles implements Parcelable {

    @SerializedName("ArticalId")
    public  String articalId;

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
    public String LikeStatus;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.articalId);
        dest.writeString(this.topicId);
        dest.writeString(this.subTopicId);
        dest.writeString(this.peopleId);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.modifiedDate);
        dest.writeString(this.fullName);
    }

    public BeanKnowledgeArticles() {
    }

    protected BeanKnowledgeArticles(Parcel in) {
        this.articalId = in.readString();
        this.topicId = in.readString();
        this.subTopicId = in.readString();
        this.peopleId = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.modifiedDate = in.readString();
        this.fullName = in.readString();
    }

    public static final Creator<BeanKnowledgeArticles> CREATOR = new Creator<BeanKnowledgeArticles>() {
        @Override
        public BeanKnowledgeArticles createFromParcel(Parcel source) {
            return new BeanKnowledgeArticles(source);
        }

        @Override
        public BeanKnowledgeArticles[] newArray(int size) {
            return new BeanKnowledgeArticles[size];
        }
    };
}
