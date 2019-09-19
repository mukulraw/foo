package Events;

import java.util.List;

import Model.OrderJson;
import Model.RestaurantJson;

/**
 * Created by ratna on 12/5/2016.
 */
public class RestaurantEvent {
    private List<RestaurantJson> restaurant;

    public List<RestaurantJson> getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(List<RestaurantJson> restaurant) {
        this.restaurant = restaurant;
    }

}
