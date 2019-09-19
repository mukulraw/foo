package com.ratna.foosip;

public class Friends2 {

    public String date;
    public String user_id_serv;

    public Friends2(){

    }

    public Friends2(String date,String user_id_serv) {
        this.date = date;
        this.user_id_serv = user_id_serv;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_id_serv() {
        return user_id_serv;
    }

    public void setUser_id_serv(String user_id_serv) {
        this.user_id_serv = user_id_serv;
    }

}
