package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.LeadsBE;

public class LeadsResponse extends BaseResponse {

    @SerializedName("responseData")
    private ArrayList<LeadsBE> leadsBES;

    @SerializedName("responseInfo")
    TotalResponse totalResponse;

    public ArrayList<LeadsBE> getLeadsBES() {
        return leadsBES;
    }

    public TotalResponse getTotalResponse() {
        return totalResponse;
    }
}
