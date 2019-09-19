package com.ratna.foosip;

public class checkBean {

    String userId;
    boolean isCheck;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getCheck()
    {
        return isCheck;
    }
}
