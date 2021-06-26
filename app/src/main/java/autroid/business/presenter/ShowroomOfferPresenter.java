package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.ShowroomOfferResponse;
import autroid.business.view.fragment.profile.ShowroomOffersFragment;
import retrofit2.Response;

public class ShowroomOfferPresenter {

    private ShowroomOffersFragment mFragment;
    private ViewGroup mMainLayout;

    public ShowroomOfferPresenter(ShowroomOffersFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getAllOffers(String showroomID, final int page) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ShowroomOfferResponse> myCall = apiRequest.getShowroomOffers(showroomID,page);
        myCall.enqueue(new ApiCallback.MyCallback<ShowroomOfferResponse>() {
            @Override
            public void success(final Response<ShowroomOfferResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ShowroomOfferResponse showroomOfferResponse = response.body();
                            mFragment.onSuccess(showroomOfferResponse,page);
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
