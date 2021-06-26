package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.ShowroomReviewResponse;
import autroid.business.view.fragment.profile.ShowroomReviewsFragment;
import retrofit2.Response;

public class ShowroomReviewsPresenter {

    private ShowroomReviewsFragment mFragment;
    private ViewGroup mMainLayout;

    public ShowroomReviewsPresenter(ShowroomReviewsFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getAllReviews(String showroomID, final int page) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ShowroomReviewResponse> myCall = apiRequest.getShowroomReviews(showroomID,page);
        myCall.enqueue(new ApiCallback.MyCallback<ShowroomReviewResponse>() {
            @Override
            public void success(final Response<ShowroomReviewResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ShowroomReviewResponse showroomReviewResponse = response.body();
                            mFragment.onSuccess(showroomReviewResponse,page);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.FALSE);
    }
}
