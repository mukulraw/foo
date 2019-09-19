package com.ratna.foosip;

/**
 * Created by AkshayeJH on 13/07/17.
 */

public class Friends {

    public String request_type;
    public String user_id_serv;





    public Friends(){

    }

    public Friends(String request_type,String user_id_serv) {
        this.request_type = request_type;
        this.user_id_serv = user_id_serv;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }
    public String getUser_id_serv() {
        return user_id_serv;
    }

    public void setUser_id_serv(String user_id_serv) {
        this.user_id_serv = user_id_serv;
    }
}
