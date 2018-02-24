package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ExT-Emp-001 on 05-11-2017.
 */

public class BeanArticalComment implements Parcelable {


    @SerializedName("ArticleCommentId")
    public String articleCommentId;


    @SerializedName("TopicId")
    public String topicId;


    @SerializedName("SubTopicId")
    public String subTopicId;


    @SerializedName("ArticalId")
    public String articalId;


    @SerializedName("PeopleId")
    public String peopleId;


    @SerializedName("ParentId")
    public String parentId;


    @SerializedName("Likes")
    public String likes;


    @SerializedName("Comments")
    public String comments;


    @SerializedName("FullName")
    public String fullName;


    @SerializedName("ProfileImage")
    public String profileImage;


    @SerializedName("LikeStatus")
    public String likeStatus;

    @SerializedName("Article_Reply_Array")
    public ArrayList<BeanArticalSubComment> article_Reply_Array;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.articleCommentId);
        dest.writeString(this.topicId);
        dest.writeString(this.subTopicId);
        dest.writeString(this.articalId);
        dest.writeString(this.peopleId);
        dest.writeString(this.parentId);
        dest.writeString(this.likes);
        dest.writeString(this.comments);
        dest.writeString(this.fullName);
        dest.writeString(this.profileImage);
        dest.writeString(this.likeStatus);
        dest.writeTypedList(this.article_Reply_Array);
    }

    public BeanArticalComment() {
    }

    protected BeanArticalComment(Parcel in) {
        this.articleCommentId = in.readString();
        this.topicId = in.readString();
        this.subTopicId = in.readString();
        this.articalId = in.readString();
        this.peopleId = in.readString();
        this.parentId = in.readString();
        this.likes = in.readString();
        this.comments = in.readString();
        this.fullName = in.readString();
        this.profileImage = in.readString();
        this.likeStatus = in.readString();
        this.article_Reply_Array = in.createTypedArrayList(BeanArticalSubComment.CREATOR);
    }

    public static final Creator<BeanArticalComment> CREATOR = new Creator<BeanArticalComment>() {
        @Override
        public BeanArticalComment createFromParcel(Parcel source) {
            return new BeanArticalComment(source);
        }

        @Override
        public BeanArticalComment[] newArray(int size) {
            return new BeanArticalComment[size];
        }
    };
}
