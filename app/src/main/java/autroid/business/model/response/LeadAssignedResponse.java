package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.LeadAssignedBE;

public class LeadAssignedResponse extends BaseResponse{

    @SerializedName("responseData")
    ArrayList<LeadAssignedBE> arrayList;

    @SerializedName("responseInfo")
    TotalResponse totalResponse;

    public ArrayList<LeadAssignedBE> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<LeadAssignedBE> arrayList) {
        this.arrayList = arrayList;
    }

    public TotalResponse getTotalResponse() {
        return totalResponse;
    }

    public void setTotalResponse(TotalResponse totalResponse) {
        this.totalResponse = totalResponse;
    }
}
