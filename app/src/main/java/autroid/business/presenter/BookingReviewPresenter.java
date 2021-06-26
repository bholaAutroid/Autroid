package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.ReviewRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.ReviewResponse;
import autroid.business.view.fragment.booking.BookingReviewFragment;
import retrofit2.Response;

public class BookingReviewPresenter {

    private BookingReviewFragment bookingReviewFragment;

    private ViewGroup viewGroup;

    public BookingReviewPresenter(BookingReviewFragment bookingReviewFragment,ViewGroup viewGroup){
        this.bookingReviewFragment=bookingReviewFragment;
        this.viewGroup=viewGroup;
    }

    public void getReviewQuestions(String bookingId) {
        ApiRequest apiRequest = ApiFactory.createService(bookingReviewFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ReviewResponse> myCall = apiRequest.getReviewQuestions(bookingId);
        myCall.enqueue(new ApiCallback.MyCallback<ReviewResponse>() {
            @Override
            public void success(final Response<ReviewResponse> response) {
                bookingReviewFragment.getActivity().runOnUiThread(()->{
                    bookingReviewFragment.onSuccessReview(response.body());
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, bookingReviewFragment.getActivity(), viewGroup, Boolean.TRUE);
    }


    public void setReviews(ReviewRequest reviewRequest) {
        ApiRequest apiRequest = ApiFactory.createService(bookingReviewFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.setReviewQuestions(reviewRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                bookingReviewFragment.getActivity().runOnUiThread(()->{
                    bookingReviewFragment.onSuccessReviewAdded(response.body());
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, bookingReviewFragment.getActivity(), viewGroup, Boolean.TRUE);

    }
}
