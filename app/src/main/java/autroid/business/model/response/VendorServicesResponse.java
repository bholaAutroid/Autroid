package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.BookingPackagesBE;

/**
 * Created by pranav.mittal on 06/25/17.
 */

public class VendorServicesResponse extends BaseResponse {

    @SerializedName("responseData")
    private ArrayList<BookingPackagesBE> getServices;

    public ArrayList<BookingPackagesBE> getGetServices() {
        return getServices;
    }

    public void setGetServices(ArrayList<BookingPackagesBE> getServices) {
        this.getServices = getServices;
    }
}
