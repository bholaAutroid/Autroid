package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.UserBE;

/**
 * Created by pranav.mittal on 09/13/17.
 */

public class UserProfileResponse extends BaseResponse {
    @SerializedName("responseData")
    private UserBE responseData;

    public UserBE getResponseData() {
        return responseData;
    }

    public void setResponseData(UserBE responseData) {
        this.responseData = responseData;
    }
}
