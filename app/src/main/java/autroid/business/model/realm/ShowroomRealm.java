package autroid.business.model.realm;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by pranav.mittal on 06/26/17.
 */

public class ShowroomRealm extends RealmObject {

    private String location;
    private Double latitude;
    private Double longitude;
    private String businessName,businessId;
    private String cover;
    private float rating;
    private RealmList<MediaRealm> media;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public RealmList<MediaRealm> getMedia() {
        return media;
    }

    public void setMedia(RealmList<MediaRealm> media) {
        this.media = media;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

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

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
