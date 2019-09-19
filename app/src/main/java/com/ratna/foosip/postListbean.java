package com.ratna.foosip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class postListbean {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("rid")
    @Expose
    private String rid;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("post")
    @Expose
    private String post;

    @SerializedName("total_likes")
    @Expose
    private String totalLikes;

    @SerializedName("total_comments")
    @Expose
    private Integer totalComments;

    @SerializedName("comments")
    @Expose
    private ArrayList<Comments> comments;

    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("sender_image")
    @Expose
    private String senderImage;
    @SerializedName("sender_name")
    @Expose
    private String senderName;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("description")
    @Expose
    private String description;


    @SerializedName("uid")
    @Expose
    private String uid;

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

    public ArrayList<Comments> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comments> comments) {
        this.comments = comments;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
