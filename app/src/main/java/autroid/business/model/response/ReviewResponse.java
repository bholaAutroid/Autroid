package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.RatingDataBE;

public class ReviewResponse extends BaseResponse{

    @SerializedName("responseData")
    ArrayList<RatingDataBE> ratingData;

    public ArrayList<RatingDataBE> getRatingData() {
        return ratingData;
    }
}
