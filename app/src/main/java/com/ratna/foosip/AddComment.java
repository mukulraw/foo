package com.ratna.foosip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddComment {

    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("post_id")
    @Expose
    private String post_id;
    @SerializedName("comment")
    @Expose
    private String comment;



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
