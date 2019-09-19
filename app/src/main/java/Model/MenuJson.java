package Model;

import java.util.List;

/**
 * Created by ratna on 10/22/2016.
 */
public class MenuJson {
    private String id;
    private String name;
    private List<SubCatJson> sub_cat;

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

    public List<SubCatJson> getSub_cat() {
        return sub_cat;
    }
    public void setSub_cat(List<SubCatJson> sub_cat) {
        this.sub_cat = sub_cat;
    }
}
