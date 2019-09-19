package Model;

/**
 * Created by ratna on 1/4/2017.
 */
public class ItemHistoryJson {
    private String id;
    private String order_id;
    private String prd_id;
    private String price;
    private String date1;
    private String qty;
    private String temp_order_id;
    private String user_order_status;
    private String res_order_status;
    private String uid;
    private String item_name;
    private String item_type;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPrd_id() {
        return prd_id;
    }
    public void setPrd_id(String prd_id) {
        this.prd_id = prd_id;
    }
    public String getName() {
        return item_name;
    }
    public void setName(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_type() {
        return item_type;
    }
    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getQty() {
        return qty;
    }
    public void setQty(String qty) {
        this.qty = qty;
    }
    public String getUser_order_status() {
        return user_order_status;
    }
    public void setUser_order_status(String user_order_status) {
        this.user_order_status = user_order_status;
    }
}
