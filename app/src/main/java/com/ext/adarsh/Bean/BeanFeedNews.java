package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ExT-Emp-001 on 02-11-2017.
 */

public class BeanFeedNews implements Parcelable {

    @SerializedName("PostId")
    public String postId;

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("IsFile")
    public String isFile;

    @SerializedName("PrivacyFlag")
    public String privacyFlag;

    @SerializedName("LikeCount")
    public String likeCount;

    @SerializedName("FileType")
    public String fileType;

    @SerializedName("AlbumDetailId")
    public String albumDetailId;

    @SerializedName("Post")
    public String post;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("ProfileImage")
    public String profileImage;

    @SerializedName("PostProfileImage")
    public String postProfileImage;

    @SerializedName("PostDateTime")
    public String postDateTime;

    @SerializedName("TotalComment")
    public String totalComment;

    @SerializedName("LikeStatus")
    public String likeStatus;

    @SerializedName("PostPeopleId")
    public String postPeopleId;


    @SerializedName("SharePostId")
    public String sharePostId;

    @SerializedName("Description")
    public String description;

    @SerializedName("ShareFlag")
    public String shareFlag;

    @SerializedName("ShareType")
    public String shareType;

    @SerializedName("ShareWithIds")
    public String shareWithIds;

    @SerializedName("ShareWithNames")
    public String shareWithNames;

    @SerializedName("PostPeopleName")
    public String postPeopleName;

    @SerializedName("ToFullName")
    public String toFullName;

    @SerializedName("ToPeopleFlag")
    public String toPeopleFlag;

    @SerializedName("IsLike")
    public String isLike;

    @SerializedName("MatchId")
    public String matchId;

    @SerializedName("TagFlag")
    public String tagFlag;

    @SerializedName("TotalPersons")
    public String totalPersons;

    @SerializedName("MultiFlag")
    public String multiFlag;

    @SerializedName("Post_Image_Array")
    public ArrayList<BeanFeddPostImage> post_Image_Array;

    @SerializedName("Post_Like_People_Array")
    public ArrayList<BeanFeedNewsLikePeopleArray> post_Like_People_Array;

    @SerializedName("Sub_Comment_Array")
    public ArrayList<BeanFeedNewsSubComment> sub_Comment_Array;

    @SerializedName("Tag_Friend_One_Array")
    public ArrayList<BeanFeedNewsTagFriendOne> tag_Friend_One_Array;

    @SerializedName("Share_Post_Array")
    public ArrayList<BeanFeedNewsSharesPostArray> share_Post_Array;

    @SerializedName("Tag_Friend_More_Array")
    public ArrayList<BeanFeedNewsTagFriendMore> tag_Friend_More_Array;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.postId);
        dest.writeString(this.peopleId);
        dest.writeString(this.isFile);
        dest.writeString(this.privacyFlag);
        dest.writeString(this.likeCount);
        dest.writeString(this.fileType);
        dest.writeString(this.albumDetailId);
        dest.writeString(this.post);
        dest.writeString(this.fullName);
        dest.writeString(this.profileImage);
        dest.writeString(this.postProfileImage);
        dest.writeString(this.postDateTime);
        dest.writeString(this.totalComment);
        dest.writeString(this.likeStatus);
        dest.writeString(this.postPeopleId);
        dest.writeString(this.sharePostId);
        dest.writeString(this.description);
        dest.writeString(this.shareFlag);
        dest.writeString(this.shareType);
        dest.writeString(this.shareWithIds);
        dest.writeString(this.shareWithNames);
        dest.writeString(this.postPeopleName);
        dest.writeString(this.toFullName);
        dest.writeString(this.toPeopleFlag);
        dest.writeString(this.isLike);
        dest.writeString(this.matchId);
        dest.writeString(this.tagFlag);
        dest.writeString(this.totalPersons);
        dest.writeString(this.multiFlag);
        dest.writeTypedList(this.post_Image_Array);
        dest.writeTypedList(this.post_Like_People_Array);
        dest.writeTypedList(this.sub_Comment_Array);
        dest.writeTypedList(this.tag_Friend_One_Array);
        dest.writeList(this.share_Post_Array);
        dest.writeTypedList(this.tag_Friend_More_Array);
    }

    public BeanFeedNews() {
    }

    protected BeanFeedNews(Parcel in) {
        this.postId = in.readString();
        this.peopleId = in.readString();
        this.isFile = in.readString();
        this.privacyFlag = in.readString();
        this.likeCount = in.readString();
        this.fileType = in.readString();
        this.albumDetailId = in.readString();
        this.post = in.readString();
        this.fullName = in.readString();
        this.profileImage = in.readString();
        this.postProfileImage = in.readString();
        this.postDateTime = in.readString();
        this.totalComment = in.readString();
        this.likeStatus = in.readString();
        this.postPeopleId = in.readString();
        this.sharePostId = in.readString();
        this.description = in.readString();
        this.shareFlag = in.readString();
        this.shareType = in.readString();
        this.shareWithIds = in.readString();
        this.shareWithNames = in.readString();
        this.postPeopleName = in.readString();
        this.toFullName = in.readString();
        this.toPeopleFlag = in.readString();
        this.isLike = in.readString();
        this.matchId = in.readString();
        this.tagFlag = in.readString();
        this.totalPersons = in.readString();
        this.multiFlag = in.readString();
        this.post_Image_Array = in.createTypedArrayList(BeanFeddPostImage.CREATOR);
        this.post_Like_People_Array = in.createTypedArrayList(BeanFeedNewsLikePeopleArray.CREATOR);
        this.sub_Comment_Array = in.createTypedArrayList(BeanFeedNewsSubComment.CREATOR);
        this.tag_Friend_One_Array = in.createTypedArrayList(BeanFeedNewsTagFriendOne.CREATOR);
        this.share_Post_Array = new ArrayList<BeanFeedNewsSharesPostArray>();
        in.readList(this.share_Post_Array, BeanFeedNewsSharesPostArray.class.getClassLoader());
        this.tag_Friend_More_Array = in.createTypedArrayList(BeanFeedNewsTagFriendMore.CREATOR);
    }

    public static final Parcelable.Creator<BeanFeedNews> CREATOR = new Parcelable.Creator<BeanFeedNews>() {
        @Override
        public BeanFeedNews createFromParcel(Parcel source) {
            return new BeanFeedNews(source);
        }

        @Override
        public BeanFeedNews[] newArray(int size) {
            return new BeanFeedNews[size];
        }
    };
}
