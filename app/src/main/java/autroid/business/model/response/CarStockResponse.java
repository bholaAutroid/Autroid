package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.CarDetailBE;

/**
 * Created by pranav.mittal on 06/10/17.
 */


public class CarStockResponse extends BaseResponse {

    @SerializedName("responseData")
    private CarDetailBE getCarDetails;

    public CarDetailBE getGetCarDetails() {
        return getCarDetails;
    }

    public void setGetCarDetails(CarDetailBE getCarDetails) {
        this.getCarDetails = getCarDetails;
    }
}
