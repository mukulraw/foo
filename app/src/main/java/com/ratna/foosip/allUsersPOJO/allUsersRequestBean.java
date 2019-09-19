package com.ratna.foosip.allUsersPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class allUsersRequestBean {

    @SerializedName("rid")
    @Expose
    private String rid;


    @SerializedName("user_id")
    @Expose
    private String user_id;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}
