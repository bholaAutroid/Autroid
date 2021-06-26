package autroid.business.model.response;

import java.util.ArrayList;

import autroid.business.model.bean.AccountInfoBE;
import autroid.business.model.bean.BookingsBE;

public class UserResponseData {

    String id,name,email,contact_no;
    AccountInfoBE account_info;

    ArrayList<BookingsBE> bookings;

    public AccountInfoBE getAccount_info() {
        return account_info;
    }

    public void setAccount_info(AccountInfoBE account_info) {
        this.account_info = account_info;
    }

    public ArrayList<BookingsBE> getBookings() {
        return bookings;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }
}
