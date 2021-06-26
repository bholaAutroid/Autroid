package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import autroid.business.model.bean.CarDetailBE;

public class GetUserCarResponse extends BaseResponse{

    @SerializedName("responseData")
    private List<CarDetailBE> getCarDetails;

    public List<CarDetailBE> getGetCarDetails() {
        return getCarDetails;
    }

    public void setGetCarDetails(List<CarDetailBE> getCarDetails) {
        this.getCarDetails = getCarDetails;
    }
}
