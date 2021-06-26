package autroid.business.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewRequest {

    float rating,recommendation;

    String booking,type,review,status;

    @SerializedName("review_points")
    ArrayList<String> points;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setRecommendation(float recommendation) {
        this.recommendation = recommendation;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setPoints(ArrayList<String> points) {
        this.points = points;
    }
}
