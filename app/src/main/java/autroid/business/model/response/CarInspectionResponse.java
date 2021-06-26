package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.InspectionDataBE;

public class CarInspectionResponse extends BaseResponse {

    @SerializedName("responseData")
    ArrayList<InspectionDataBE> inspectionData;

    public ArrayList<InspectionDataBE> getInspectionData() {
        return inspectionData;
    }
}
