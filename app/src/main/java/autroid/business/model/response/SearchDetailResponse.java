package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.SearchUserBE;

public class SearchDetailResponse extends BaseResponse {

    @SerializedName("responseData")
    private SearchUserBE getSearchUser;

    public SearchUserBE getGetSearchUser() {
        return getSearchUser;
    }

    public void setGetSearchUser(SearchUserBE getSearchUser) {
        this.getSearchUser = getSearchUser;
    }
}
