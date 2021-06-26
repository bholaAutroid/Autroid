package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.RatingReviewBE;

public class ShowroomReviewResponse extends BaseResponse {
    @SerializedName("responseData")
    private ArrayList<RatingReviewBE> ratingReviewBES;

    public ArrayList<RatingReviewBE> getRatingReviewBES() {
        return ratingReviewBES;
    }

    public void setRatingReviewBES(ArrayList<RatingReviewBE> ratingReviewBES) {
        this.ratingReviewBES = ratingReviewBES;
    }
}
