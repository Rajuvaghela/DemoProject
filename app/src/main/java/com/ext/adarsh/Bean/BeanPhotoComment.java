package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ExT-Emp-011 on 11/15/2017.
 */

public class BeanPhotoComment implements Parcelable {


    @SerializedName("FullName")
    public String fullName;

    @SerializedName("ProfileImage")
    public String profileImage;

    @SerializedName("PostProfileImage")
    public String postProfileImage;

    @SerializedName("AlbumId")
    public String albumId;

    @SerializedName("AlbumDetailId")
    public String albumDetailId;

    @SerializedName("PostPeopleFullName")
    public String postPeopleFullName;

    @SerializedName("CommentPeopleFullName")
    public String commentPeopleFullName;

    @SerializedName("PostPeopleId")
    public String postPeopleId;

    @SerializedName("CommentPeopleId")
    public String commentPeopleId;

    @SerializedName("ParentId")
    public String parentId;

    @SerializedName("LikesUserId")
    public String likesUserId;

    @SerializedName("PostCommentId")
    public String postCommentId;

    @SerializedName("CommentPeopleProfileImage")
    public String commentPeopleProfileImage;

    @SerializedName("Comment")
    public String comment;

    @SerializedName("LikeCount")
    public String likeCount;

    @SerializedName("CommentPepoleId")
    public String commentPepoleId;

    @SerializedName("PostId")
    public String postId;

    @SerializedName("CommentLikeDislikeFlag")
    public String commentLikeDislikeFlag;

    @SerializedName("Photo_Comment_Replay_Array")
    public ArrayList<BeanPhotoSubComment> photo_Comment_Replay_Array;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullName);
        dest.writeString(this.profileImage);
        dest.writeString(this.postProfileImage);
        dest.writeString(this.albumId);
        dest.writeString(this.albumDetailId);
        dest.writeString(this.postPeopleId);
        dest.writeString(this.commentPeopleId);
        dest.writeString(this.parentId);
        dest.writeString(this.likesUserId);
        dest.writeString(this.postCommentId);
        dest.writeString(this.commentPeopleProfileImage);
        dest.writeString(this.comment);
        dest.writeString(this.likeCount);
        dest.writeString(this.commentPepoleId);
        dest.writeString(this.postId);
        dest.writeString(this.commentLikeDislikeFlag);
        dest.writeString(this.commentPeopleFullName);
        dest.writeTypedList(this.photo_Comment_Replay_Array);
    }

    public BeanPhotoComment() {
    }

    public BeanPhotoComment(String fullName, String profileImage, String postProfileImage, String postCommentId, String albumId, String albumDetailId, String postId, String postPeopleId, String commentPeopleId, String parentId, String comment, String likesUserId, String likeCount, String likeStatus,ArrayList<BeanPhotoSubComment> list) {
        this.commentPeopleFullName = fullName;
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
        this.commentLikeDislikeFlag = likeStatus;
        this.photo_Comment_Replay_Array = list;
    }

    protected BeanPhotoComment(Parcel in) {
        this.fullName = in.readString();
        this.profileImage = in.readString();
        this.postProfileImage = in.readString();
        this.albumId = in.readString();
        this.albumDetailId = in.readString();
        this.postPeopleId = in.readString();
        this.commentPeopleId = in.readString();
        this.parentId = in.readString();
        this.likesUserId = in.readString();
        this.postCommentId = in.readString();
        this.commentPeopleProfileImage = in.readString();
        this.comment = in.readString();
        this.likeCount = in.readString();
        this.commentPepoleId = in.readString();
        this.postId = in.readString();
        this.commentLikeDislikeFlag = in.readString();
        this.commentPeopleFullName = in.readString();
        this.photo_Comment_Replay_Array = in.createTypedArrayList(BeanPhotoSubComment.CREATOR);
    }

    public static final Creator<BeanPhotoComment> CREATOR = new Creator<BeanPhotoComment>() {
        @Override
        public BeanPhotoComment createFromParcel(Parcel source) {
            return new BeanPhotoComment(source);
        }

        @Override
        public BeanPhotoComment[] newArray(int size) {
            return new BeanPhotoComment[size];
        }
    };
}
