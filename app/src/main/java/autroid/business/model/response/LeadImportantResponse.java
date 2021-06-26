package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

public class LeadImportantResponse extends BaseResponse {
    @SerializedName("responseData")
    private GetImp getImp;

    public GetImp getGetImp() {
        return getImp;
    }

    public void setGetImp(GetImp getImp) {
        this.getImp = getImp;
    }

    public class GetImp{
        private Boolean important;

        public Boolean getImportant() {
            return important;
        }

        public void setImportant(Boolean important) {
            this.important = important;
        }
    }
}
