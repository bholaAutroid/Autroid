package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.CarItemsBE;

/**
 * Created by pranav.mittal on 06/01/17.
 */

public class CarModelsResponse extends BaseResponse {

    @SerializedName("responseData")
    private ArrayList<CarItemsBE> models;



        public ArrayList<CarItemsBE> getModels() {
            return models;
        }

        public void setModels(ArrayList<CarItemsBE> models) {
            this.models = models;
        }

}
