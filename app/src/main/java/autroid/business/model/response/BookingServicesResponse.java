package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.BookingServicesBE;


public class BookingServicesResponse extends BaseResponse {
    @SerializedName("responseData")
    private ArrayList<BookingServicesBE> services;

    public ArrayList<BookingServicesBE> getServices() {
        return services;
    }

    public void setServices(ArrayList<BookingServicesBE> services) {
        this.services = services;
    }
}
