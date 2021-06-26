package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.UpdateBusinessLocationRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.view.fragment.UpdateBusinessLocationFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 06/23/17.
 */

public class UpdateBusinessLocationPresenter {

    private UpdateBusinessLocationFragment mActivity;
    private ViewGroup mMainLayout;

    public UpdateBusinessLocationPresenter(UpdateBusinessLocationFragment frag, ViewGroup mainLayout) {
        mActivity = frag;
        mMainLayout = mainLayout;
    }


    public void updateLocation(UpdateBusinessLocationRequest updateBusinessLocationRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.updateLocation(updateBusinessLocationRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mActivity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BaseResponse baseResponse = response.body();
                            mActivity.onSuccess(baseResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity.getActivity(), mMainLayout,Boolean.TRUE);
    }
}

