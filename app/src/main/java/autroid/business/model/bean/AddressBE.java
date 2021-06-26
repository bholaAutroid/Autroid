package autroid.business.model.bean;

import java.io.Serializable;

/**
 * Created by pranav.mittal on 02/10/18.
 */

public class AddressBE implements Serializable {

    private String location,address,time_zone,zip,city,user,id,state,landmark,country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUser() {
        return user;
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getLocation() {
        return location;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }
}
