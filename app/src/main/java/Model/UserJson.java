package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ratna on 4/22/2018.
 */

public class UserJson {

    String message;
    List<UserVal> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserVal> getUserVals() {
        return data;
    }

    public void setUserVals(List<UserVal> data) {
        this.data = data;
    }


}
