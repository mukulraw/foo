package Events;

import java.util.List;

import Model.MenuJson;

/**
 * Created by ratna on 10/22/2016.
 */
public class MenuEvent {
    private List<MenuJson> menu;
    private String temp_order_id;
    private String qr_code;
    private String rid;
    private String tableno;
    private String waiter_id;
    private String tid;
    private String message;


    public String getTemp_order_id() {
        return temp_order_id;
    }

    public void setTemp_order_id(String temp_order_id) {
        this.temp_order_id = temp_order_id;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }
    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getTableno() {
        return tableno;
    }

    public void setTableno(String tableno) {
        this.tableno = tableno;
    }
    public String getWaiter_id() {
        return waiter_id;
    }

    public void setWaiter_id(String waiter_id) {
        this.waiter_id = waiter_id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MenuJson> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuJson> menu) {
        this.menu = menu;
    }
}
