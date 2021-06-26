package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.ServiceDueBE;

public class ServiceDueResponse extends BaseResponse {

    @SerializedName("responseData")
    private ArrayList<ServiceDueBE> getServiceData;

    @SerializedName("responseInfo")
    private TotalResponse totalResponse;

    public ArrayList<ServiceDueBE> getGetServiceData() {
        return getServiceData;
    }

    public void setGetServiceData(ArrayList<ServiceDueBE> getServiceData) {
        this.getServiceData = getServiceData;
    }

    public TotalResponse getTotalResponse() {
        return totalResponse;
    }

    public void setTotalResponse(TotalResponse totalResponse) {
        this.totalResponse = totalResponse;
    }
}
