package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.LeadDetailBE;

public class LeadDetailsResponse extends BaseResponse {

    @SerializedName("responseData")
    LeadDetailBE leadDetail;

    public LeadDetailBE getLeadDetail() {
        return leadDetail;
    }
}
