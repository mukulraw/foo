package Model;

/**
 * Created by ratna on 10/22/2016.
 */
public class ItemJson {

    private String name;
    private String price;
    private String id;
    private String rid;
    private String item_type;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRid() {
        return rid;
    }
    public void setRid(String rid) {
        this.rid = rid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getItem_type()
    {
        return item_type;
    }

    public void setItem_type(String item_type)
    {
        this.item_type = item_type;
    }
}
