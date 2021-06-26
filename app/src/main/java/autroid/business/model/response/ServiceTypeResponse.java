package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.ServiceTypeBE;

/**
 * Created by pranav.mittal on 06/26/17.
 */

public class ServiceTypeResponse extends BaseResponse{

    @SerializedName("responseData")
    private ArrayList<ServiceTypeBE> getServiceType;

    public ArrayList<ServiceTypeBE> getGetServiceType() {
        return getServiceType;
    }

    public void setGetServiceType(ArrayList<ServiceTypeBE> getServiceType) {
        this.getServiceType = getServiceType;
    }
}
