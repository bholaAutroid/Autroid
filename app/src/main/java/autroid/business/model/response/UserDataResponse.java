package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.VendorInfoBE;

public class UserDataResponse extends BaseResponse {
    @SerializedName("responseData")
    private VendorInfoBE getUserData;

    public VendorInfoBE getGetUserData() {
        return getUserData;
    }

    public void setGetUserData(VendorInfoBE getUserData) {
        this.getUserData = getUserData;
    }
}
