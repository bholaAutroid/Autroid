package autroid.business.model.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import autroid.business.model.bean.InsuranceDataBE;

/**
 * Created by pranav.mittal on 06/12/17.
 */

public class AddCarRequest implements Serializable {

    private String maker="";
    private String model="";
    private String variant="";
    private String description="";
    private String vehicle_color="";
    private String owner="";
    private String manufacture_year="";
    private String location="";
    private String registration_no="";
    private String odometer="";
    private String vehicle_status="";
    private String transmission="";
    private String accidental="";
    private String insurance="";
    private String fuel_type="";
    private String user="";

    public void setUser(String user) {
        this.user = user;
    }

    private String body_style="";
    private String car="";
    private boolean booking,publish,is_package;
    private float price,refurbishment_cost,purchase_price;
    private double latitude,longitude;

    @SerializedName("insurance_info")
    private InsuranceDataBE insuranceData;

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public Boolean getBooking() {
        return booking;
    }

    public void setBooking(Boolean booking) {
        this.booking = booking;
    }

    public String getBody_style() {
        return body_style;
    }

    public void setBody_style(String body_style) {
        this.body_style = body_style;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getAccidental() {
        return accidental;
    }

    public void setAccidental(String accidental) {
        this.accidental = accidental;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVehicle_color() {
        return vehicle_color;
    }

    public void setVehicle_color(String vehicle_color) {
        this.vehicle_color = vehicle_color;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getManufacture_year() {
        return manufacture_year;
    }

    public void setManufacture_year(String manufacture_year) {
        this.manufacture_year = manufacture_year;
    }

    public double getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
    }

    public String getVehicle_status() {
        return vehicle_status;
    }

    public void setVehicle_status(String vehicle_status) {
        this.vehicle_status = vehicle_status;
    }

    public void setInsuranceData(InsuranceDataBE insuranceData) {
        this.insuranceData = insuranceData;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public void setIs_package(boolean is_package) {
        this.is_package = is_package;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setRefurbishment_cost(float refurbishment_cost) {
        this.refurbishment_cost = refurbishment_cost;
    }

    public void setPurchase_price(float purchase_price) {
        this.purchase_price = purchase_price;
    }
}
