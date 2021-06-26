package autroid.business.presenter.carsales;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.view.fragment.carsales.ServiceDetailsFragment;
import retrofit2.Response;

public class ServiceDetailsPresenter {

    private ServiceDetailsFragment mFragment;
    private ViewGroup mMainLayout;

    public ServiceDetailsPresenter(ServiceDetailsFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getServiceDetails(String id,String type) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.getVendorServicesDetails(id,type);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BaseResponse vendorServicesResponse = response.body();
                            mFragment.onSuccess(vendorServicesResponse);
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
