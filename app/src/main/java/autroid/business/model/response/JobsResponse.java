package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.JobsBE;

public class JobsResponse extends BaseResponse {

    @SerializedName("responseData")
    private ArrayList<JobsBE> getJobs;

    @SerializedName("responseInfo")
    TotalResponse totalResponse;

    public ArrayList<JobsBE> getGetJobs() {
        return getJobs;
    }

    public TotalResponse getTotalResponse() {
        return totalResponse;
    }
}
