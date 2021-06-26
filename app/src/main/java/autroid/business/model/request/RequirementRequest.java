package autroid.business.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.RequirementBE;

public class RequirementRequest {

    @SerializedName("booking")
    String bookingId;

    @SerializedName("demands")
    ArrayList<RequirementBE> arrayList;

    @SerializedName("technician")
    String technicianId;

    @SerializedName("driver")
    String driverId;

    String delivery_date,claim,cashless,name,contact_no,delivery_time;

    String driver_accident,accident_date,manufacture_year,accident_time,accident_place,accident_cause,spot_survey,fir;

    boolean is_driver;

    public void setManufacture_year(String manufacture_year) {
        this.manufacture_year = manufacture_year;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public void setDriver_accident(String driver_accident) {
        this.driver_accident = driver_accident;
    }

    public void setAccident_date(String accident_date) {
        this.accident_date = accident_date;
    }

    public void setAccident_time(String accident_time) {
        this.accident_time = accident_time;
    }

    public void setAccident_place(String accident_place) {
        this.accident_place = accident_place;
    }

    public void setAccident_cause(String accident_cause) {
        this.accident_cause = accident_cause;
    }

    public void setSpot_survey(String spot_survey) {
        this.spot_survey = spot_survey;
    }

    public void setFir(String fir) {
        this.fir = fir;
    }

    public void setTime(String time) {
        this.delivery_time = time;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public ArrayList<RequirementBE> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<RequirementBE> arrayList) {
        this.arrayList = arrayList;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    public void setCashless(String cashless) {
        this.cashless = cashless;
    }

    public void setIs_driver(boolean is_driver) {
        this.is_driver = is_driver;
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

}
