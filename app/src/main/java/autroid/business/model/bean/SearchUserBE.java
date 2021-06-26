package autroid.business.model.bean;

import java.util.ArrayList;

public class SearchUserBE {

    private String id,name,contact_no,email,avatar_address;
    private ArrayList<CarDetailBE> cars;
    private ArrayList<BookingsBE> bookings;
    private ArrayList<LeadsBE> leads;

    public String getAvatar_address() {
        return avatar_address;
    }

    public void setAvatar_address(String avatar_address) {
        this.avatar_address = avatar_address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<LeadsBE> getLeads() {
        return leads;
    }

    public void setLeads(ArrayList<LeadsBE> leads) {
        this.leads = leads;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public ArrayList<CarDetailBE> getCars() {
        return cars;
    }

    public void setCars(ArrayList<CarDetailBE> cars) {
        this.cars = cars;
    }

    public ArrayList<BookingsBE> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<BookingsBE> bookings) {
        this.bookings = bookings;
    }
}
