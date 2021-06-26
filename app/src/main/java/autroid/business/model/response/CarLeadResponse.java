package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

public class CarLeadResponse extends BaseResponse {
    @SerializedName("responseData")
    private GetData getData;

    public GetData getGetData() {
        return getData;
    }

    public void setGetData(GetData getData) {
        this.getData = getData;
    }

    public class GetData{
        private Boolean expired;

        public Boolean getExpired() {
            return expired;
        }

        public void setExpired(Boolean expired) {
            this.expired = expired;
        }
    }
}
