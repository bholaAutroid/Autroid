package autroid.business.model.response;

import java.util.ArrayList;

import autroid.business.model.bean.OfferBE;

/**
 * Created by pranav.mittal on 06/11/17.
 */

public class OffersResponse extends BaseResponse {

    private GetOffers responseData;

    public GetOffers getResponseData() {
        return responseData;
    }

    public void setResponseData(GetOffers responseData) {
        this.responseData = responseData;
    }

    public  class GetOffers{
        private String total,published;
        private ArrayList<OfferBE> offers;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getPublished() {
            return published;
        }

        public void setPublished(String published) {
            this.published = published;
        }

        public ArrayList<OfferBE> getOffers() {
            return offers;
        }

        public void setOffers(ArrayList<OfferBE> offers) {
            this.offers = offers;
        }
    }
}
