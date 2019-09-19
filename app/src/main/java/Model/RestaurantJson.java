package Model;

/**
 * Created by ratna on 12/5/2016.
 */
public class RestaurantJson {

    private String rid;
    private String txt_address;
    private String res_area;
    private String name;
    private String available;
    private String waiting;
    private String total_rating;
    private String no_of_user_rated;
    private String logo;
    private String phone;
    private String pin;
    private String open_time;
    private String close_time;


    public String getRid() {
        return rid;
    }
    public void setRid(String rid) {
        this.rid = rid;
    }
    public String getTxt_address() {
        return txt_address;
    }
    public void setTxt_address(String txt_address) {
        this.txt_address = txt_address;
    }
    public String getRes_area() {
        return res_area;
    }
    public void setRes_area(String res_area) {
        this.res_area = res_area;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAvailable() {
        return available;
    }
    public void setAvailable(String available) {
        this.available = available;
    }
    public String getWaiting() {
        return waiting;
    }
    public void setWaiting(String waiting) {
        this.waiting = waiting;
    }
    public String getTotal_rating() {
        return total_rating;
    }
    public void setTotal_rating(String total_rating) {
        this.total_rating = total_rating;
    }

    public String getNo_of_user_rated() {
        return no_of_user_rated;
    }
    public void setNo_of_user_rated(String no_of_user_rated) {
        this.no_of_user_rated = no_of_user_rated;
    }

    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }


    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPin() {
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getOpen_time() {
        return open_time;
    }
    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }
    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }
}
