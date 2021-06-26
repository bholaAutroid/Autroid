package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.GarageCarBE;

public class GarageCarResponse extends BaseResponse {

    @SerializedName("responseData")
    GarageCarBE garagecar;

    public GarageCarBE getGaragecar() {
        return garagecar;
    }
}
