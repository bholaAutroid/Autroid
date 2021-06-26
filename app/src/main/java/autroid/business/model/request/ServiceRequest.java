package autroid.business.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pranav.mittal on 01/16/18.
 */

public class ServiceRequest {
    private String car,business,type,paint;
    @SerializedName("package")
    private String packages;

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPaint() {
        return paint;
    }

    public void setPaint(String paint) {
        this.paint = paint;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }
}
