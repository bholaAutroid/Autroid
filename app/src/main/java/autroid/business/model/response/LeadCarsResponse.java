package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.CarDetailBE;

public class LeadCarsResponse extends BaseResponse{

    @SerializedName("responseData")
    ArrayList<CarDetailBE> cars;

    public ArrayList<CarDetailBE> getCars() {
        return cars;
    }
}
