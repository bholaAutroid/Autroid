package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.JobsQCBE;

public class JobsQCResponse extends BaseResponse {

    @SerializedName("responseData")
    private ArrayList<JobsQCBE> getJobsQC;

    public ArrayList<JobsQCBE> getGetJobsQC() {
        return getJobsQC;
    }

    public void setGetJobsQC(ArrayList<JobsQCBE> getJobsQC) {
        this.getJobsQC = getJobsQC;
    }
}
