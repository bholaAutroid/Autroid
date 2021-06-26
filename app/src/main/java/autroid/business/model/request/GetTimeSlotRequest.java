package autroid.business.model.request;

import java.util.ArrayList;

import autroid.business.model.bean.ServiceBE;


public class GetTimeSlotRequest {
    private String business,date,booking,label;
    private ArrayList<ServiceBE> services;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBooking() {
        return booking;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ServiceBE> getServices() {
        return services;
    }

    public void setServices(ArrayList<ServiceBE> services) {
        this.services = services;
    }
}
