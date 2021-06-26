package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MultipleDataBE {

    @SerializedName("booking")
    ArrayList<BookingsBE> bookingsList;

    @SerializedName("user")
    ArrayList<UserBE> usersList;

    @SerializedName("order")
    ArrayList<OrderDataBE> orderList;

    @SerializedName("lead")
    ArrayList<LeadsBE> leadsList;

    public ArrayList<LeadsBE> getLeadsList() {
        return leadsList;
    }

    public void setLeadsList(ArrayList<LeadsBE> leadsList) {
        this.leadsList = leadsList;
    }

    public ArrayList<OrderDataBE> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<OrderDataBE> orderList) {
        this.orderList = orderList;
    }

    public ArrayList<BookingsBE> getBookingsList() {
        return bookingsList;
    }

    public void setBookingsList(ArrayList<BookingsBE> bookingsList) {
        this.bookingsList = bookingsList;
    }

    public ArrayList<UserBE> getUsersList() {
        return usersList;
    }

    public void setUsersList(ArrayList<UserBE> usersList) {
        this.usersList = usersList;
    }
}
