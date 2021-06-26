package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.InsuranceDueBE;

public class InsuranceDueResponse extends BaseResponse {

    @SerializedName("responseData")
    private ArrayList<InsuranceDueBE> getInsuranceDue;

    @SerializedName("responseInfo")
    private TotalResponse totalResponse;

    public ArrayList<InsuranceDueBE> getGetInsuranceDue() {
        return getInsuranceDue;
    }

    public TotalResponse getTotalResponse() {
        return totalResponse;
    }
}
