package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.LeadsBE;

public class CreateLeadResponse extends BaseResponse {

    @SerializedName("responseData")
    private LeadsBE mLeadsBE;

    public LeadsBE getmLeadsBE() {
        return mLeadsBE;
    }

    public void setmLeadsBE(LeadsBE mLeadsBE) {
        this.mLeadsBE = mLeadsBE;
    }
}
