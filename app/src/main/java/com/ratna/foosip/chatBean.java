package com.ratna.foosip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class chatBean {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("sender_id")
    @Expose
    private String senderId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("sender_image")
    @Expose
    private String senderImage;
    @SerializedName("sender_name")
    @Expose
    private String senderName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

}
