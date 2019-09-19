package Events;

import java.util.List;

import Model.HistoryJson;
import Model.MenuJson;

/**
 * Created by ratna on 1/4/2017.
 */
public class HistoryEvent {

    private List<HistoryJson> history;

    public List<HistoryJson> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryJson> history) {
        this.history = history;
    }
}
