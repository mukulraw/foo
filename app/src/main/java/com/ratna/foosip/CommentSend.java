package com.ratna.foosip;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CommentSend implements Parcelable {
    protected CommentSend(Parcel in) {
        this.id = in.readString();
        this.user_id = in.readString();
        this.comment = in.readString();
        this.user_image = in.readString();
        this.user_name = in.readString();
        this.uid = in.readString();
    }

    public static final Creator<CommentSend> CREATOR = new Creator<CommentSend>() {
        @Override
        public CommentSend createFromParcel(Parcel in) {
           return new CommentSend(in);
        }

        @Override
        public CommentSend[] newArray(int size) {
            return new CommentSend[size];
        }
    };


    private String id;
    private String user_id;
    private String comment;
    private String user_image;
    private String user_name;
    private String uid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public CommentSend(String id,String user_id,String comment,String user_image,String user_name,String uid)
    {
        this.id = id;
        this.user_id = user_id;
        this.comment = comment;
        this.user_image = user_image;
        this.user_name = user_name;
        this.uid = uid;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.user_id);
        parcel.writeString(this.comment);
        parcel.writeString(this.user_image);
        parcel.writeString(this.user_name);
        parcel.writeString(this.uid);

    }
}
