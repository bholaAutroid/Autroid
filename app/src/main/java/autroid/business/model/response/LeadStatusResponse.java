package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LeadStatusResponse extends BaseResponse {

    @SerializedName("responseData")
    private ArrayList<String> statusList;

    public ArrayList<String> getStatusList() {
        return statusList;
    }
}
