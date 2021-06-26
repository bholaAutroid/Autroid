package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.PinDataBE;

public class BusinessPinResponse extends BaseResponse {

    @SerializedName("responseData")
    ArrayList<PinDataBE> pinData;

    public ArrayList<PinDataBE> getPinData() {
        return pinData;
    }
}
