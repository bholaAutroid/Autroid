package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

public class DriverDataBE {

    @SerializedName("id")
    String driverId;

    String name,email;

    public String getEmail() {
        return email;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
