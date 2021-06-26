package autroid.business.model.request;

import com.google.gson.annotations.SerializedName;

public class JobCardBookingRequest{

    @SerializedName("booking")
    String bookingId;

    @SerializedName("car")
    String carId;

    @SerializedName("user")
    String userId;

    String registration_no,variant,vin,engine_no,insurance_company,policy_no,expire,policy_holder,requirement;

    int odometer,fuel_level,premium;

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public void setPolicy_holder(String policy_holder) {
        this.policy_holder = policy_holder;
    }

    public void setPremium(int premium) {
        this.premium = premium;
    }

    public String getBooking() {
        return bookingId;
    }

    public void setBooking(String booking) {
        this.bookingId = booking;
    }

    public String getCar() {
        return carId;
    }

    public void setCar(String car) {
        this.carId = car;
    }

    public String getUser() {
        return userId;
    }

    public void setUser(String user) {
        this.userId = user;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }


    public void setVin(String vin) {
        this.vin = vin;
    }


    public void setEngine_no(String engine_no) {
        this.engine_no = engine_no;
    }


    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }


    public void setFuel_level(int fuel_level) {
        this.fuel_level = fuel_level;
    }


    public void setInsurance_company(String insurance_company) {
        this.insurance_company = insurance_company;
    }


    public void setPolicy_no(String policy_no) {
        this.policy_no = policy_no;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }
}
