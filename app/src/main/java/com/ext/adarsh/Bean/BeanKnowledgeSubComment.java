package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 30-10-2017.
 */

public class BeanKnowledgeSubComment implements Parcelable {

    @SerializedName("TopicCommentId")
    public String TopicCommentId;

    @SerializedName("TopicId")
    public String TopicId;

    @SerializedName("SubTopicId")
    public String SubTopicId;

    @SerializedName("PeopleId")
    public String PeopleId;

    @SerializedName("ParentId")
    public String ParentId;

    @SerializedName("Likes")
    public String Likes;

    @SerializedName("Comments")
    public String Comments;

    @SerializedName("FullName")
    public String FullName;

    @SerializedName("ProfileImage")
    public String ProfileImage;

    @SerializedName("LikeStatus")
    public String likeStatus;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.TopicCommentId);
        dest.writeString(this.TopicId);
        dest.writeString(this.SubTopicId);
        dest.writeString(this.PeopleId);
        dest.writeString(this.ParentId);
        dest.writeString(this.Likes);
        dest.writeString(this.Comments);
        dest.writeString(this.FullName);
        dest.writeString(this.ProfileImage);
        dest.writeString(this.likeStatus);
    }

    public BeanKnowledgeSubComment() {
    }

    protected BeanKnowledgeSubComment(Parcel in) {
        this.TopicCommentId = in.readString();
        this.TopicId = in.readString();
        this.SubTopicId = in.readString();
        this.PeopleId = in.readString();
        this.ParentId = in.readString();
        this.Likes = in.readString();
        this.Comments = in.readString();
        this.FullName = in.readString();
        this.ProfileImage = in.readString();
        this.likeStatus = in.readString();
    }

    public static final Parcelable.Creator<BeanKnowledgeSubComment> CREATOR = new Parcelable.Creator<BeanKnowledgeSubComment>() {
        @Override
        public BeanKnowledgeSubComment createFromParcel(Parcel source) {
            return new BeanKnowledgeSubComment(source);
        }

        @Override
        public BeanKnowledgeSubComment[] newArray(int size) {
            return new BeanKnowledgeSubComment[size];
        }
    };
}
