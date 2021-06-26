package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

public class ForgotPasswordResponse extends BaseResponse{

    @SerializedName("responseData")
    private GetId getId;

    public GetId getGetId() {
        return getId;
    }

    public void setGetId(GetId getId) {
        this.getId = getId;
    }

    public class GetId{
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
