package autroid.business.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.ParticularsDataBE;

public class UpdateRequest {

    String address;

    @SerializedName("booking")
    String bookingId;

    @SerializedName("assets")
    ArrayList<ParticularsDataBE> arrayList;

    @SerializedName("other")
    String otherDetails;

    String convenience,charges;

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getConvenience() {
        return convenience;
    }

    public void setConvenience(String convenience) {
        this.convenience = convenience;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public ArrayList<ParticularsDataBE> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<ParticularsDataBE> arrayList) {
        this.arrayList = arrayList;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

}
