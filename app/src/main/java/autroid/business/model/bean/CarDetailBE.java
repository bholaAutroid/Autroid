package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pranav.mittal on 06/10/17.
 */

public class CarDetailBE implements Serializable {

    private String purchased_year,odometer,carId,careager_rating,created_at,description,driven,id,location,maker,manufacture_year,mileage,model,modelName,model_id,owner,posted_by,premium,price,registration_no,variant,variant_id,vehicle_color,vehicle_status,title,transmission,accidental,service_history,fuel_type,insurance,body_style,engine_no, vin,rc_address,ic_address,ic,rc;
    private String latitude,longitude;
    private String _variant,_automaker,_model;
    private float refurbishment_cost,purchase_price,numericPrice;
    private  boolean publish,admin_approved,isChatEnable;
    private ArrayList<Double> geometry;
    private ArrayList<ThumbnailBE> thumbnails=new ArrayList<>();
    private ShowroomProfileBE user;
    private ArrayList<VariantDetailBE> variant_details;

    public String getPurchased_year() {
        return purchased_year;
    }

    public void setPurchased_year(String purchased_year) {
        this.purchased_year = purchased_year;
    }

    public boolean isChatEnable() {
        return isChatEnable;
    }

    public void setChatEnable(boolean chatEnable) {
        isChatEnable = chatEnable;
    }

    @SerializedName("package")
    private PurchasedPackagesBE packageData;

    @SerializedName("insurance_info")
    private InsuranceDataBE insuranceDataBE;

    public boolean isPublish() {
        return publish;
    }

    public boolean isAdmin_approved() {
        return admin_approved;
    }

    public PurchasedPackagesBE getPackageData() {
        return packageData;
    }

    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    public InsuranceDataBE getInsuranceDataBE() {
        return insuranceDataBE;
    }

    public void setInsuranceDataBE(InsuranceDataBE insuranceDataBE) {
        this.insuranceDataBE = insuranceDataBE;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getRc_address() {
        return rc_address;
    }

    public void setRc_address(String rc_address) {
        this.rc_address = rc_address;
    }

    public String getIc_address() {
        return ic_address;
    }

    public void setIc_address(String ic_address) {
        this.ic_address = ic_address;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getBody_style() {
        return body_style;
    }

    public void setBody_style(String body_style) {
        this.body_style = body_style;
    }

    public ArrayList<Double> getGeometry() {
        return geometry;
    }

    public void setGeometry(ArrayList<Double> geometry) {
        this.geometry = geometry;
    }

    public Boolean getPublish() {
        return publish;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }


    public ArrayList<VariantDetailBE> getVariant_details() {
        return variant_details;
    }

    public void setVariant_details(ArrayList<VariantDetailBE> variant_details) {
        this.variant_details = variant_details;
    }

    public ArrayList<ThumbnailBE> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(ArrayList<ThumbnailBE> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public ShowroomProfileBE getUser() {
        return user;
    }

    public void setUser(ShowroomProfileBE user) {
        this.user = user;
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

    public String getService_history() {
        return service_history;
    }

    public void setService_history(String service_history) {
        this.service_history = service_history;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getCareager_rating() {
        return careager_rating;
    }

    public void setCareager_rating(String careager_rating) {
        this.careager_rating = careager_rating;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDriven() {
        return driven;
    }

    public void setDriven(String driven) {
        this.driven = driven;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getManufacture_year() {
        return manufacture_year;
    }

    public void setManufacture_year(String manufacture_year) {
        this.manufacture_year = manufacture_year;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(String variant_id) {
        this.variant_id = variant_id;
    }

    public String getVehicle_color() {
        return vehicle_color;
    }

    public void setVehicle_color(String vehicle_color) {
        this.vehicle_color = vehicle_color;
    }

    public String getVehicle_status() {
        return vehicle_status;
    }

    public void setVehicle_status(String vehicle_status) {
        this.vehicle_status = vehicle_status;
    }

    public String getTitle() {
        return title;
    }

    public String getEngine_no() {
        return engine_no;
    }

    public void setEngine_no(String engine_no) {
        this.engine_no = engine_no;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getAdmin_approved() {
        return admin_approved;
    }

    public float getRefurbishment_cost() {
        return refurbishment_cost;
    }

    public float getPurchase_price() {
        return purchase_price;
    }

    public float getNumericPrice() {
        return numericPrice;
    }

    public String get_variant() {
        return _variant;
    }

    public void set_variant(String _variant) {
        this._variant = _variant;
    }

    public String get_automaker() {
        return _automaker;
    }

    public void set_automaker(String _automaker) {
        this._automaker = _automaker;
    }

    public String get_model() {
        return _model;
    }

    public void set_model(String _model) {
        this._model = _model;
    }
}
