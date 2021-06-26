package autroid.business.model.request;

public class UpdateCarDetailsRequest {

    private String user,booking,car,title,registration_no,variant,vin,engine_no,manufacture_year,purchased_year,odometer,fuel_level;


    public String getUser() {
        return user;
    }

    public String getBooking() {
        return booking;
    }

    public String getCar() {
        return car;
    }

    public String getTitle() {
        return title;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public String getVariant() {
        return variant;
    }

    public String getVin() {
        return vin;
    }

    public String getEngine_no() {
        return engine_no;
    }

    public String getManufacture_year() {
        return manufacture_year;
    }

    public void setManufacture_year(String manufacture_year) {
        this.manufacture_year = manufacture_year;
    }

    public String getPurchased_year() {
        return purchased_year;
    }

    public void setPurchased_year(String purchased_year) {
        this.purchased_year = purchased_year;
    }

    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    public String getFuel_level() {
        return fuel_level;
    }

    public void setFuel_level(String fuel_level) {
        this.fuel_level = fuel_level;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
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
}
