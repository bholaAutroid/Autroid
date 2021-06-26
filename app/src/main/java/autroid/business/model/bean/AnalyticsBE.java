package autroid.business.model.bean;

import java.util.ArrayList;

/**
 * Created by pranav.mittal on 08/04/17.
 */

public class AnalyticsBE {

    private String id, name, role, avatar_address;

    private ArrayList<ValuesBE> analytics;

    public String getId() {
        return id;
    }

    public String getAvatar_address() {
        return avatar_address;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ValuesBE> getAnalytics() {
        return analytics;
    }
}
