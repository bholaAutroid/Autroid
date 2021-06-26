package autroid.business.model.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import autroid.business.model.bean.ServiceBE;


/**
 * Created by pranav.mittal on 11/15/17.
 */

public class AddBookingRequest implements Serializable {
    private String booking,car,business,date,time_slot,total_amount,payment_mode,payment_status,status,convenience,transaction_id,address,requirements,charges;
    private ArrayList<ServiceBE> services;
    @SerializedName("package")
    private String packages;
    @SerializedName("for")
    private String forPackage;
    private String label;
    private Boolean fromCategory=true;

    public String getBooking() {
        return booking;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public Boolean getFromCategory() {
        return fromCategory;
    }

    public void setFromCategory(Boolean fromCategory) {
        this.fromCategory = fromCategory;
    }

    public String getForPackage() {
        return forPackage;
    }

    public void setForPackage(String forPackage) {
        this.forPackage = forPackage;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private Boolean is_services;

    public Boolean getIs_services() {
        return is_services;
    }

    public void setIs_services(Boolean is_services) {
        this.is_services = is_services;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
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

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConvenience() {
        return convenience;
    }

    public void setConvenience(String convenience) {
        this.convenience = convenience;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public ArrayList<ServiceBE> getServices() {
        return services;
    }

    public void setServices(ArrayList<ServiceBE> services) {
        this.services = services;
    }
}
