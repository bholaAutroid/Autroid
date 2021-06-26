package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.OfferBE;

/**
 * Created by pranav.mittal on 06/11/17.
 */

public class AddOfferResponse  extends BaseResponse{
    @SerializedName("responseData")
    private OfferResponse offerResponse;

    public OfferResponse getOfferResponse() {
        return offerResponse;
    }

    public void setOfferResponse(OfferResponse offerResponse) {
        this.offerResponse = offerResponse;
    }

    public class OfferResponse{
        OfferBE item;

        public OfferBE getItem() {
            return item;
        }

        public void setItem(OfferBE item) {
            this.item = item;
        }
    }
}
