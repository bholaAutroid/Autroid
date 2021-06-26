package autroid.business.model.bean;

import java.util.ArrayList;

/**
 * Created by pranav.mittal on 11/21/17.
 */

public class BookingsBE {
    private String id,booking_no,convenience,created_at,date,status,time_slot,updated_at;
    private String order_no,job_no,listing;

    private ShowroomProfileBE business;

    private UserBE user;
    private ArrayList<ServiceBE> services;
    private CarDetailBE car;
    private ServicePaymentBE payment;
    private PaymentBE due;

    private BookingAddressBE address;

    public ShowroomProfileBE getBusiness() {
        return business;
    }

    public void setBusiness(ShowroomProfileBE business) {
        this.business = business;
    }

    public PaymentBE getDue() {
        return due;
    }

    public void setDue(PaymentBE due) {
        this.due = due;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getJob_no() {
        return job_no;
    }

    public void setJob_no(String job_no) {
        this.job_no = job_no;
    }

    public String getListing() {
        return listing;
    }

    public void setListing(String listing) {
        this.listing = listing;
    }

    public BookingAddressBE getAddress() {
        return address;
    }

    public void setAddress(BookingAddressBE address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBooking_no() {
        return booking_no;
    }

    public void setBooking_no(String booking_no) {
        this.booking_no = booking_no;
    }

    public String getConvenience() {
        return convenience;
    }

    public void setConvenience(String convenience) {
        this.convenience = convenience;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public UserBE getUser() {
        return user;
    }

    public void setUser(UserBE user) {
        this.user = user;
    }

    public ArrayList<ServiceBE> getServices() {
        return services;
    }

    public void setServices(ArrayList<ServiceBE> services) {
        this.services = services;
    }

    public CarDetailBE getCar() {
        return car;
    }

    public void setCar(CarDetailBE car) {
        this.car = car;
    }

    public ServicePaymentBE getPayment() {
        return payment;
    }

    public void setPayment(ServicePaymentBE payment) {
        this.payment = payment;
    }
}
