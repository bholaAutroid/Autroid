package autroid.business.model.request;

/**
 * Created by pranav.mittal on 06/23/17.
 */

public class UpdateBusinessLocationRequest {
    private String location;
    private Double latitude,longitude;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
