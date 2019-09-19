package Model;

import java.util.List;

/**
 * Created by ratna on 10/22/2016.
 */
public class SubCatJson {

    private String id;
    private String name;
    private String photo;
    private String sub_category;
    private String status;
    private List<ItemJson> items;

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
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getSub_category() {
        return sub_category;
    }
    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }


    public List<ItemJson> getItems() {
        return items;
    }
    public void setItems(List<ItemJson> items) {
        this.items = items;
    }


}
