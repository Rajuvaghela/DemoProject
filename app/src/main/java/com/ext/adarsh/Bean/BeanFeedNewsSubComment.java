package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ExT-Emp-001 on 02-11-2017.
 */

public class BeanFeedNewsSubComment implements Parcelable {

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("ProfileImage")
    public String profileImage;

    @SerializedName("PostProfileImage")
    public String postProfileImage;

    @SerializedName("PostCommentId")
    public String postCommentId;

    @SerializedName("AlbumId")
    public String albumId;
    @SerializedName("AlbumDetailId")
    public String albumDetailId;

    @SerializedName("PostId")
    public String postId;

    @SerializedName("PostPeopleId")
    public String postPeopleId;

    @SerializedName("CommentPeopleId")
    public String commentPeopleId;


    @SerializedName("ParentId")
    public String parentId;

    @SerializedName("Comment")
    public String comment;

    @SerializedName("LikesUserId")
    public String likesUserId;

    @SerializedName("LikeCount")
    public String likeCount;

    @SerializedName("LikeStatus")
    public String likeStatus;


    @SerializedName("Sub_Sub_Comment_Array")
    public ArrayList<BeanFeedNewsSubSubComment> sub_Sub_Comment_Array;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullName);
        dest.writeString(this.profileImage);
        dest.writeString(this.postProfileImage);
        dest.writeString(this.postCommentId);
        dest.writeString(this.albumId);
        dest.writeString(this.albumDetailId);
        dest.writeString(this.postId);
        dest.writeString(this.postPeopleId);
        dest.writeString(this.commentPeopleId);
        dest.writeString(this.parentId);
        dest.writeString(this.comment);
        dest.writeString(this.likesUserId);
        dest.writeString(this.likeCount);
        dest.writeString(this.likeStatus);
        dest.writeTypedList(this.sub_Sub_Comment_Array);
    }

    public BeanFeedNewsSubComment(String fullName, String profileImage, String postProfileImage, String postCommentId, String albumId, String albumDetailId, String postId, String postPeopleId, String commentPeopleId, String parentId, String comment, String likesUserId, String likeCount, String likeStatus, ArrayList<BeanFeedNewsSubSubComment> sub_Sub_Comment_Array) {
        this.fullName = fullName;
        this.profileImage = profileImage;
        this.postProfileImage = postProfileImage;
        this.postCommentId = postCommentId;
        this.albumId = albumId;
        this.albumDetailId = albumDetailId;
        this.postId = postId;
        this.postPeopleId = postPeopleId;
        this.commentPeopleId = commentPeopleId;
        this.parentId = parentId;
        this.comment = comment;
        this.likesUserId = likesUserId;
        this.likeCount = likeCount;
        this.likeStatus = likeStatus;
        this.sub_Sub_Comment_Array = sub_Sub_Comment_Array;
    }

    public BeanFeedNewsSubComment() {
    }

    protected BeanFeedNewsSubComment(Parcel in) {
        this.fullName = in.readString();
        this.profileImage = in.readString();
        this.postProfileImage = in.readString();
        this.postCommentId = in.readString();
        this.albumId = in.readString();
        this.albumDetailId = in.readString();
        this.postId = in.readString();
        this.postPeopleId = in.readString();
        this.commentPeopleId = in.readString();
        this.parentId = in.readString();
        this.comment = in.readString();
        this.likesUserId = in.readString();
        this.likeCount = in.readString();
        this.likeStatus = in.readString();
        this.sub_Sub_Comment_Array = in.createTypedArrayList(BeanFeedNewsSubSubComment.CREATOR);
    }

    public static final Creator<BeanFeedNewsSubComment> CREATOR = new Creator<BeanFeedNewsSubComment>() {
        @Override
        public BeanFeedNewsSubComment createFromParcel(Parcel source) {
            return new BeanFeedNewsSubComment(source);
        }

        @Override
        public BeanFeedNewsSubComment[] newArray(int size) {
            return new BeanFeedNewsSubComment[size];
        }
    };
}
