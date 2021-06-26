package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.SearchCarBE;

public class SearchCarResponse extends BaseResponse {
    @SerializedName("responseData")
    ArrayList<SearchCarBE> searchCarBE;

    public ArrayList<SearchCarBE> getSearchCarBE() {
        return searchCarBE;
    }

    public void setSearchCarBE(ArrayList<SearchCarBE> searchCarBE) {
        this.searchCarBE = searchCarBE;
    }
}
