package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

public class CarSoldResponse extends BaseResponse {
    @SerializedName("responseData")
    private GetSoldData getSoldData;

    public GetSoldData getGetSoldData() {
        return getSoldData;
    }

    public void setGetSoldData(GetSoldData getSoldData) {
        this.getSoldData = getSoldData;
    }

    public class GetSoldData{
        private String buyer,owner,seller,id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBuyer() {
            return buyer;
        }

        public void setBuyer(String buyer) {
            this.buyer = buyer;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getSeller() {
            return seller;
        }

        public void setSeller(String seller) {
            this.seller = seller;
        }
    }
}
