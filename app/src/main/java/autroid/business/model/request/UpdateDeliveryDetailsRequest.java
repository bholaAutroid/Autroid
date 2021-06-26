package autroid.business.model.request;

public class UpdateDeliveryDetailsRequest {

    private String booking,delivery_date,delivery_time;

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }
}
