package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddReviewRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.view.fragment.AddShowroomReviewFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 03/07/18.
 */

public class ShowroomReviewPresenter {

    private AddShowroomReviewFragment mFragment;
    private ViewGroup mMainLayout;

    public ShowroomReviewPresenter(AddShowroomReviewFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void addReview(AddReviewRequest addReviewRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.addReview(addReviewRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BaseResponse baseResponse = response.body();
                            mFragment.onSuccess(baseResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
    }
}
