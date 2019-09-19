package com.ratna.foosip.profilePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class picRequestBean {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

}
