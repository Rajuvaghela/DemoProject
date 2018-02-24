package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ExT-Emp-001 on 25-10-2017.
 */

public class BeanKnowledgeComment implements Parcelable {
    @SerializedName("TopicCommentId")
    public String TopicCommentId;

    @SerializedName("TopicId")
    public String   TopicId;

    @SerializedName("SubTopicId")
    public String SubTopicId;

    @SerializedName("PeopleId")
    public String PeopleId;

    @SerializedName("ParentId")
    public String ParentId;

    @SerializedName("Likes")
    public String Likes;

    @SerializedName("Comments")
    public  String Comments;

    @SerializedName("FullName")
    public String FullName;

    @SerializedName("ProfileImage")
    public String ProfileImage;

    @SerializedName("LikeStatus")
    public String likeStatus;


    @SerializedName("Topic_Reply_Array")
    public ArrayList<BeanKnowledgeSubComment> topic_Reply_Array;


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
        dest.writeTypedList(this.topic_Reply_Array);
    }

    public BeanKnowledgeComment() {
    }

    protected BeanKnowledgeComment(Parcel in) {
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
        this.topic_Reply_Array = in.createTypedArrayList(BeanKnowledgeSubComment.CREATOR);
    }

    public static final Parcelable.Creator<BeanKnowledgeComment> CREATOR = new Parcelable.Creator<BeanKnowledgeComment>() {
        @Override
        public BeanKnowledgeComment createFromParcel(Parcel source) {
            return new BeanKnowledgeComment(source);
        }

        @Override
        public BeanKnowledgeComment[] newArray(int size) {
            return new BeanKnowledgeComment[size];
        }
    };
}

