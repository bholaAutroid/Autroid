package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.ServicesDataBE;

public class GetBookedServicesResponse extends BaseResponse {

    @SerializedName("responseData")
    ArrayList<ServicesDataBE> servicesList;

    public ArrayList<ServicesDataBE> getServicesList() {
        return servicesList;
    }

    public void setServicesList(ArrayList<ServicesDataBE> servicesList) {
        this.servicesList = servicesList;
    }
}
