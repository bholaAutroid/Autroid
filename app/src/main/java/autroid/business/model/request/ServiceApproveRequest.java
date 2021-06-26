package autroid.business.model.request;

import java.util.ArrayList;

import autroid.business.model.bean.ServiceBE;


public class ServiceApproveRequest {

    private String booking;
    private ArrayList<ServiceBE> services;

    public String getBooking() {
        return booking;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public ArrayList<ServiceBE> getServices() {
        return services;
    }

    public void setServices(ArrayList<ServiceBE> services) {
        this.services = services;
    }
}
