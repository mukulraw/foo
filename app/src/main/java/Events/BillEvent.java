package Events;

import java.util.List;

import Model.OrderJson;

/**
 * Created by ratna on 11/24/2016.
 */

public class BillEvent {
    private List<OrderJson> orderitems;
    private String service_charge;
    private String service_tax;
    private String swach_cess;
    private String kirishi_cess;
    private String vat_alcohol;
    private String vat_food;

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getService_tax() {
        return service_tax;
    }

    public void setService_tax(String service_tax) {
        this.service_tax = service_tax;
    }

    public String getSwach_cess() {
        return swach_cess;
    }

    public void setSwach_cess(String swach_cess) {
        this.swach_cess = swach_cess;
    }


    public String getKirishi_cess() {
        return kirishi_cess;
    }

    public void setKirishi_cess(String kirishi_cess) {
        this.kirishi_cess = kirishi_cess;
    }

    public String getVat_alcohol() {
        return vat_alcohol;
    }

    public void setVat_alcohol(String vat_alcohol) {
        this.vat_alcohol = vat_alcohol;
    }

    public String getVat_food() {
        return vat_food;
    }

    public void setVat_food(String vat_food) {
        this.vat_food = vat_food;
    }





    public List<OrderJson> getOrderitems() {
        return orderitems;
    }
    public void setOrderitems(List<OrderJson> orderitems) {
        this.orderitems = orderitems;
    }

}
