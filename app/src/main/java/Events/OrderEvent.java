package Events;

import java.util.List;

import Model.MenuJson;
import Model.OrderJson;

/**
 * Created by ratna on 11/17/2016.
 */

public class OrderEvent {
    private List<OrderJson> orderitems;

    public List<OrderJson> getOrderitems() {
        return orderitems;
    }
    public void setOrderitems(List<OrderJson> orderitems) {
        this.orderitems = orderitems;
    }
}
