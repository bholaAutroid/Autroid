package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.AnalyticsBE;

/**
 * Created by pranav.mittal on 08/04/17.
 */

public class AnalyticsResponse extends BaseResponse{

    @SerializedName("responseData")
    ArrayList<AnalyticsBE> analyticsData;

    public ArrayList<AnalyticsBE> getAnalyticsData() {
        return analyticsData;
    }
}
