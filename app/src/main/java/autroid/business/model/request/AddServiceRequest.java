package autroid.business.model.request;

/**
 * Created by pranav.mittal on 06/25/17.
 */

public class AddServiceRequest {

    private String service_id,price;

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
