package com.ratna.foosip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class getUserModel {

    @SerializedName("user_id")
    @Expose
    private String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
