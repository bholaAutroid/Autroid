package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BookingPackagesBE {
    @SerializedName("package")
    private String packages;

    private ArrayList<BookingServicesBE> services;

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public ArrayList<BookingServicesBE> getServices() {
        return services;
    }

    public void setServices(ArrayList<BookingServicesBE> services) {
        this.services = services;
    }
}
