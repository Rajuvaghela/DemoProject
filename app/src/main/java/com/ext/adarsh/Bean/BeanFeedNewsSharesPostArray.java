package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
/**
 * Created by ExT-Emp-011 on 2/12/2018.
 */
public class BeanFeedNewsSharesPostArray implements Parcelable {

    @SerializedName("PostId")
    public String postId;

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("PostPeopleId")
    public String postPeopleId;

    @SerializedName("IsFile")
    public String isFile;

    @SerializedName("PrivacyFlag")
    public String privacyFlag;

    @SerializedName("LikeCount")
    public String likeCount;

    @SerializedName("FileType")
    public String fileType;

    @SerializedName("Description")
    public String description;

    @SerializedName("AlbumDetailId")
    public String albumDetailId;

    @SerializedName("Post")
    public String post;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("PostPeopleName")
    public String postPeopleName;

    @SerializedName("ProfileImage")
    public String profileImage;

    @SerializedName("PostProfileImage")
    public String postProfileImage;

    @SerializedName("PostDateTime")
    public String postDateTime;

    @SerializedName("TotalComment")
    public String totalComment;

    @SerializedName("IsLike")
    public String isLike;

    @SerializedName("MatchId")
    public String matchId;

    @SerializedName("TagFlag")
    public String tagFlag;

    @SerializedName("TotalPersons")
    public String totalPersons;

    @SerializedName("Post_Image_Array")
    public ArrayList<BeanFeddPostImage> post_Image_Array;

    @SerializedName("Share_Post_Tag_Friend_One_Array")
    public ArrayList<BeanFeedNewsTagFriendOne> share_Post_Tag_Friend_One_Array;

    @SerializedName("Share_Post_Tag_Friend_More_Array")
    public ArrayList<BeanFeedNewsTagFriendMore> share_Post_Tag_Friend_More_Array;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.postId);
        dest.writeString(this.peopleId);
        dest.writeString(this.postPeopleId);
        dest.writeString(this.isFile);
        dest.writeString(this.privacyFlag);
        dest.writeString(this.likeCount);
        dest.writeString(this.fileType);
        dest.writeString(this.description);
        dest.writeString(this.albumDetailId);
        dest.writeString(this.post);
        dest.writeString(this.fullName);
        dest.writeString(this.postPeopleName);
        dest.writeString(this.profileImage);
        dest.writeString(this.postProfileImage);
        dest.writeString(this.postDateTime);
        dest.writeString(this.totalComment);
        dest.writeString(this.isLike);
        dest.writeString(this.matchId);
        dest.writeString(this.tagFlag);
        dest.writeString(this.totalPersons);
        dest.writeTypedList(this.post_Image_Array);
        dest.writeTypedList(this.share_Post_Tag_Friend_One_Array);
        dest.writeTypedList(this.share_Post_Tag_Friend_More_Array);
    }

    public BeanFeedNewsSharesPostArray() {
    }

    protected BeanFeedNewsSharesPostArray(Parcel in) {
        this.postId = in.readString();
        this.peopleId = in.readString();
        this.postPeopleId = in.readString();
        this.isFile = in.readString();
        this.privacyFlag = in.readString();
        this.likeCount = in.readString();
        this.fileType = in.readString();
        this.description = in.readString();
        this.albumDetailId = in.readString();
        this.post = in.readString();
        this.fullName = in.readString();
        this.postPeopleName = in.readString();
        this.profileImage = in.readString();
        this.postProfileImage = in.readString();
        this.postDateTime = in.readString();
        this.totalComment = in.readString();
        this.isLike = in.readString();
        this.matchId = in.readString();
        this.tagFlag = in.readString();
        this.totalPersons = in.readString();
        this.post_Image_Array = in.createTypedArrayList(BeanFeddPostImage.CREATOR);
        this.share_Post_Tag_Friend_One_Array = in.createTypedArrayList(BeanFeedNewsTagFriendOne.CREATOR);
        this.share_Post_Tag_Friend_More_Array = in.createTypedArrayList(BeanFeedNewsTagFriendMore.CREATOR);
    }

    public static final Parcelable.Creator<BeanFeedNewsSharesPostArray> CREATOR = new Parcelable.Creator<BeanFeedNewsSharesPostArray>() {
        @Override
        public BeanFeedNewsSharesPostArray createFromParcel(Parcel source) {
            return new BeanFeedNewsSharesPostArray(source);
        }

        @Override
        public BeanFeedNewsSharesPostArray[] newArray(int size) {
            return new BeanFeedNewsSharesPostArray[size];
        }
    };
}
