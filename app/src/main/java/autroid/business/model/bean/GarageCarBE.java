package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GarageCarBE implements Serializable {

    private String id,title,modelName,accidental,insurance,description,carId /*Like booking no*/,manufacture_year,registration_no,fuel_type,variant,odometer,location,ic,rc,ic_address,rc_address;

    @SerializedName("package")
    private String myPackage;

    private double numericPrice,purchase_price,refurbishment_cost;

    private UserBE user;

    @SerializedName("insurance_info")
    InsuranceDataBE insuranceData;

    @SerializedName("body_style")
    ArrayList<SpecificationsBE> bodyStyle;

    @SerializedName("owner")
    ArrayList<SpecificationsBE> owner;

    @SerializedName("transmission")
    ArrayList<SpecificationsBE> transmission;

    @SerializedName("vehicle_color")
    ArrayList<SpecificationsBE> vehicleColor;

    ArrayList<Double> geometry;
    
    ArrayList<ThumbnailBE> thumbnails;

    public String getInsurance() {
        return insurance;
    }

    public double getPurchase_price() {
        return purchase_price;
    }

    public double getRefurbishment_cost() {
        return refurbishment_cost;
    }

    public ArrayList<ThumbnailBE> getThumbnails() {
        return thumbnails;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getModelName() {
        return modelName;
    }

    public String getAccidental() {
        return accidental;
    }

    public String getDescription() {
        return description;
    }

    public String getCarId() {
        return carId;
    }

    public String getManufacture_year() {
        return manufacture_year;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public InsuranceDataBE getInsuranceData() {
        return insuranceData;
    }

    public double getNumericPrice() {
        return numericPrice;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public String getVariant() {
        return variant;
    }

    public ArrayList<SpecificationsBE> getBodyStyle() {
        return bodyStyle;
    }

    public ArrayList<SpecificationsBE> getOwner() {
        return owner;
    }

    public ArrayList<SpecificationsBE> getTransmission() {
        return transmission;
    }

    public ArrayList<SpecificationsBE> getVehicleColor() {
        return vehicleColor;
    }

    public String getOdometer() {
        return odometer;
    }

    public String getLocation() {
        return location;
    }

    public ArrayList<Double> getGeometry() {
        return geometry;
    }

    public String getIc() {
        return ic;
    }

    public String getRc() {
        return rc;
    }

    public String getIc_address() {
        return ic_address;
    }

    public String getRc_address() {
        return rc_address;
    }

    public UserBE getUser() {
        return user;
    }

    public String getMyPackage() {
        return myPackage;
    }
}
