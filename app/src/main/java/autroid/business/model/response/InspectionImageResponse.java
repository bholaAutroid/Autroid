package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.InspectionDataBE;

public class InspectionImageResponse extends BaseResponse {

    @SerializedName("responseData")
    InspectionDataBE inspectionDataBE;

    public InspectionDataBE getInspectionDataBE() {
        return inspectionDataBE;
    }

    public void setInspectionDataBE(InspectionDataBE inspectionDataBE) {
        this.inspectionDataBE = inspectionDataBE;
    }
}
