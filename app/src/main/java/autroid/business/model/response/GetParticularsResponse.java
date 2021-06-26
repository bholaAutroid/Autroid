package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import autroid.business.model.bean.ParticularsDataBE;

public class GetParticularsResponse extends BaseResponse {

    @SerializedName("responseData")
    List<ParticularsDataBE> getParticulars;

    public List<ParticularsDataBE> getGetParticulars() {
        return getParticulars;
    }

    public void setGetParticulars(List<ParticularsDataBE> getParticulars) {
        this.getParticulars = getParticulars;
    }
}
