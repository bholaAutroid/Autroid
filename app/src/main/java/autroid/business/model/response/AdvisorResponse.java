package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.AdvisorBE;

public class AdvisorResponse extends BaseResponse{

    @SerializedName("responseData")
    ArrayList<AdvisorBE> advisorsList;

    public ArrayList<AdvisorBE> getAdvisorsList() {
        return advisorsList;
    }
}
