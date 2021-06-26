package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.BookingCategoryResponse;
import autroid.business.view.fragment.search.EstimateFragment;
import retrofit2.Response;

public class EstimatePresenter {

    private EstimateFragment mFragment;
    private ViewGroup mMainLayout;

    public EstimatePresenter(EstimateFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void bookingCategory(String car) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingCategoryResponse> myCall = apiRequest.getBookingCategory(car);
        myCall.enqueue(new ApiCallback.MyCallback<BookingCategoryResponse>() {
            @Override
            public void success(final Response<BookingCategoryResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingCategoryResponse bookingCategoryResponse = response.body();
                            mFragment.onSuccess(bookingCategoryResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {

            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }
}
