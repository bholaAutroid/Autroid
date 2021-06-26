package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.UserSearchBE;

public class EditLeadResponse extends BaseResponse {

    @SerializedName("responseData")
    UserSearchBE user;

    public UserSearchBE getUser() {
        return user;
    }

    public void setUser(UserSearchBE user) {
        this.user = user;
    }
}
