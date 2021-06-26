package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.OverviewBE;

public class BusinessOverviewResponse extends BaseResponse{

    @SerializedName("responseData")
    ArrayList<OverviewBE> overViewList;

    public ArrayList<OverviewBE> getOverViewList() {
        return overViewList;
    }
}
