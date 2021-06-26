package autroid.business.model.bean;

/**
 * Created by pranav.mittal on 06/26/17.
 */

public class ServiceTypeBE {

    private String id,business;
    private int is_added;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public int getIs_added() {
        return is_added;
    }

    public void setIs_added(int is_added) {
        this.is_added = is_added;
    }
}
