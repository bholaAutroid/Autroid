package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.DriverDataBE;

public class GetDriverResponse extends BaseResponse {

    @SerializedName("responseData")
    DriverDataBE driverDataBE;

    public DriverDataBE getDriverDataBE() {
        return driverDataBE;
    }

    public void setDriverDataBE(DriverDataBE driverDataBE) {
        this.driverDataBE = driverDataBE;
    }
}
