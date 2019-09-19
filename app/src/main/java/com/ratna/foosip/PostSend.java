package com.ratna.foosip;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostSend implements Parcelable {
    protected PostSend(Parcel in) {

        this.id = in.readString();
        this.userId = in.readString();
        this.rid = in.readString();
        this.type = in.readString();
        this.post = in.readString();
        this.totalLikes = in.readString();
        this.totalComments = in.readInt();
        this.postType = in.readString();
        this.createdDate = in.readString();
        this.senderImage = in.readString();
        this.senderName = in.readString();
        this.description = in.readString();
        this.uid = in.readString();
    }

    public static final Creator<PostSend> CREATOR = new Creator<PostSend>() {
        @Override
        public PostSend createFromParcel(Parcel in) {
            return new PostSend(in);
        }

        @Override
        public PostSend[] newArray(int size) {
            return new PostSend[size];
        }
    };

    private String id;

    private String rid;

    private String type;

    private String post;

    private String totalLikes;

    private Integer totalComments;

    private String postType;

    private String createdDate;

    private String senderImage;

    private String senderName;

    private String userId;

    private String description;

    private String uid;



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(String totalLikes) {
        this.totalLikes = totalLikes;
    }

    public Integer getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    public  PostSend(String id,String userId,String rid,String type,String post,String totalLikes,int totalComments,String postType,
                     String createdDate,String senderImage,String senderName,String description,String uid) {

        this.id = id;
        this.userId = userId;
        this.rid = rid;
        this.type = type;
        this.post = post;
        this.totalLikes = totalLikes;
        this.totalComments = totalComments;
        this.postType = postType;
        this.createdDate = createdDate;
        this.senderImage = senderImage;
        this.senderName = senderName;
        this.description = description;
        this.uid =  uid;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(this.id);
        parcel.writeString(this.userId);
        parcel.writeString(this.rid);
        parcel.writeString(this.type);
        parcel.writeString(this.post);
        parcel.writeString(this.totalLikes);
        parcel.writeInt(this.totalComments);
        parcel.writeString(this.postType);
        parcel.writeString(this.createdDate);
        parcel.writeString(this.senderImage);
        parcel.writeString(this.senderName);
        parcel.writeString(this.description);
        parcel.writeString(this.uid);
    }
}
