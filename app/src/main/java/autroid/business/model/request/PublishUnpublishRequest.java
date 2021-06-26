package autroid.business.model.request;

/**
 * Created by pranav.mittal on 06/18/17.
 */

public class PublishUnpublishRequest {

    private String id,car;

    public void setStock_id(String stock_id) {
        this.id = stock_id;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getId() {
        return id;
    }

    public String getCar() {
        return car;
    }
}
