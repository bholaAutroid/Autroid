package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.SearchUserBE;

public class UserSearchResponse extends BaseResponse {
    @SerializedName("responseData")
    private ArrayList<SearchUserBE> getSearchUser;

    public ArrayList<SearchUserBE> getGetSearchUser() {
        return getSearchUser;
    }

    public void setGetSearchUser(ArrayList<SearchUserBE> getSearchUser) {
        this.getSearchUser = getSearchUser;
    }
}
