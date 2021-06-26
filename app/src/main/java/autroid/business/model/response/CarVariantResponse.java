package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.CarItemsBE;

/**
 * Created by pranav.mittal on 06/01/17.
 */

public class CarVariantResponse extends BaseResponse {

    @SerializedName("responseData")
        private ArrayList<CarItemsBE> variants;

        public ArrayList<CarItemsBE> getVaiants() {
            return variants;
        }

        public void setVaiants(ArrayList<CarItemsBE> vaiants) {
            this.variants = vaiants;
        }

}
