package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.PinDataBE;

public class PinResponse extends BaseResponse {

    @SerializedName("responseData")
    PinDataBE pinData;

    public PinDataBE getPinData() {
        return pinData;
    }

    public void setPinData(PinDataBE pinData) {
        this.pinData = pinData;
    }
}
