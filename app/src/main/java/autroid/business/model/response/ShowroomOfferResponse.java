package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.OfferBE;

public class ShowroomOfferResponse extends BaseResponse {
    @SerializedName("responseData")
    private ArrayList<OfferBE> offerBES;

    public ArrayList<OfferBE> getOfferBES() {
        return offerBES;
    }

    public void setOfferBES(ArrayList<OfferBE> offerBES) {
        this.offerBES = offerBES;
    }
}
