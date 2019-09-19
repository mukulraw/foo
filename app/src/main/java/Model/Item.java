package Model;

/**
 * Created by ratna on 11/23/2016.
 */

public class Item {

    private String id;
    private String prd_id;
    private String prd_name;
    private String price;
    private String qty;
    private String user_order_status;
    private boolean checked = false ;


    public Item(String id,String prd_id,String name,String price,String qty,String user_order_status,Boolean checked)
    {
        this.id = id;
        this.prd_id = prd_id;
        this.prd_name = name;
        this.price = price;
        this.qty = qty;
        this.user_order_status = user_order_status;
        this.checked = checked;
    }

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

    public String getPrd_name() {
        return prd_name;
    }
    public void setPrd_name(String prd_name) {
        this.prd_name = prd_name;
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

    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void toggleChecked() {
        checked = !checked ;
    }

}
