package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.TechniciansDataBE;

public class GetTechniciansResponse extends BaseResponse {


    @SerializedName("responseData")
    ArrayList<TechniciansDataBE> technicians;

    public ArrayList<TechniciansDataBE> getTechnicians() {
        return technicians;
    }

    public void setTechnicians(ArrayList<TechniciansDataBE> technicians) {
        this.technicians = technicians;
    }
}
