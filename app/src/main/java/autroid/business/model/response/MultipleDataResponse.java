package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.MultipleDataBE;

public class MultipleDataResponse extends BaseResponse {

    @SerializedName("responseData")
    MultipleDataBE multipleDataBE;

    public MultipleDataBE getMultipleDataBE() {
        return multipleDataBE;
    }

    public void setMultipleDataBE(MultipleDataBE multipleDataBE) {
        this.multipleDataBE = multipleDataBE;
    }
}
