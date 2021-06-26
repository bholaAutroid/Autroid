package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.CarDetailBE;

public class ShowroomCarsResponse extends BaseResponse {
    @SerializedName("responseData")
    private ArrayList<CarDetailBE> carDetailBES;

    public ArrayList<CarDetailBE> getCarDetailBES() {
        return carDetailBES;
    }

    public void setCarDetailBES(ArrayList<CarDetailBE> carDetailBES) {
        this.carDetailBES = carDetailBES;
    }
}
