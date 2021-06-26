package autroid.business.model.realm;

import io.realm.RealmObject;

public class OrdersRealm extends RealmObject {

    private String id="",contact_no="",orderNumber="", deliveredBy ="",timeSlot="",name="",timeLeft="",status;

    private boolean isStock;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDeliveredBy() {
        return deliveredBy;
    }

    public void setDeliveredBy(String deliveredBy) {
        this.deliveredBy = deliveredBy;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }

    public boolean isStock() {
        return isStock;
    }

    public void setStock(boolean stock) {
        isStock = stock;
    }

    public String getId() {
        return id;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }
}

