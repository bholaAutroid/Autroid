package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.ServiceBE;

public class BookingEstimateResponse extends BaseResponse {
    @SerializedName("responseData")
    private ArrayList<ServiceBE> serviceBES;

    public ArrayList<ServiceBE> getServiceBES() {
        return serviceBES;
    }

    public void setServiceBES(ArrayList<ServiceBE> serviceBES) {
        this.serviceBES = serviceBES;
    }
}
