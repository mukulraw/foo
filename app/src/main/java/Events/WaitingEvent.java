package Events;

import java.util.List;

import Model.RestaurantJson;
import Model.WaitingJson;

/**
 * Created by ratna on 12/6/2016.
 */
public class WaitingEvent {

    private List<WaitingJson> waiting;
    private String rname;
    private String logo;

    public List<WaitingJson> getWaiting() {
        return waiting;
    }
    public void setWaiting(List<WaitingJson> waiting) {
        this.waiting = waiting;
    }

    public String getR_name() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
