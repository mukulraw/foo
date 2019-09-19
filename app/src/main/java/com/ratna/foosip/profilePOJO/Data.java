package com.ratna.foosip.profilePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("rid")
    @Expose
    private String rid;
    @SerializedName("mobile_verified")
    @Expose
    private String mobileVerified;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("sel_type")
    @Expose
    private String selType;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("email_code")
    @Expose
    private String emailCode;
    @SerializedName("fcm_reg_id")
    @Expose
    private String fcmRegId;
    @SerializedName("date1")
    @Expose
    private String date1;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("profession")
    @Expose
    private String profession;
    @SerializedName("interest")
    @Expose
    private String interest;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("uid")
    @Expose
    private String uid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(String mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSelType() {
        return selType;
    }

    public void setSelType(String selType) {
        this.selType = selType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    public String getFcmRegId() {
        return fcmRegId;
    }

    public void setFcmRegId(String fcmRegId) {
        this.fcmRegId = fcmRegId;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
