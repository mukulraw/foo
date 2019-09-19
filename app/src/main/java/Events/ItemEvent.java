package Events;

import java.util.List;

import Model.HistoryJson;
import Model.ItemHistoryJson;

/**
 * Created by ratna on 1/4/2017.
 */
public class ItemEvent {

    private List<ItemHistoryJson> item_details;

    public List<ItemHistoryJson> getItemHistoryJsons() {
        return item_details;
    }

    public void setItemHistoryJsons(List<ItemHistoryJson> item_details) {
        this.item_details = item_details;
    }
}
