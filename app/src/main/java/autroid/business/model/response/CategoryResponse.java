package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryResponse extends BaseResponse {

    @SerializedName("responseData")
    ArrayList<String> categoryList;

    public ArrayList<String> getCategoryList() {
        return categoryList;
    }
}
