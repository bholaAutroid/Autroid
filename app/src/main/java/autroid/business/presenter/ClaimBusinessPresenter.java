package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.ClaimBusinessRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.view.fragment.ClaimBusinessFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 08/13/17.
 */

public class ClaimBusinessPresenter {

    private ClaimBusinessFragment mFragment;
    private ViewGroup mMainLayout;

    public ClaimBusinessPresenter(ClaimBusinessFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void claimBussiness(ClaimBusinessRequest claimBusinessRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.claimBusiness(claimBusinessRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BaseResponse baseResponse = response.body();
                            mFragment.claimBussSuccess(baseResponse);
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
