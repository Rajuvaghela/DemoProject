package com.ext.adarsh.Bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 30-10-2017.
 */

public class BeanPost {

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

    @SerializedName("Description")
    public String description;

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

    @SerializedName("Sub_Comment_Array")
    public String sub_Comment_Array;


}
