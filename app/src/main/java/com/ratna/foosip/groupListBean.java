package com.ratna.foosip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class groupListBean {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("rid")
    @Expose
    private String rid;
    @SerializedName("admin_id")
    @Expose
    private String adminId;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("last_message")
    @Expose
    private String lastMessage;
    @SerializedName("last_message_time")
    @Expose
    private String lastMessageTime;

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

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

}
